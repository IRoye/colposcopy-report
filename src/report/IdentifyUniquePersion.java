package report;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Pattern;

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
            int numberColIndex = getColumnIndex(headerRow, "编号");
            int nameColIndex = getColumnIndex(headerRow, "姓名");
            int otherColIndex = getColumnIndex(headerRow, "其他");
            int detectionMethodColIndex = getColumnIndex(headerRow, "检测方法");
            int quantitativeColIndex = getColumnIndex(headerRow, "定量");

            // 日期和年龄需要做特别处理，因为数据不合规
            int checkDateColIndex = getColumnIndex(headerRow, "检查日期");
            int ageColIndex = getColumnIndex(headerRow, "年龄");


            // 添加一个新的列：出生年份
            int birthYearColIndex = headerRow.getPhysicalNumberOfCells();
            headerRow.createCell(birthYearColIndex).setCellValue("出生年份");

            // 添加GroupID列
            int groupIdColIndex = birthYearColIndex + 1;
            headerRow.createCell(groupIdColIndex).setCellValue("GroupID");

            // 处理数据行
            int currentGroupId = 1;

            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                String name = getCellValue(row, nameColIndex);

                // 【1】、获取其他列的值
                String otherStr = getCellValue(row, otherColIndex);

                /**
                 * 如果其他列不为空，按照规则处理数据:
                 * 有HC2字眼，U列检测方法 写HC2，W列 定量 写 冒号后面的数值，抽取规则为：
                 * 如果包含HC2字眼，那么以：拆分，冒号后面的内容作为值，分别填充入：
                 */
                setOtherCellValue(otherStr, row, detectionMethodColIndex, quantitativeColIndex);

                // 【2】、检查日期处理
                String checkDateStr = getCellValue(row, checkDateColIndex);
                String checkDateRegex = "^\\d{4}-\\d{2}-\\d{2}$";

                // 【3】、年龄处理
                String ageStr = getCellValue(row, ageColIndex);
                String ageWithoutSuffix = ageStr.replace("岁", "");
                String ageRegex = "^[1-9]\\d*$";
                int currentAge = -1;
                // 检查年龄是否为数字
                if (ageWithoutSuffix.matches(ageRegex)) {
                    currentAge = Integer.parseInt(ageWithoutSuffix);
                }

                // 【4】、根据检查日期和年龄，计算出生年，随后根据出生年和姓名判断是否为同一个人
                int birthYear = -1;
                try {
                    // 转换检查日期为年份
                    int checkYear = getYearFromDate(checkDateStr);
                    birthYear = checkYear - currentAge;
                } catch (Exception e) {
                    // TODO
                }
                // 设置出生年列
                row.createCell(birthYearColIndex).setCellValue(birthYear);

                // 【5】、如果当前行没有分配组号，则给它分配一个新的组号
                if (row.getCell(groupIdColIndex) == null || row.getCell(groupIdColIndex).getCellType() == CellType.BLANK) {
                    row.createCell(groupIdColIndex).setCellValue(currentGroupId); // 在第三列创建组号
                }


                for (int j = i + 1; j < sheet.getPhysicalNumberOfRows(); j++) {
                    // 获取当前行
                    Row otherRow = sheet.getRow(j);

                    String otherOtherStr = getCellValue(otherRow, otherColIndex);
                    setOtherCellValue(otherOtherStr, otherRow, detectionMethodColIndex, quantitativeColIndex);

                    // 如果当前行不为null，说明已经设置了-1或者正常的组值，直接略过判断
                    if (otherRow.getCell(groupIdColIndex) != null) {
                        // TODO
                    }

                    // 获取另一个人的姓名
                    String otherName = getCellValue(otherRow, nameColIndex);

                    // 获取另一个人的检查日期
                    String otherCheckDate = getCellValue(otherRow, checkDateColIndex);
                    // 判断日期格式是否匹配
                    if (!otherCheckDate.matches(checkDateRegex)) {
                        otherCheckDate = "-1";
                    }
                    // 非法检查日期，跳过改行
                    if (otherCheckDate.equals("-1")) {
                        otherRow.createCell(groupIdColIndex).setCellValue(-1);
                    }

                    // 年龄处理，非法内容使用-1
                    String otherAgeStr = getCellValue(otherRow, ageColIndex);
                    String otherAgeWithoutSuffix = otherAgeStr.replace("岁", "");
                    int otherCurrentAge = -1;
                    if (otherAgeWithoutSuffix.matches(ageRegex)) {
                        otherCurrentAge = Integer.parseInt(otherAgeWithoutSuffix);
                    }

                    // 非法年龄，跳过该行
                    if (otherCurrentAge == -1) {
                        otherRow.createCell(groupIdColIndex).setCellValue(-1);
                    }

                    // 处理出生年
                    int otherBirthYear = -1;
                    try {
                        // 转换检查日期为年份
                        int checkYear = getYearFromDate(otherCheckDate);
                        otherBirthYear = checkYear - otherCurrentAge;
                    } catch (Exception e) {
                        // TODO
                    }
                    if (otherBirthYear != -1) {
                        otherRow.createCell(birthYearColIndex).setCellValue(otherBirthYear);
                    } else {
                        // TODO
                    }

                    // 如果名字相同 并且 出生日期想差不超过2岁，那么设置同样的currentGroupId
                    if (otherName.equals(name) && Math.abs(birthYear - otherBirthYear) <= 2 ) {
                        otherRow.createCell(groupIdColIndex).setCellValue(currentGroupId);
                    }
                }

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
        if (!"".equals(otherColumnValue)) {

            if (otherColumnValue.contains("HC2")) {
                // 检测方法列
                System.out.println(otherColumnValue);
                System.out.println(detectionMethodColIndex);
                Cell detectionMethodCell = row.getCell(detectionMethodColIndex);
                // 定量列
                Cell quantitativeCell = row.getCell(quantitativeColIndex);
                detectionMethodCell.setCellValue("HC2");

                /**
                 * 外院：HPV(-)
                 * HC2_HPV:1103.32
                 * 格式明显不一样
                 */
                if (otherColumnValue.contains("：")) {

                    quantitativeCell.setCellValue(otherColumnValue.split("：")[1]);
                }

                if (otherColumnValue.contains(":")) {
                    quantitativeCell.setCellValue(otherColumnValue.split(":")[1]);
                }
            }
        }

    }
}
