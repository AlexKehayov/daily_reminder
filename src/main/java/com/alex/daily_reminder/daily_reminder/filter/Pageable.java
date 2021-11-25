package com.alex.daily_reminder.daily_reminder.filter;

public interface Pageable {
    Integer DEFAULT_PAGE = 1;
    Integer DEFAULT_PAGE_SIZE = 5;
    Integer PAGE_SIZE_100 = 100;

    Integer getPage();

    Integer getPageSize();

}
