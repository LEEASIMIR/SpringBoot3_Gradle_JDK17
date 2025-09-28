package com.springboot.template.common.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageDto {
    private long pageNumber = 0;
    private int pageSize = 10;
    private List<PageSortDto> sorts;

    /**
     * @param pageSize max 500, 너무 많은양 리턴 방지
     * @author 이봉용
     * @date 25. 9. 27.
     */
    public void setPageSize(int pageSize) {
        this.pageSize = Math.min(pageSize, 500);
    }
}
