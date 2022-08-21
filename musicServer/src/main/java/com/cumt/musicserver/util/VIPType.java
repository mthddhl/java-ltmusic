package com.cumt.musicserver.util;

public enum VIPType {

    MONTH_VIP("月会员"),
    QUARTER_VIP("季会员"),
    YEAR_VIP("年会员"),
    MONTH_VIP_DISCOUNT("月会员(惠)"),
    QUARTER_VIP_DISCOUNT("季会员(惠)"),
    YEAR_VIP_DISCOUNT("年会员(惠)");

    private String type;

    VIPType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
