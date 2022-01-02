package com.alex.daily_reminder.daily_reminder.service;

import com.alex.daily_reminder.daily_reminder.filter.OrganizerRecordFilter;
import com.alex.daily_reminder.daily_reminder.model.OrganizerRecordEntity;

import java.util.List;

public interface OrganizerRecordService {

    void saveOrganizerEntry(OrganizerRecordEntity organizerRecord);

    void deleteOrganizerEntry(Integer id);

    List<OrganizerRecordEntity> selectOrganizerRecords(OrganizerRecordFilter filter);

    int selectOrganizerRecordsCount(OrganizerRecordFilter filter);

    OrganizerRecordEntity selectOrganizerRecordById(Integer id);

    List<OrganizerRecordEntity> selectOrganizerRecordsForTomorrow();

    List<OrganizerRecordEntity> selectUpcomingTasks(Integer days);

}
