package report;

public class Report {

    // 所属文件夹名称
    private String Guid;
    private String CreateTime;
    // private String [PatientInfo]
    // 编号
    private String BianHao;
    // private String BingLiHao;
    // 检查日期
    private String CurrentDate;
    // 代码1: （匹配后面的一个列，将后面的列的值转化）
    private String codeOne;
    /**
     * 没有名字 标为0 名字后面出现“LEEP” 标为 1 普通名字 标为2
     */
    // 姓名
    private String XingMing;
    // 年龄
    private String NianLing;
    // 出生日期
    private String ChuShengRiQi;
    // 联系电话
    private String LianXiDianHua;
    // 代码2：（匹配后面的一个列，将后面的列的值转化）
    private String codeTwo;
    // 阴道镜检查征
    // 宫颈细胞学结果
    /**
     * 没有 标为0 有的话 标为1
     */
    private String ZhiZheng_JieGuoYiChang;
    // 检查日期
    // 年
    private String ZhiZheng_Nian;
    // 月
    private String ZhiZheng_Yue;
    // 经组织学确诊
    private String ZhiZheng_QueZhenWei;
    // 代码3： （匹配后面的一个列，将后面的列的值转化）
    private String codeThree;
    // 其他
    /**
     * 没有结果 标为0 出现 “H” 或者 “h” 标为1 其他的 标为3
     */
    private String ZhiZheng_QiTa;
    // 末次月经
    private String ZhiZheng_MoCiYueJing;
    // 孕
    private String ZhiZheng_YunCi;
    // 产
    private String ZhiZheng_ChanCi;
    // 避孕方式
    private String ZhiZheng_BiYunFangShi;

    // 宫颈转化区
    private String ZhuanHuaQu;

    public String getZhuanHuaQu() {
        return ZhuanHuaQu;
    }

    public void setZhuanHuaQu(String zhuanHuaQu) {
        ZhuanHuaQu = zhuanHuaQu;
    }

    /**
     * 没有0 转化区I 为1 转化区II 为2 转化区III 为3
     */
    // 宫颈转化区 1型
    private String ZhuanHuaQu_YiXing;
    // 宫颈转化区 2型
    private String ZhuanHuaQu_ErXing;
    // 宫颈转化区 3型
    private String ZhuanHuaQu_SanXing;

    // 阴道镜检查
    private String JianCha;

    public String getJianCha() {
        return JianCha;
    }

    public void setJianCha(String jianCha) {
        JianCha = jianCha;
    }

    /**
     * 阴道镜检查 没有0 满意1 不满意2
     */
    // 检查满意
    private String JianCha_MainYi;
    // 检查不满意
    private String JianCha_BuMainYi;

    // 病变位置
    private String BingBianWeiYu;

    public String getBingBianWeiYu() {
        return BingBianWeiYu;
    }

    public void setBingBianWeiYu(String bingBianWeiYu) {
        BingBianWeiYu = bingBianWeiYu;
    }

    /**
     * 病变位置，宫颈外口 没有0 右上 1 左上 2 右下 3 坐下 4
     */
    private String BingBianWeiYu_YouShang;
    private String BingBianWeiYu_ZuoShang;
    private String BingBianWeiYu_YouXia;
    private String BingBianWeiYu_ZuoXia;
    /**
     * 宫颈管内 无0 有1
     */
    private String BingBianWeiYu_GongJingGuanNei;
    /**
     * 无0 有 有啥写啥
     */
    private String TuXiang_QiTa;
    // 代码4： （匹配后面的一个列，将后面的列的值转化）
    private String codeFour;

    //阴道镜评估印象
    private String YinXiang;
    public String getYinXiang() {
        return YinXiang;
    }

    public void setYinXiang(String yinXiang) {
        YinXiang = yinXiang;
    }

    // 阴道镜评估印象
    /**
     * 无 标0 后面0
     * 1正常 标1 后面 标1
     * 2宫颈阴道部病变 标2 后面LSIL（1） HSIL(2) 可疑（3）
     * 3宫颈管内 标3 SIL(1) 腺上皮病变（2）
     * 4其他 标4 后面有啥写啥
     */
    // 1. 未见异常
    private String YinXiang_WeiJianYiChang;
    // 2. 宫颈阴道病变部位 LSIL
    private String YinXiang_LSIL;
    // 2. 宫颈阴道病变部位 HSIL
    private String YinXiang_HSIL;
    // 2. 宫颈阴道病变部位 可疑宫颈浸润癌
    private String YinXiang_KeYiGongJingJinRunAi;
    // 3. 宫颈管内病变 SIL
    private String YinXiang_SIL;
    // 3. 宫颈管内病变 腺上皮病变
    private String YinXiang_XianShangPiBingBian;
    // 4. 其他部位病变
    private String YinXiang_QiTaBuWeiBingBian;

    // 诊断意见：
    // 图片上字段的名称是 处理意见；
    private String ZhenDuanYiJian;
    // 病例属性
    private String BingLiShuXing;
    // 复诊日期
    // 年
    private String FuZhenNian;
    // 月
    private String FuZhenYue;
    // 日
    private String FuZhenRi;
    // 医生签名
    private String YiSheng;

    @Override
    public String toString() {
        return "Report [Guid=" + Guid + ", CreateTime=" + CreateTime + ", BianHao=" + BianHao + ", CurrentDate="
                + CurrentDate + ", codeOne=" + codeOne + ", XingMing=" + XingMing + ", NianLing=" + NianLing
                + ", ChuShengRiQi=" + ChuShengRiQi + ", LianXiDianHua=" + LianXiDianHua + ", codeTwo=" + codeTwo
                + ", ZhiZheng_JieGuoYiChang=" + ZhiZheng_JieGuoYiChang + ", ZhiZheng_Nian=" + ZhiZheng_Nian
                + ", ZhiZheng_Yue=" + ZhiZheng_Yue + ", ZhiZheng_QueZhenWei=" + ZhiZheng_QueZhenWei + ", codeThree="
                + codeThree + ", ZhiZheng_QiTa=" + ZhiZheng_QiTa + ", ZhiZheng_MoCiYueJing=" + ZhiZheng_MoCiYueJing
                + ", ZhiZheng_YunCi=" + ZhiZheng_YunCi + ", ZhiZheng_ChanCi=" + ZhiZheng_ChanCi
                + ", ZhiZheng_BiYunFangShi=" + ZhiZheng_BiYunFangShi + ", ZhuanHuaQu=" + ZhuanHuaQu
                + ", ZhuanHuaQu_YiXing=" + ZhuanHuaQu_YiXing + ", ZhuanHuaQu_ErXing=" + ZhuanHuaQu_ErXing
                + ", ZhuanHuaQu_SanXing=" + ZhuanHuaQu_SanXing + ", JianCha=" + JianCha + ", JianCha_MainYi="
                + JianCha_MainYi + ", JianCha_BuMainYi=" + JianCha_BuMainYi + ", BingBianWeiYu=" + BingBianWeiYu
                + ", BingBianWeiYu_YouShang=" + BingBianWeiYu_YouShang + ", BingBianWeiYu_ZuoShang="
                + BingBianWeiYu_ZuoShang + ", BingBianWeiYu_YouXia=" + BingBianWeiYu_YouXia + ", BingBianWeiYu_ZuoXia="
                + BingBianWeiYu_ZuoXia + ", BingBianWeiYu_GongJingGuanNei=" + BingBianWeiYu_GongJingGuanNei
                + ", TuXiang_QiTa=" + TuXiang_QiTa + ", codeFour=" + codeFour + ", YinXiang=" + YinXiang
                + ", YinXiang_WeiJianYiChang=" + YinXiang_WeiJianYiChang + ", YinXiang_LSIL=" + YinXiang_LSIL
                + ", YinXiang_HSIL=" + YinXiang_HSIL + ", YinXiang_KeYiGongJingJinRunAi="
                + YinXiang_KeYiGongJingJinRunAi + ", YinXiang_SIL=" + YinXiang_SIL + ", YinXiang_XianShangPiBingBian="
                + YinXiang_XianShangPiBingBian + ", YinXiang_QiTaBuWeiBingBian=" + YinXiang_QiTaBuWeiBingBian
                + ", ZhenDuanYiJian=" + ZhenDuanYiJian + ", BingLiShuXing=" + BingLiShuXing + ", FuZhenNian="
                + FuZhenNian + ", FuZhenYue=" + FuZhenYue + ", FuZhenRi=" + FuZhenRi + ", YiSheng=" + YiSheng
                + ", getZhuanHuaQu()=" + getZhuanHuaQu() + ", getJianCha()=" + getJianCha() + ", getBingBianWeiYu()="
                + getBingBianWeiYu() + ", getYinXiang()=" + getYinXiang() + ", getGuid()=" + getGuid()
                + ", getCreateTime()=" + getCreateTime() + ", getBianHao()=" + getBianHao() + ", getCurrentDate()="
                + getCurrentDate() + ", getCodeOne()=" + getCodeOne() + ", getXingMing()=" + getXingMing()
                + ", getNianLing()=" + getNianLing() + ", getChuShengRiQi()=" + getChuShengRiQi()
                + ", getLianXiDianHua()=" + getLianXiDianHua() + ", getCodeTwo()=" + getCodeTwo()
                + ", getZhiZheng_JieGuoYiChang()=" + getZhiZheng_JieGuoYiChang() + ", getZhiZheng_Nian()="
                + getZhiZheng_Nian() + ", getZhiZheng_Yue()=" + getZhiZheng_Yue() + ", getZhiZheng_QueZhenWei()="
                + getZhiZheng_QueZhenWei() + ", getCodeThree()=" + getCodeThree() + ", getZhiZheng_QiTa()="
                + getZhiZheng_QiTa() + ", getZhiZheng_MoCiYueJing()=" + getZhiZheng_MoCiYueJing()
                + ", getZhiZheng_YunCi()=" + getZhiZheng_YunCi() + ", getZhiZheng_ChanCi()=" + getZhiZheng_ChanCi()
                + ", getZhiZheng_BiYunFangShi()=" + getZhiZheng_BiYunFangShi() + ", getZhuanHuaQu_YiXing()="
                + getZhuanHuaQu_YiXing() + ", getZhuanHuaQu_ErXing()=" + getZhuanHuaQu_ErXing()
                + ", getZhuanHuaQu_SanXing()=" + getZhuanHuaQu_SanXing() + ", getJianCha_MainYi()="
                + getJianCha_MainYi() + ", getJianCha_BuMainYi()=" + getJianCha_BuMainYi()
                + ", getBingBianWeiYu_YouShang()=" + getBingBianWeiYu_YouShang() + ", getBingBianWeiYu_ZuoShang()="
                + getBingBianWeiYu_ZuoShang() + ", getBingBianWeiYu_YouXia()=" + getBingBianWeiYu_YouXia()
                + ", getBingBianWeiYu_ZuoXia()=" + getBingBianWeiYu_ZuoXia() + ", getBingBianWeiYu_GongJingGuanNei()="
                + getBingBianWeiYu_GongJingGuanNei() + ", getTuXiang_QiTa()=" + getTuXiang_QiTa() + ", getCodeFour()="
                + getCodeFour() + ", getYinXiang_WeiJianYiChang()=" + getYinXiang_WeiJianYiChang()
                + ", getYinXiang_LSIL()=" + getYinXiang_LSIL() + ", getYinXiang_HSIL()=" + getYinXiang_HSIL()
                + ", getYinXiang_KeYiGongJingJinRunAi()=" + getYinXiang_KeYiGongJingJinRunAi() + ", getYinXiang_SIL()="
                + getYinXiang_SIL() + ", getYinXiang_XianShangPiBingBian()=" + getYinXiang_XianShangPiBingBian()
                + ", getYinXiang_QiTaBuWeiBingBian()=" + getYinXiang_QiTaBuWeiBingBian() + ", getZhenDuanYiJian()="
                + getZhenDuanYiJian() + ", getBingLiShuXing()=" + getBingLiShuXing() + ", getFuZhenNian()="
                + getFuZhenNian() + ", getFuZhenYue()=" + getFuZhenYue() + ", getFuZhenRi()=" + getFuZhenRi()
                + ", getYiSheng()=" + getYiSheng() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
                + ", toString()=" + super.toString() + "]";
    }

    public String getGuid() {
        return Guid;
    }

    public void setGuid(String guid) {
        Guid = guid;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getBianHao() {
        return BianHao;
    }

    public void setBianHao(String bianHao) {
        BianHao = bianHao;
    }

    public String getCurrentDate() {
        return CurrentDate;
    }

    public void setCurrentDate(String currentDate) {
        CurrentDate = currentDate;
    }

    public String getCodeOne() {
        return codeOne;
    }

    public void setCodeOne(String codeOne) {
        this.codeOne = codeOne;
    }

    public String getXingMing() {
        return XingMing;
    }

    public void setXingMing(String xingMing) {
        XingMing = xingMing;
    }

    public String getNianLing() {
        return NianLing;
    }

    public void setNianLing(String nianLing) {
        NianLing = nianLing;
    }

    public String getChuShengRiQi() {
        return ChuShengRiQi;
    }

    public void setChuShengRiQi(String chuShengRiQi) {
        ChuShengRiQi = chuShengRiQi;
    }

    public String getLianXiDianHua() {
        return LianXiDianHua;
    }

    public void setLianXiDianHua(String lianXiDianHua) {
        LianXiDianHua = lianXiDianHua;
    }

    public String getCodeTwo() {
        return codeTwo;
    }

    public void setCodeTwo(String codeTwo) {
        this.codeTwo = codeTwo;
    }

    public String getZhiZheng_JieGuoYiChang() {
        return ZhiZheng_JieGuoYiChang;
    }

    public void setZhiZheng_JieGuoYiChang(String zhiZheng_JieGuoYiChang) {
        ZhiZheng_JieGuoYiChang = zhiZheng_JieGuoYiChang;
    }

    public String getZhiZheng_Nian() {
        return ZhiZheng_Nian;
    }

    public void setZhiZheng_Nian(String zhiZheng_Nian) {
        ZhiZheng_Nian = zhiZheng_Nian;
    }

    public String getZhiZheng_Yue() {
        return ZhiZheng_Yue;
    }

    public void setZhiZheng_Yue(String zhiZheng_Yue) {
        ZhiZheng_Yue = zhiZheng_Yue;
    }

    public String getZhiZheng_QueZhenWei() {
        return ZhiZheng_QueZhenWei;
    }

    public void setZhiZheng_QueZhenWei(String zhiZheng_QueZhenWei) {
        ZhiZheng_QueZhenWei = zhiZheng_QueZhenWei;
    }

    public String getCodeThree() {
        return codeThree;
    }

    public void setCodeThree(String codeThree) {
        this.codeThree = codeThree;
    }

    public String getZhiZheng_QiTa() {
        return ZhiZheng_QiTa;
    }

    public void setZhiZheng_QiTa(String zhiZheng_QiTa) {
        ZhiZheng_QiTa = zhiZheng_QiTa;
    }

    public String getZhiZheng_MoCiYueJing() {
        return ZhiZheng_MoCiYueJing;
    }

    public void setZhiZheng_MoCiYueJing(String zhiZheng_MoCiYueJing) {
        ZhiZheng_MoCiYueJing = zhiZheng_MoCiYueJing;
    }

    public String getZhiZheng_YunCi() {
        return ZhiZheng_YunCi;
    }

    public void setZhiZheng_YunCi(String zhiZheng_YunCi) {
        ZhiZheng_YunCi = zhiZheng_YunCi;
    }

    public String getZhiZheng_ChanCi() {
        return ZhiZheng_ChanCi;
    }

    public void setZhiZheng_ChanCi(String zhiZheng_ChanCi) {
        ZhiZheng_ChanCi = zhiZheng_ChanCi;
    }

    public String getZhiZheng_BiYunFangShi() {
        return ZhiZheng_BiYunFangShi;
    }

    public void setZhiZheng_BiYunFangShi(String zhiZheng_BiYunFangShi) {
        ZhiZheng_BiYunFangShi = zhiZheng_BiYunFangShi;
    }

    public String getZhuanHuaQu_YiXing() {
        return ZhuanHuaQu_YiXing;
    }

    public void setZhuanHuaQu_YiXing(String zhuanHuaQu_YiXing) {
        ZhuanHuaQu_YiXing = zhuanHuaQu_YiXing;
    }

    public String getZhuanHuaQu_ErXing() {
        return ZhuanHuaQu_ErXing;
    }

    public void setZhuanHuaQu_ErXing(String zhuanHuaQu_ErXing) {
        ZhuanHuaQu_ErXing = zhuanHuaQu_ErXing;
    }

    public String getZhuanHuaQu_SanXing() {
        return ZhuanHuaQu_SanXing;
    }

    public void setZhuanHuaQu_SanXing(String zhuanHuaQu_SanXing) {
        ZhuanHuaQu_SanXing = zhuanHuaQu_SanXing;
    }

    public String getJianCha_MainYi() {
        return JianCha_MainYi;
    }

    public void setJianCha_MainYi(String jianCha_MainYi) {
        JianCha_MainYi = jianCha_MainYi;
    }

    public String getJianCha_BuMainYi() {
        return JianCha_BuMainYi;
    }

    public void setJianCha_BuMainYi(String jianCha_BuMainYi) {
        JianCha_BuMainYi = jianCha_BuMainYi;
    }

    public String getBingBianWeiYu_YouShang() {
        return BingBianWeiYu_YouShang;
    }

    public void setBingBianWeiYu_YouShang(String bingBianWeiYu_YouShang) {
        BingBianWeiYu_YouShang = bingBianWeiYu_YouShang;
    }

    public String getBingBianWeiYu_ZuoShang() {
        return BingBianWeiYu_ZuoShang;
    }

    public void setBingBianWeiYu_ZuoShang(String bingBianWeiYu_ZuoShang) {
        BingBianWeiYu_ZuoShang = bingBianWeiYu_ZuoShang;
    }

    public String getBingBianWeiYu_YouXia() {
        return BingBianWeiYu_YouXia;
    }

    public void setBingBianWeiYu_YouXia(String bingBianWeiYu_YouXia) {
        BingBianWeiYu_YouXia = bingBianWeiYu_YouXia;
    }

    public String getBingBianWeiYu_ZuoXia() {
        return BingBianWeiYu_ZuoXia;
    }

    public void setBingBianWeiYu_ZuoXia(String bingBianWeiYu_ZuoXia) {
        BingBianWeiYu_ZuoXia = bingBianWeiYu_ZuoXia;
    }

    public String getBingBianWeiYu_GongJingGuanNei() {
        return BingBianWeiYu_GongJingGuanNei;
    }

    public void setBingBianWeiYu_GongJingGuanNei(String bingBianWeiYu_GongJingGuanNei) {
        BingBianWeiYu_GongJingGuanNei = bingBianWeiYu_GongJingGuanNei;
    }

    public String getTuXiang_QiTa() {
        return TuXiang_QiTa;
    }

    public void setTuXiang_QiTa(String tuXiang_QiTa) {
        TuXiang_QiTa = tuXiang_QiTa;
    }

    public String getCodeFour() {
        return codeFour;
    }

    public void setCodeFour(String codeFour) {
        this.codeFour = codeFour;
    }

    public String getYinXiang_WeiJianYiChang() {
        return YinXiang_WeiJianYiChang;
    }

    public void setYinXiang_WeiJianYiChang(String yinXiang_WeiJianYiChang) {
        YinXiang_WeiJianYiChang = yinXiang_WeiJianYiChang;
    }

    public String getYinXiang_LSIL() {
        return YinXiang_LSIL;
    }

    public void setYinXiang_LSIL(String yinXiang_LSIL) {
        YinXiang_LSIL = yinXiang_LSIL;
    }

    public String getYinXiang_HSIL() {
        return YinXiang_HSIL;
    }

    public void setYinXiang_HSIL(String yinXiang_HSIL) {
        YinXiang_HSIL = yinXiang_HSIL;
    }

    public String getYinXiang_KeYiGongJingJinRunAi() {
        return YinXiang_KeYiGongJingJinRunAi;
    }

    public void setYinXiang_KeYiGongJingJinRunAi(String yinXiang_KeYiGongJingJinRunAi) {
        YinXiang_KeYiGongJingJinRunAi = yinXiang_KeYiGongJingJinRunAi;
    }

    public String getYinXiang_SIL() {
        return YinXiang_SIL;
    }

    public void setYinXiang_SIL(String yinXiang_SIL) {
        YinXiang_SIL = yinXiang_SIL;
    }

    public String getYinXiang_XianShangPiBingBian() {
        return YinXiang_XianShangPiBingBian;
    }

    public void setYinXiang_XianShangPiBingBian(String yinXiang_XianShangPiBingBian) {
        YinXiang_XianShangPiBingBian = yinXiang_XianShangPiBingBian;
    }

    public String getYinXiang_QiTaBuWeiBingBian() {
        return YinXiang_QiTaBuWeiBingBian;
    }

    public void setYinXiang_QiTaBuWeiBingBian(String yinXiang_QiTaBuWeiBingBian) {
        YinXiang_QiTaBuWeiBingBian = yinXiang_QiTaBuWeiBingBian;
    }

    public String getZhenDuanYiJian() {
        return ZhenDuanYiJian;
    }

    public void setZhenDuanYiJian(String zhenDuanYiJian) {
        ZhenDuanYiJian = zhenDuanYiJian;
    }

    public String getBingLiShuXing() {
        return BingLiShuXing;
    }

    public void setBingLiShuXing(String bingLiShuXing) {
        BingLiShuXing = bingLiShuXing;
    }

    public String getFuZhenNian() {
        return FuZhenNian;
    }

    public void setFuZhenNian(String fuZhenNian) {
        FuZhenNian = fuZhenNian;
    }

    public String getFuZhenYue() {
        return FuZhenYue;
    }

    public void setFuZhenYue(String fuZhenYue) {
        FuZhenYue = fuZhenYue;
    }

    public String getFuZhenRi() {
        return FuZhenRi;
    }

    public void setFuZhenRi(String fuZhenRi) {
        FuZhenRi = fuZhenRi;
    }

    public String getYiSheng() {
        return YiSheng;
    }

    public void setYiSheng(String yiSheng) {
        YiSheng = yiSheng;
    }
}
