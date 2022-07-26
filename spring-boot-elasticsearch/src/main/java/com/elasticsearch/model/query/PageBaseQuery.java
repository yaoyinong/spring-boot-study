package com.elasticsearch.model.query;

import lombok.Data;

/**
 * @author yaoyinong
 * @date 2022/7/25 20:34
 * @description
 */
@Data
public class PageBaseQuery {

    /**
     * 页码
     */
    private Integer page;

    /**
     * 每页数量
     */
    private Integer size;

    /**
     * 排序字段
     * 默认ID
     */
    private String sortBy;

    /**
     * 排序规则
     * 默认ASC
     */
    private String direction;

    public void setPage(Integer page) {
        this.page = page != null ? page : 1;
    }

    public void setSize(Integer size) {
        this.size = size != null ? size : 10;
    }

}
