package com.java2e.martin.extension.ncnb.model;

/**
 * @author 狮少
 * @version 1.0
 * @date 2021/4/26
 * @describtion UpOrLow
 * @since 1.0
 */
public enum UpOrLow {
    UPPERCASE("UPPERCASE"),
    LOWCASE("LOWCASE"),
    DEFAULT("DEFAULT");

    private String description;

    private UpOrLow(String desc) {
        this.description = desc;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
