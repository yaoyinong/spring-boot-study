package com.elasticsearch.model.query;

import cn.hutool.core.text.CharSequenceUtil;
import lombok.Data;

/**
 * @author yaoyinong
 * @date 2022/7/25 20:34
 * @description
 */
@Data
public class PageBaseQuery {

    private Integer page;

    private Integer size;

    private String sortBy;

    private String direction;

    public void setPage(Integer page) {
        this.page = page != null ? page : 1;
    }

    public void setSize(Integer size) {
        this.size = size != null ? size : 10;
    }

    public void setDirection(String direction) {
        this.direction = CharSequenceUtil.isNotBlank(direction) ? direction : "asc";
    }
}
