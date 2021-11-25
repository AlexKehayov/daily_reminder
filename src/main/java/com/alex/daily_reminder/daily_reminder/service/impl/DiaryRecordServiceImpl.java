package com.alex.daily_reminder.daily_reminder.service.impl;

import com.alex.daily_reminder.daily_reminder.filter.DiaryRecordFilter;
import com.alex.daily_reminder.daily_reminder.model.DiaryRecordEntity;
import com.alex.daily_reminder.daily_reminder.repository.DiaryRecordRepository;
import com.alex.daily_reminder.daily_reminder.repository.DiaryRecordRepositoryCustom;
import com.alex.daily_reminder.daily_reminder.service.DiaryRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DiaryRecordServiceImpl implements DiaryRecordService {

    private final DiaryRecordRepository diaryRecordRepository;
    private final DiaryRecordRepositoryCustom diaryRecordRepositoryCustom;

    @Override
    public void saveDiaryEntry(DiaryRecordEntity diaryRecord) {
        diaryRecordRepository.save(diaryRecord);
    }

    @Override
    public List<DiaryRecordEntity> selectDiaryRecords(DiaryRecordFilter filter) {
        List<DiaryRecordEntity> result = diaryRecordRepositoryCustom.selectConfigParams(filter);
        return result;
    }

    @Override
    public int selectDiaryRecordsCount(DiaryRecordFilter filter) {
        return diaryRecordRepositoryCustom.selectConfigParamsCount(filter);
    }

    @Override
    public DiaryRecordEntity selectDiaryRecordById(Integer id) {
        return diaryRecordRepository.getById(id);
    }
}
