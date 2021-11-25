package com.alex.daily_reminder.daily_reminder.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryRecordFilter implements Sortable, Pageable {
    private String sortOrder = this.DESC_ORDER;
    private String sortColumn = DiaryRecordSorterUtils.CREATED_DATE;
    private Integer page = this.DEFAULT_PAGE;
    private Integer pageSize = this.DEFAULT_PAGE_SIZE;
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date createDateFrom;
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date createDateTo;
    private String content;
}
