package com.alex.daily_reminder.daily_reminder.filter;

public interface Sortable {
    String ASC_ORDER = "ASC";
    String DESC_ORDER = "DESC";

    String getSortOrder();

    String getSortColumn();
}
