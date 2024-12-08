package report.constants;

/**
 * 定义HPV类型：
 * 6，11，16、18，26，31、33、35、39、40、42、43、45、51、52、53，54、56、58、59、61、66、68、70、72、73、81、82    E6/E7
 */
public enum HpvTypes {

    HPV_6("6", ""),
    HPV_11("11", ""),
    HPV_16("16", ""),
    HPV_18("18", ""),
    HPV_26("26", ""),
    HPV_31("32", ""),
    HPV_33("33", ""),
    HPV_35("35", ""),
    HPV_39("39", ""),
    HPV_40("40", ""),
    HPV_42("42", ""),
    HPV_43("43", ""),
    HPV_51("51", ""),
    HPV_52("52", ""),
    HPV_53("53", ""),
    HPV_54("54", ""),
    HPV_56("56", ""),
    HPV_58("58", ""),
    HPV_61("61", ""),
    HPV_66("66", ""),
    HPV_68("68", ""),
    HPV_70("70", ""),
    HPV_72("72", ""),
    HPV_73("73", ""),
    HPV_81("81", ""),
    HPV_82("82", ""),
    HPV_E6E7("E6/E7", "");

    // 定义属性来接收状态码和描述
    private final String code;
    private final String description;

    // 带参数的构造方法
    HpvTypes(String code, String description) {
        this.code = code;
        this.description = description;
    }

    // 获取状态码的方法
    public String getCode() {
        return code;
    }

    // 获取描述信息的方法
    public String getDescription() {
        return description;
    }

}
