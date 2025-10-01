package com.springboot.template.common.model.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ResponseDto<T> extends PageDto {
    private final List<T> content;
    private final int totalPages;
    private final long total;
    private final boolean first;
    private final boolean last;

    //필요한 것 만
    public ResponseDto(long pageNumber, int pageSize, Long total, List<PageSortDto> sortDto, List<T> content) {
        this.content = content;
        this.total = total == null ? 0 : total;
        this.totalPages = (int) Math.ceil((double) this.total / pageSize);
        this.setPageNumber(pageNumber);
        this.setPageSize(pageSize);
        this.first = this.getPageNumber() == 0;
        this.last = this.totalPages == this.getPageNumber()-1;
        this.setSorts(sortDto);
    }
}
