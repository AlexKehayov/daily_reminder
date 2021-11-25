package com.alex.daily_reminder.daily_reminder.service.impl;

import com.alex.daily_reminder.daily_reminder.model.DiaryRecordEntity;
import com.alex.daily_reminder.daily_reminder.repository.DiaryRecordRepository;
import com.alex.daily_reminder.daily_reminder.service.DiaryRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DiaryRecordServiceImpl implements DiaryRecordService {

    private final DiaryRecordRepository diaryRecordRepository;

    @Override
    public void saveDiaryEntry(DiaryRecordEntity diaryRecord) {
        diaryRecordRepository.save(diaryRecord);
    }
}
