package test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternTest {

    public static void main(String[] args) {
        String str = "的味道得我12000.2先休息22";

        // 使用正则表达式来提取至少 4 位的数字，不允许负数
        Pattern pattern = Pattern.compile("\\d{4,}(\\.\\d+)?");
        Matcher matcher = pattern.matcher(str);

        if (matcher.find()) {
            // 提取到的数字
            String numberStr = matcher.group();
            System.out.println("提取到的数字是: " + numberStr);
        } else {
            System.out.println("没有找到数字");
        }
    }


}
