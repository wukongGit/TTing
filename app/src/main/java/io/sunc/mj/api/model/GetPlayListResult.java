package io.sunc.mj.api.model;

/**
 * Description:
 * Date: 2017-04-12 15:02
 * Author: suncheng
 */
public class GetPlayListResult {
    private String code;
    private boolean more;
    private int total;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isMore() {
        return more;
    }

    public void setMore(boolean more) {
        this.more = more;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}

