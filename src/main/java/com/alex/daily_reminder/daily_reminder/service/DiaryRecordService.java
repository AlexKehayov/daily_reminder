package com.alex.daily_reminder.daily_reminder.service;

import com.alex.daily_reminder.daily_reminder.filter.DiaryRecordFilter;
import com.alex.daily_reminder.daily_reminder.model.DiaryRecordEntity;

import java.util.List;

public interface DiaryRecordService {

    void saveDiaryEntry(DiaryRecordEntity diaryRecord);

    List<DiaryRecordEntity> selectDiaryRecords(DiaryRecordFilter filter);

    int selectDiaryRecordsCount(DiaryRecordFilter filter);

    DiaryRecordEntity selectDiaryRecordById(Integer id);

}
