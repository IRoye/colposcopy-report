package report;


import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
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

/**
 * 【2】、设置HPV类型和HPV的类型值，这是每一行都需要设置，除非没有这一列
 * HPV类型
 * 有16 12 +字眼，Y列写阳性的型号，CT值就是对应那个值，依次类推
 * 6，11，16、18，26，31、33、35、39、40、42、43、45、51、52、53，54、56、58、59、61、66、68、70、72、73、81、82    E6/E7
 */
public class IdentifyHpvTypes {


    public static class Type {
        private String type;
        private String value;

        // 构造函数，初始化 value 为默认值
        public Type() {
            this.value = "0"; // 默认值
        }

        // 获取 HPV 类型的数字
        public String getType() {
            return type;
        }

        // 获取 HPV 类型的描述
        public String getValue() {
            return value;
        }

        // 设置 HPV 类型的数字
        public void setType(String type) {
            this.type = type;
        }

        // 设置 HPV 值
        public void setValue(String value) {
            this.value = value;
        }
    }

    public static void main(String[] args) {

        String inputFile = "/Users/royeyu/Downloads/grouped_patients_data.xlsx";  // 替换为实际文件路径
        String outputFile = "/Users/royeyu/Downloads/grouped_patients_hpv_data.xlsx";


        try {
            Workbook book = new XSSFWorkbook(inputFile);
            Sheet sheet = book.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();

            Row headerRow = rowIterator.next();
            int otherColIndex = getColumnIndex(headerRow, "其他");

            // 【1】、额外增加HPV类型列和类型值列
            int hpvTypesStartIndex = headerRow.getPhysicalNumberOfCells();
            for (HpvTypes hpv : HpvTypes.values()) {
                // HPV值列
                headerRow.createCell(hpvTypesStartIndex++).setCellValue("HPV_" + hpv.getCode() + "CT值");
            }

            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);

                // 如果为空列，则跳过
                if (row.getCell(otherColIndex) == null || row.getCell(otherColIndex).getCellType() == CellType.BLANK) {
                    continue;
                }

                // 获取其他列的值
                String otherStr = getCellValue(row, otherColIndex);

                if (otherStr.contains("HC2_HPV")) {
                    continue;
                }

                /**
                 * 分组，目的为了更好的取值，每组下可能有Ct=33.2，这就作为值，如果找不到这种格式，那么值就是0
                 * 本院Cobas 12HR-HPV+，Ct=33.2; 外院; HPV6.68+
                 *
                 * 格式1：
                 * 1、Cobas HPV16+，Ct=30.9
                 * 2、高危HPV:33+
                 * 3、
                 */
                String[] hpvUnit = null;
                if (otherStr.contains(";")) {
                    hpvUnit = otherStr.split(";");
                } else {
                    hpvUnit = otherStr.split("；");
                }

                List<Type> hpvs = new ArrayList<>();

                // 处理，比如处理整个Cobas 12HR-HPV+，Ct=33.2; 外院; HPV6.68+其中的：Cobas 12HR-HPV+，Ct=33.2
                for (int j = 0; j < hpvUnit.length; j++) {
                    String currentTypes = hpvUnit[j];
                    Type type = new Type();

                    /**
                     * 获取HPV类型的情况有很多种，目前格式：
                     * 1、类型1：Cobas HPV16+，Ct=30.9; 这是比较正常的类型
                     */
                    Pattern hpvPattern = Pattern.compile("HPV(\\d+)\\+|(\\d+)HR-HPV");

                    // 类型2：外院; HPV16：+
                    Pattern hpvColonPattern = Pattern.compile("HPV(\\d+)：\\+");

                    // 类型3：外院; HPV16:+
                    Pattern hpvColon2Pattern = Pattern.compile("HPV(\\d+):\\+");

                    // 类型4：HPV52+、42+
                    Pattern hpvmultipePattern = Pattern.compile("^HPV(\\d+)\\D+(\\d+)\\D+");

                    // 类型5：HPV：52+
                    Pattern Pattern5 = Pattern.compile("HPV：(\\d+)\\+");

                    // 类型6：HPV16，56+
                    Pattern Pattern6 = Pattern.compile("HPV:(\\d+)");

                    // 类型7：HPV42/59+
                    Pattern Pattern7 = Pattern.compile("HPV(\\d+)(?:/(\\d+))(?:/(\\d+))?\\+");

                    // 类型8：12HR_HPV+ (\d+)HR_HPV\+
                    Pattern Pattern8 = Pattern.compile("(\\d+)HR_HPV\\+");

                    int count = 0;
                    if (hpvPattern.matcher(currentTypes).find()) {
                        count ++;
                        System.out.printf("类型1：%s \n", currentTypes);
                        matchAndPrint(hpvPattern, currentTypes, type);
                    } else if (hpvColonPattern.matcher(currentTypes).find()) {
                        count ++;
                        System.out.printf("类型2：%s \n", currentTypes);
                        matchAndPrint(hpvColonPattern, currentTypes, type);
                    } else if (hpvColon2Pattern.matcher(currentTypes).find()) {
                        count ++;
                        System.out.printf("类型3：%s \n", currentTypes);
                        matchAndPrint(hpvColon2Pattern, currentTypes, type);
                    } else if (hpvmultipePattern.matcher(currentTypes).find()) {
                        count ++;
                        System.out.printf("类型4：%s \n", currentTypes);
                        matchAndPrint(hpvmultipePattern, currentTypes, type);
                    } else if (Pattern5.matcher(currentTypes).find()) {
                        count ++;
                        System.out.printf("类型5：%s \n", currentTypes);
                        matchAndPrint(Pattern5, currentTypes, type);
                    } else if (Pattern6.matcher(currentTypes).find()) {
                        count ++;
                        System.out.printf("类型6：%s \n", currentTypes);
                        Matcher ctMatcher = Pattern6.matcher(currentTypes);
                        while (ctMatcher.find()) {
                            String value = ctMatcher.group(1);  // 获取匹配到的数字并添加到列表
                            setType(value, currentTypes, type);
                        }
                    } else if (Pattern7.matcher(currentTypes).find()) {
                        count ++;
                        System.out.printf("类型7：%s \n", currentTypes);
                        matchAndPrint(Pattern7, currentTypes, type);
                    }
                    else if (Pattern8.matcher(currentTypes).find()) {
                        count ++;
                        System.out.printf("类型8：%s \n", currentTypes);
                        matchAndPrint(Pattern8, currentTypes, type);
                    }

                    if (count == 0) {
                        System.out.printf("文本：%s 未匹配 \n", currentTypes);
                    }

                    hpvs.add(type);
                }

                // 将值赋值到列
            }

            FileOutputStream fos = new FileOutputStream(new File(outputFile));
            book.write(fos);
            fos.close();

            System.out.println("HPV类型处理完成，结果已保存到：" + outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void matchAndPrint(Pattern pattern, String input, Type type) {
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            // 遍历所有捕获的分组
            for (int i = 1; i <= matcher.groupCount(); i++) {
                // 获取每个捕获分组的值
                String groupValue = matcher.group(i);
                // 如果分组有匹配到值，输出它
                setType(groupValue, input, type);
            }
        }
    }

    public static void setType(String groupValue, String input, Type type) {
        if (groupValue != null) {
            type.setType(groupValue);
            Pattern ctPattern = Pattern.compile("(?i)Ct=(\\d+(\\.\\d+)?)");
            Matcher ctMatcher = ctPattern.matcher(input);
            if (ctMatcher.find()) {
                String ctValue = ctMatcher.group(1); // 获取CT值
                type.setValue(ctValue);
            }
            System.out.printf("匹配：文本：%s, 匹配key：%s, 匹配value %s \n", input, type.getType(), type.getValue());
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
}
