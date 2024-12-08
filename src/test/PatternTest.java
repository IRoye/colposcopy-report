package test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternTest {

    public static void main(String[] args) {
        String input = "xxxxCIN2 CIN3xxx HSIL";  // 示例输入

        // 正则表达式，匹配 CIN1、CIN2、CIN3、CIN2-3、LSIL、HSIL
        Pattern pattern = Pattern.compile("(CIN1|CIN2|CIN2-3?|CIN3|LSIL|HSIL)");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            // 遍历所有捕获的分组
            for (int i = 1; i <= matcher.groupCount(); i++) {
                // 获取每个捕获分组的值
                String groupValue = matcher.group(i);
                System.out.println(groupValue);
            }
        }
    }


}
