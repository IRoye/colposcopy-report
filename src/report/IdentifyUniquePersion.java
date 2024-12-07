package report;


import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class IdentifyUniquePersion {
    public static void main(String[] args) {
        // Excel文件路径
        String inputFile = "/Users/royeyu/Downloads/HPV20240626.xlsx";  // 替换为实际文件路径
        String outputFile = "/Users/royeyu/Downloads/grouped_patients_data.xlsx";

        try {
            Workbook book = new XSSFWorkbook(inputFile);
            Sheet sheet = book.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();

            Row headerRow = rowIterator.next();

            // 提前计算各列index
            int numberColIndex = getColumnIndex(headerRow, "编号");
            int nameColIndex = getColumnIndex(headerRow, "姓名");
            int phoneNumberColIndex = getColumnIndex(headerRow, "联系电话");
            int otherColIndex = getColumnIndex(headerRow, "其他");
            int detectionMethodColIndex = getColumnIndex(headerRow, "检测方法");
            int quantitativeColIndex = getColumnIndex(headerRow, "定量");

            // 日期和年龄需要做特别处理，因为数据不合规
            int checkDateColIndex = getColumnIndex(headerRow, "检查日期");
            int ageColIndex = getColumnIndex(headerRow, "年龄");

            // 新增：出生年份列
            int birthYearColIndex = headerRow.getPhysicalNumberOfCells();
            headerRow.createCell(birthYearColIndex).setCellValue("出生年份");

            // 新增：最开始开始检查 至 最后结束检查 间隔月列
            int checkInternalColIndex = birthYearColIndex + 1;
            headerRow.createCell(checkInternalColIndex).setCellValue("检查间隔月数");

            // 新增：GroupID列，用于分组
            int groupIdColIndex = birthYearColIndex + 2;
            headerRow.createCell(groupIdColIndex).setCellValue("GroupID");

            // 处理数据行
            int currentGroupId = 1;

            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);

                // 【1】、获取基本信息
                // 姓名列
                String name = getCellValue(row, nameColIndex);
                // 手机号列
                String phoneNumber = getCellValue(row, phoneNumberColIndex);
                // 获取检查日期的List
                List<String> checkDateList = new ArrayList<>();

                // 【2】、如果当前行没有分配组号，则给它分配一个新的组号；如果已经设置了组号，则无需处理
                if (row.getCell(groupIdColIndex) == null || row.getCell(groupIdColIndex).getCellType() == CellType.BLANK) {
                    row.createCell(groupIdColIndex).setCellValue(currentGroupId); // 在第三列创建组号
                } else {
                    continue;
                }

                /**
                 * 【3】、处理”其他“列数据，规则如下：
                 * 如果其他列不为空，按照规则处理数据：
                 * 有HC2字眼，U列检测方法 写HC2，W列 定量 写 冒号后面的数值，抽取规则为：
                 * 如果包含HC2字眼，那么以：拆分，冒号后面的内容作为值，分别填充入：
                 */
                String otherStr = getCellValue(row, otherColIndex);
                setOtherCellValue(otherStr, row, detectionMethodColIndex, quantitativeColIndex);

                // 【4】、检查日期处理
                String checkDateStr = getCellValue(row, checkDateColIndex);
                // 检查为年月日的格式
                String checkDateRegex = "^\\d{4}-\\d{2}-\\d{2}$";
                if (checkDateStr.matches(checkDateRegex)) {
                    checkDateList.add(checkDateStr);
                }

                // 【5】、年龄处理
                String ageStr = getCellValue(row, ageColIndex);
                String ageWithoutSuffix = ageStr.replace("岁", "");
                String ageRegex = "^[1-9]\\d*$";
                int currentAge = -1;
                // 判断年龄是否为合法数字
                if (ageWithoutSuffix.matches(ageRegex)) {
                    currentAge = Integer.parseInt(ageWithoutSuffix);
                }

                // 【6】、如果检查日期 和 年龄都为合法，继续计算出生年月，否则无意义
                int birthYear = -1;
                if (checkDateStr.matches(checkDateRegex) && currentAge != -1) {
                    // 【5.1】、根据检查日期和年龄，计算出生年，随后根据出生年和姓名判断是否为同一个人
                    birthYear = -1;
                    try {
                        // 转换检查日期为年份
                        int checkYear = getYearFromDate(checkDateStr);
                        birthYear = checkYear - currentAge;
                    } catch (Exception e) {
                        // TODO
                    }
                    // 设置出生年列 为 正常 出生年
                    row.createCell(birthYearColIndex).setCellValue(birthYear);
                } else {
                    // 设置出生年列 为 -1
                    row.createCell(birthYearColIndex).setCellValue(-1);
                }

                /**
                 *
                 * 【7】、根据sheet.getRow(i)，继续遍历判断后续数据（目前数据只能遍历），是否和sheet.getRow(i)为同一个人，若为同一个人，使用同一个currentGroupId
                 *  判断依据：
                 *  ”姓名相同并且出生年月相差2岁“ 或者 ”手机号相同“，则视为同一个人
                 */
                for (int j = i + 1; j < sheet.getPhysicalNumberOfRows(); j++) {
                    // 【7.1】、获取当前行
                    Row otherRow = sheet.getRow(j);

                    // 【7.2】、若当前行已经设置了组号，表示已经进行过了处理，则略过即可
                    if (otherRow.getCell(groupIdColIndex) != null && otherRow.getCell(groupIdColIndex).getCellType() != CellType.BLANK) {
                        continue;
                    }

                    String otherOtherStr = getCellValue(otherRow, otherColIndex);
                    setOtherCellValue(otherOtherStr, otherRow, detectionMethodColIndex, quantitativeColIndex);

                    // 【7.3】、获取基本信息
                    String otherName = getCellValue(otherRow, nameColIndex);
                    String otherPhoneNumber = getCellValue(otherRow, phoneNumberColIndex);

                    // 【7.4】、获取另一个人的检查日期
                    String otherCheckDate = getCellValue(otherRow, checkDateColIndex);

                    // 【7.5】、年龄处理，非法内容使用-1
                    String otherAgeStr = getCellValue(otherRow, ageColIndex);
                    String otherAgeWithoutSuffix = otherAgeStr.replace("岁", "");
                    int otherCurrentAge = -1;
                    if (otherAgeWithoutSuffix.matches(ageRegex)) {
                        otherCurrentAge = Integer.parseInt(otherAgeWithoutSuffix);
                    }

                    // 【7.5】、如果检查日期 和 年龄都为合法，继续计算出生年月，否则无意义；
                    if (otherCheckDate.matches(checkDateRegex) && otherCurrentAge != -1) {
                        // 【7.5.1】、处理出生年
                        int otherBirthYear = -1;
                        try {
                            // 转换检查日期为年份
                            int checkYear = getYearFromDate(otherCheckDate);
                            otherBirthYear = checkYear - otherCurrentAge;
                        } catch (Exception e) {
                            // TODO
                        }
                        otherRow.createCell(birthYearColIndex).setCellValue(otherBirthYear);

                        //  【7.5.2】、如果名字相同 并且 出生日期想差不超过2岁，那么设置同样的currentGroupId；如果检查日期不合法，则判断手机号，手机号如果一致，也可视为同一人
                        if (otherName.equals(name) && Math.abs(birthYear - otherBirthYear) <= 2) {
                            otherRow.createCell(groupIdColIndex).setCellValue(currentGroupId);

                            // 如果此时为同一人，记录此时的检查日期（注意时间格式）
                            if (otherCheckDate.matches(checkDateRegex)) {
                                checkDateList.add(otherCheckDate);
                            }
                        } else {
                            if (StringUtils.isNotBlank(phoneNumber) && StringUtils.isNotBlank(otherPhoneNumber)) {
                                if (phoneNumber.equals(otherPhoneNumber)) {
                                    otherRow.createCell(groupIdColIndex).setCellValue(currentGroupId);
                                }
                            }
                        }
                    } else {
                        // 【7.6】、如果检查日期不合法，则判断手机号，手机号如果一致，也可视为同一人
                        if (StringUtils.isNotBlank(phoneNumber) && StringUtils.isNotBlank(otherPhoneNumber)) {
                            if (phoneNumber.equals(otherPhoneNumber)) {
                                otherRow.createCell(groupIdColIndex).setCellValue(currentGroupId);

                                // 如果此时为同一人，记录此时的检查日期（注意时间格式）
                                if (otherCheckDate.matches(checkDateRegex)) {
                                    checkDateList.add(otherCheckDate);
                                }
                            }
                        }
                    }
                }

                // 如果此时获得了合格的时间格式，那么设置checkInternalColIndex列的值为:checkDateList中最大值减去最小值的月份间隔
                int intervals = calculateMonths(checkDateList);
                Cell intervalCell = row.createCell(checkInternalColIndex);
                intervalCell.setCellValue(intervals);

                currentGroupId++;
            }

            FileOutputStream fos = new FileOutputStream(new File(outputFile));
            book.write(fos);
            fos.close();

            System.out.println("处理完成，结果已保存到：" + outputFile);


            System.out.println(numberColIndex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int getColumnIndex(Row row, String columnName) {
        Iterator<Cell> cellIterator = row.iterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if (cell.getStringCellValue().equals(columnName)) {
                return cell.getColumnIndex();
            }
        }
        return -1;
    }

    // 从日期字符串中提取年份（假设日期格式为 yyyy-mm-dd）
    private static int getYearFromDate(String dateStr) throws Exception {
        // 假设日期格式是yyyy-MM-dd，其他格式需要调整解析方式
        String[] parts = dateStr.split("-");
        if (parts.length == 3) {
            return Integer.parseInt(parts[0]);  // 返回年份部分
        } else {
            return -1;
        }
    }

    // 获取单元格值
    private static String getCellValue(Row row, int colIndex) {
        Cell cell = row.getCell(colIndex);
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            default:
                return "";
        }
    }

    /**
     * @param otherColumnValue        当前需判断的其他行的值
     * @param row                     当前行
     * @param detectionMethodColIndex 需赋值的key。赋值到检测方法列，这里为检测方法的index
     * @param quantitativeColIndex    需赋值的value。赋值到定量列，这里为定量列的index
     * @return void
     * @description: 对“其他“列进行拆分重新赋值
     * 如果其他列不为空，按照规则处理数据:
     * 有HC2字眼，U列检测方法 写HC2，W列 定量 写 冒号后面的数值，抽取规则为：
     * 如果包含HC2字眼，那么以：拆分，冒号后面的内容作为值，分别填充入：
     */
    private static void setOtherCellValue(String otherColumnValue, Row row, int detectionMethodColIndex, int quantitativeColIndex) {
        // TODO 如果detectionMethodColIndex、quantitativeColIndex有值，则表示已经处理过，可直接return
        if (!"".equals(otherColumnValue)) {

            if (otherColumnValue.contains("HC2")) {
                // 检测方法列
                Cell detectionMethodCell = row.getCell(detectionMethodColIndex);
                if (detectionMethodCell == null) {
                    row.createCell(detectionMethodColIndex).setCellValue("");
                    detectionMethodCell = row.getCell(detectionMethodColIndex);
                }

                // 定量列
                Cell quantitativeCell = row.getCell(quantitativeColIndex);
                if (quantitativeCell == null) {
                    row.createCell(quantitativeColIndex).setCellValue("");
                    quantitativeCell = row.getCell(quantitativeColIndex);
                }

                // 检测方法列固定设置HC2
                try {
                    detectionMethodCell.setCellValue("HC2");
                } catch (Exception e) {
                    System.out.printf("错误记录，otherColumnValue：%s， detectionMethodColIndex：%d， quantitativeColIndex：%d \n", otherColumnValue, detectionMethodColIndex, quantitativeColIndex);
                }

                /**
                 * 外院：HPV(-)
                 * HC2_HPV:1103.32
                 * 格式明显不一样
                 */
                System.out.printf("抽取的值：%s \n", otherColumnValue);
                if (otherColumnValue.contains("：")) {
                    // 可能有分隔，但是没值的情况
                    String[] values = otherColumnValue.split("：");
                    quantitativeCell.setCellValue(values.length > 1 ? otherColumnValue.split("：")[1] : "");
                }

                if (otherColumnValue.contains(":")) {
                    // 可能有分隔，但是没值的情况
                    String[] values = otherColumnValue.split(":");
                    quantitativeCell.setCellValue(values.length > 1 ? otherColumnValue.split(":")[1] : "");
                }
            }
        }

    }

    /**
     * 计算最大年月日和最小年月日的月份间隔
     *
     * @param dateList 收集的年份数据
     */
    private static int calculateMonths(List<String> dateList) {

        if (dateList.size() == 0 || dateList.size() == 1) return 0;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 将日期字符串转换为 LocalDate 列表并排序
        List<LocalDate> dates = dateList.stream()
                .map(dateStr -> LocalDate.parse(dateStr, formatter))  // 将字符串转换为 LocalDate
                .sorted()  // 按照日期升序排序
                .collect(Collectors.toList());

        // 获取最小和最大日期
        LocalDate minDate = dates.get(0);
        LocalDate maxDate = dates.get(dates.size() - 1);

        // 计算最小日期和最大日期之间的月份差
        return (maxDate.getYear() - minDate.getYear()) * 12 + maxDate.getMonthValue() - minDate.getMonthValue();
    }
}
