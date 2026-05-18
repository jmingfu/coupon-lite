package com.coupon.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageParam {
    @ApiModelProperty("分页参数-页码")
    private Integer pageNum = 1;
    @ApiModelProperty("分页参数-页面大小")
    private Integer pageSize = 0;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
