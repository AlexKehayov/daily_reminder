package com.alex.daily_reminder.daily_reminder.controller;

import com.alex.daily_reminder.daily_reminder.filter.DiaryRecordFilter;
import com.alex.daily_reminder.daily_reminder.model.DiaryRecordEntity;
import com.alex.daily_reminder.daily_reminder.security.model.UserEntity;
import com.alex.daily_reminder.daily_reminder.service.DiaryRecordService;
import com.alex.daily_reminder.daily_reminder.util.JsonUtil;
import com.alex.daily_reminder.daily_reminder.util.SecurityUtil;
import com.alex.daily_reminder.daily_reminder.validation.DiaryEntryValidator;
import com.alex.daily_reminder.daily_reminder.validation.ValidationError;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
@RequestMapping("/diary")
public class DiaryController {

    private final SecurityUtil securityUtil;
    private final DiaryEntryValidator diaryEntryValidator;
    private final DiaryRecordService diaryRecordService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('user:read_diary', 'user:write_diary')")
    public String getDiaryHome(Model model) {

        UserEntity loggedUser = securityUtil.getLoggedUser();
        if (Objects.nonNull(loggedUser)) {
            model.addAttribute("loggedUser", loggedUser);
        }

        DiaryRecordFilter diaryRecordFilter = new DiaryRecordFilter();
        fillData(model, diaryRecordFilter);
        model.addAttribute("diaryEntry", new DiaryRecordEntity());
        return "diary/diary";
    }

    @PostMapping("/saveEntry")
    @PreAuthorize("hasAuthority('user:write_diary')")
    public String saveDiaryEntry(
            @RequestParam String content,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lng,
            @RequestParam(required = false) String place,
            Model model) {

        DiaryRecordEntity diaryRecordEntity = new DiaryRecordEntity();
        diaryRecordEntity.setContent(content);
        diaryRecordEntity.setCreatedDate(new Date());
        diaryRecordEntity.setGeoLat(lat);
        diaryRecordEntity.setGeoLng(lng);
        diaryRecordEntity.setGeoPlace(place);
        diaryRecordEntity.setUserUsername(securityUtil.getLoggedUser().getUsername());

        List<ValidationError> errors = diaryEntryValidator.validate(diaryRecordEntity);
        if (!CollectionUtils.isEmpty(errors)) {
            model.addAttribute("errors", errors);
            model.addAttribute("diaryEntry", diaryRecordEntity);
        } else {
            try {
                diaryRecordService.saveDiaryEntry(diaryRecordEntity);
            } catch (Exception e) {
                model.addAttribute("message", "An unexpected error occurred while saving the entry");
            }
        }

        return "diary/fragments/diaryContent :: diaryContent";
    }

    @PostMapping("/deleteEntry")
    @PreAuthorize("hasAuthority('user:write_diary')")
    @ResponseBody
    public void deleteDiaryEntry(
            @RequestParam Integer id) {
       diaryRecordService.deleteDiaryEntry(id);
    }

    @PostMapping("/searchDiary")
    @PreAuthorize("hasAuthority('user:read_diary')")
    public String searchDiary(
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo,
            @RequestParam(required = false) Integer currentPage,
            Model model) throws ParseException {

        DiaryRecordFilter diaryRecordFilter = new DiaryRecordFilter();

        if (StringUtils.hasText(content))
            diaryRecordFilter.setContent(content);

        if (StringUtils.hasText(dateFrom))
            diaryRecordFilter.setCreateDateFrom(new SimpleDateFormat("dd.MM.yy").parse(dateFrom));

        if (StringUtils.hasText(dateTo))
            diaryRecordFilter.setCreateDateTo(new SimpleDateFormat("dd.MM.yy").parse(dateTo));

        if (Objects.nonNull(currentPage))
            diaryRecordFilter.setPage(currentPage);

        fillData(model, diaryRecordFilter);

        return "diary/fragments/diaryTableContent :: table";
    }

    @PostMapping("/initContentModal")
    @PreAuthorize("hasAuthority('user:read_diary')")
    public String initContentModal(
            @RequestParam String content,
            @RequestParam String createdDate,
            Model model) throws ParseException {
        model.addAttribute("content", content);
        model.addAttribute("createdDate", new SimpleDateFormat("yy-MM-dd HH:mm:ss.S").parse(createdDate));
        return "diary/modals/viewContent :: view-content";
    }

    @PostMapping("/initGeoLocationModal")
    @PreAuthorize("hasAuthority('user:read_diary')")
    public String initGeoLocationModal(
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam String createdDate,
            Model model) throws ParseException {
        model.addAttribute("geoLat", lat);
        model.addAttribute("geoLng", lng);
        model.addAttribute("createdDate", new SimpleDateFormat("yy-MM-dd HH:mm:ss.S").parse(createdDate));
        return "diary/modals/viewGeoLocation :: view-geo-location";
    }

    private void fillData(Model model, DiaryRecordFilter filter) {
        List<DiaryRecordEntity> diaryRecords = diaryRecordService.selectDiaryRecords(filter);
        model.addAttribute("filter", filter);
        model.addAttribute("diaryRecords", diaryRecords);
        model.addAttribute("diaryRecordsJson", JsonUtil.createJsonList(diaryRecords));
        model.addAttribute("total", diaryRecordService.selectDiaryRecordsCount(filter));
    }

}
