package report;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import report.constants.HpvTypes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static report.IdentifyUniquePersion.getCellValue;
import static report.IdentifyUniquePersion.getColumnIndex;

public class AfterSurgery {


    public static void main(String[] args) {

        String inputFile = "/Users/royeyu/Downloads/grouped_patients_data.xlsx";  // 替换为实际文件路径
        String outputFile = "/Users/royeyu/Downloads/grouped_patients_hpv_data.xlsx";

        try {
            Workbook book = new XSSFWorkbook(inputFile);
            Sheet sheet = book.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();

            Row headerRow = rowIterator.next();
            int confirmDateColIndex = getColumnIndex(headerRow, "组织学确诊");
            int afterSurColIndex = getColumnIndex(headerRow, "术后");

            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);

                // 如果为空列，则跳过
                if (row.getCell(confirmDateColIndex) == null || row.getCell(confirmDateColIndex).getCellType() == CellType.BLANK) {
                    continue;
                }

                // 获取其他列的值
                String confirmDate = getCellValue(row, confirmDateColIndex);

                if (!confirmDate.contains("术后")) {
                    continue;
                }

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

                    System.out.printf("原始值：%s, 数字 %s, 单位 %s, 月 %d \n", confirmDate, matcher.group(1), matcher.group(2), month);

                    // 写入对应列
                    row.createCell(afterSurColIndex).setCellValue(month);
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
}
