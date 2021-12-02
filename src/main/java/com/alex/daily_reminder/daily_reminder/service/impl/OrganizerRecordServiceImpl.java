package com.alex.daily_reminder.daily_reminder.service.impl;

import com.alex.daily_reminder.daily_reminder.filter.OrganizerRecordFilter;
import com.alex.daily_reminder.daily_reminder.model.OrganizerRecordEntity;
import com.alex.daily_reminder.daily_reminder.repository.OrganizerRecordRepository;
import com.alex.daily_reminder.daily_reminder.repository.OrganizerRecordRepositoryCustom;
import com.alex.daily_reminder.daily_reminder.service.OrganizerRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrganizerRecordServiceImpl implements OrganizerRecordService {

    private final OrganizerRecordRepository organizerRecordRepository;
    private final OrganizerRecordRepositoryCustom organizerRecordRepositoryCustom;

    @Override
    public void saveOrganizerEntry(OrganizerRecordEntity diaryRecord) {
        organizerRecordRepository.save(diaryRecord);
    }

    @Override
    public void deleteOrganizerEntry(Integer id) {
        organizerRecordRepository.deleteById(id);
    }

    @Override
    public List<OrganizerRecordEntity> selectOrganizerRecords(OrganizerRecordFilter filter) {
        List<OrganizerRecordEntity> result = organizerRecordRepositoryCustom.selectOrganizerEntries(filter);
        return result;
    }

    @Override
    public int selectOrganizerRecordsCount(OrganizerRecordFilter filter) {
        return organizerRecordRepositoryCustom.selectOrganizerEntriesCount(filter);
    }

    @Override
    public OrganizerRecordEntity selectOrganizerRecordById(Integer id) {
        return organizerRecordRepository.getById(id);
    }
}
