package com.demo.test.auth;

public enum PayStatus {
    N("待支付"),
    P("支付中"),
    S("支付成功"),
    F("支付失败"),
    C("取消支付");
    /**
     * 支付状态对应的中文名称
     */
    private String statusName;
    /**
     * 枚举中只允许私有构造方法
     * @param statusName
     */
    private PayStatus(String statusName) {
        this.statusName = statusName;
    }
    /**
     * 获取支付状态对象对应的中文名称
     * @return
     */
    public String getStatusName() {
        return statusName;
    }
}
