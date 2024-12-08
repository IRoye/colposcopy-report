package report;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static report.IdentifyUniquePersion.getCellValue;
import static report.IdentifyUniquePersion.getColumnIndex;

public class PositiveTime {

    private static final Map<String, String> chineseToNumberMap = new HashMap<>();


    static {
        chineseToNumberMap.put("一", "1");
        chineseToNumberMap.put("二", "2");
        chineseToNumberMap.put("三", "3");
        chineseToNumberMap.put("四", "4");
        chineseToNumberMap.put("五", "5");
        chineseToNumberMap.put("六", "6");
        chineseToNumberMap.put("七", "7");
        chineseToNumberMap.put("八", "8");
        chineseToNumberMap.put("九", "9");
        chineseToNumberMap.put("十", "10");
        chineseToNumberMap.put("两", "2");  // 处理“两”
        chineseToNumberMap.put("1+", "1");
        chineseToNumberMap.put("2+", "2");
        chineseToNumberMap.put("3+", "3");
        chineseToNumberMap.put("4+", "4");
        chineseToNumberMap.put("5+", "5");
    }

    public static void main(String[] args) {
        String inputFile = "/Users/royeyu/Downloads/grouped_patients_data.xlsx";  // 替换为实际文件路径
        String outputFile = "/Users/royeyu/Downloads/grouped_patients_hpv_data.xlsx";

        try {
            Workbook book = new XSSFWorkbook(inputFile);
            Sheet sheet = book.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();

            Row headerRow = rowIterator.next();
            //  分析数据来源
            int confirmDateColIndex = getColumnIndex(headerRow, "组织学确诊");
            int caseAttributesColIndex = getColumnIndex(headerRow, "病例属性");
            int cellResultColIndex = getColumnIndex(headerRow, "宫颈细胞学结果");

            // 新增列：HPV阳性时间
            int positiveTimeColIndex = headerRow.getPhysicalNumberOfCells();
            headerRow.createCell(positiveTimeColIndex).setCellValue("HPV阳性时间");

            // 新增列：病理
            int pathologyColIndex = positiveTimeColIndex + 1;
            headerRow.createCell(pathologyColIndex).setCellValue("病理");

            // 新增列：病理号
            int pathologyNumberColIndex = positiveTimeColIndex + 2;
            headerRow.createCell(pathologyNumberColIndex).setCellValue("病理号");

            // 新增列：TCT
            int tctColIndex = positiveTimeColIndex + 3;
            headerRow.createCell(tctColIndex).setCellValue("TCT");

            // 新增列：术后
            int afterSurColIndex = positiveTimeColIndex + 4;
            headerRow.createCell(afterSurColIndex).setCellValue("术后");

            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);

                // 【1】、HPV阳性时间
                String confirmDate = getCellValue(row, confirmDateColIndex);
                if (confirmDate != null && confirmDate.contains("阳性")) {
                    confirmDate = replaceChineseWithNumbers(confirmDate);
                    Pattern pattern = Pattern.compile("阳性(\\d+)(天|个月|月|年半|年)(\\d+)?(个月|月)?");
                    Matcher matcher = pattern.matcher(confirmDate);

                    // 查找并打印匹配到的内容
                    while (matcher.find()) {

                        String value = matcher.group(1);
                        String unit = matcher.group(2);

                        int month = -1;
                        // 将其计算为月份：

                        if ("年半".equals(unit)) {
                            month = Math.round(Integer.parseInt(value) * 12) + 6;
                        }
                        if ("年".equals(unit)) {
                            month = Math.round(Integer.parseInt(value) * 12);
                        }

                        if ("月".equals(unit) || "个月".equals(unit)) {
                            month = Integer.parseInt(value);
                        }

                        if ("天".equals(unit)) {
                            month = Math.round(Integer.parseInt(value) / 30);
                        }

                        // 提取第二个时间段
                        String secondValue = matcher.group(3); // 第二个数字 (例如 6)
                        String secondUnit = matcher.group(4);  // 第二个单位 (例如 "个月")

                        if (secondValue != null && secondUnit != null) {
                            if ("月".equals(secondUnit) || "个月".equals(secondUnit)) {
                                month = month + Integer.parseInt(secondValue);
                            }
                        }
                        System.out.printf("原始值：%s, 数字 %s, 单位 %s, 月 %d \n", confirmDate, matcher.group(1), matcher.group(2), month);
                        // 写入对应列
                        row.createCell(positiveTimeColIndex).setCellValue(month);
                    }
                }

                // 【2】、处理病理 CIN1 CIN2 CIN3 CIN2-3 LSIL HSIL
                String caseAttributesData = getCellValue(row, caseAttributesColIndex);
                if (caseAttributesData != null) {
                    Pattern pattern = Pattern.compile("(CIN1|CIN2|CIN2-3?|CIN3|LSIL|HSIL)");
                    Matcher matcher = pattern.matcher(caseAttributesData);
                    List<String> list = new ArrayList<>();

                    while (matcher.find()) {
                        // 遍历所有捕获的分组
                        for (int j = 1; j <= matcher.groupCount(); j++) {
                            // 获取每个捕获分组的值
                            String groupValue = matcher.group(j);
                            list.add(groupValue);
                        }
                    }

                    Set<String> set = new HashSet<>(list);
                    List<String> uniqueList = new ArrayList<>(set);

                    if (uniqueList.contains("HSIL")) {
                        row.createCell(pathologyColIndex).setCellValue("CIN2,CIN3");
                    } else {
                        row.createCell(pathologyColIndex).setCellValue(String.join(",", uniqueList));
                    }
                }

                // 【3】、处理病理号
                if (caseAttributesData != null) {
                    Pattern pattern = Pattern.compile("\\d{4,}(\\.\\d+)?");
                    Matcher matcher = pattern.matcher(caseAttributesData);

                    if (matcher.find()) {
                        // 提取到的数字
                        String numberStr = matcher.group();
                        row.createCell(pathologyNumberColIndex).setCellValue(numberStr);
                    }
                }

                // 【4】、宫颈细胞学结果
                String cellResultData = getCellValue(row, cellResultColIndex);
                if (cellResultData != null) {

                    /**
                     * TCT
                     * NILM  ASCUS  ASC-H   HSIL   LSIL   AGC  轻度炎症
                     */
                    Pattern pattern = Pattern.compile("(NILM|ASCUS|ASC-H|HSIL|LSIL|AGC|轻度炎症)");
                    Matcher matcher = pattern.matcher(cellResultData);

                    List<String> list = new ArrayList<>();
                    while (matcher.find()) {
                        // 遍历所有捕获的分组
                        for (int k = 1; k <= matcher.groupCount(); k++) {
                            // 获取每个捕获分组的值
                            String groupValue = matcher.group(k);
                            list.add(groupValue);
                        }
                    }

                    Set<String> set = new HashSet<>(list);
                    List<String> uniqueList = new ArrayList<>(set);

                    row.createCell(tctColIndex).setCellValue(String.join(",", uniqueList));
                }

                // 【5】、术后
                if (confirmDate.contains("术后")) {

                    Pattern pattern = Pattern.compile("术后(\\d+)(天|月|年半|年)");
                    Matcher matcher = pattern.matcher(confirmDate);

                    // 查找并打印匹配到的内容
                    while (matcher.find()) {

                        String value = matcher.group(1);
                        String unit = matcher.group(2);

                        int month = -1;
                        // 将其计算为月份：

                        if ("年半".equals(unit)) {
                            month = Math.round(Integer.parseInt(value) * 12) + 6;
                        }
                        if ("年".equals(unit)) {
                            month = Math.round(Integer.parseInt(value) * 12);
                        }

                        if ("月".equals(unit)) {
                            month = Integer.parseInt(value);
                        }

                        if ("天".equals(unit)) {
                            month = Math.round(Integer.parseInt(value) / 30);
                        }

                        // 写入对应列
                        row.createCell(afterSurColIndex).setCellValue(month);
                    }
                }
            }

            FileOutputStream fos = new FileOutputStream(new File(outputFile));
            book.write(fos);
            fos.close();

            System.out.println("HPV类型处理完成，结果已保存到：" + outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 使用 replace() 替换中文数字为阿拉伯数字
    public static String replaceChineseWithNumbers(String input) {
        for (Map.Entry<String, String> entry : chineseToNumberMap.entrySet()) {
            input = input.replace(entry.getKey(), entry.getValue()); // 替换每个中文数字为阿拉伯数字
        }
        return input;
    }
}
