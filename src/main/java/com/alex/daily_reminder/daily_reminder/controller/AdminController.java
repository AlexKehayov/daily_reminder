package com.alex.daily_reminder.daily_reminder.controller;

import com.alex.daily_reminder.daily_reminder.security.model.UserEntity;
import com.alex.daily_reminder.daily_reminder.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/admin")
public class AdminController {

    private final SecurityUtil securityUtil;

    @GetMapping
    public String getAdminHome(Model model) {

        UserEntity loggedUser = securityUtil.getLoggedUser();
        if (Objects.nonNull(loggedUser)) {
            model.addAttribute("loggedUser", loggedUser);
        }

//        DiaryRecordFilter diaryRecordFilter = new DiaryRecordFilter();
//        fillData(model, diaryRecordFilter);
        return "admin/admin";
    }

//    @PostMapping("/saveEntry")
//    public String saveDiaryEntry(
//            @RequestParam String content,
//            @RequestParam(required = false) Double lat,
//            @RequestParam(required = false) Double lng,
//            Model model) {
//
//        DiaryRecordEntity diaryRecordEntity = new DiaryRecordEntity();
//        diaryRecordEntity.setContent(content);
//        diaryRecordEntity.setCreatedDate(new Date());
//        diaryRecordEntity.setGeoLat(lat);
//        diaryRecordEntity.setGeoLng(lng);
//        diaryRecordEntity.setUserUsername(securityUtil.getLoggedUser().getUsername());
//
//        List<ValidationError> errors = diaryEntryValidator.validate(diaryRecordEntity);
//        if (!CollectionUtils.isEmpty(errors)) {
//            model.addAttribute("errors", errors);
//            model.addAttribute("content", content);
//        } else {
//            try {
//                diaryRecordService.saveDiaryEntry(diaryRecordEntity);
//            } catch (Exception e) {
//                model.addAttribute("message", "An unexpected error occurred while saving the entry");
//            }
//        }
//
//        return "diary/fragments/diaryContent :: diaryContent";
//    }
//
//    @PostMapping("/deleteEntry")
//    @PreAuthorize("hasAuthority('user:write_diary')")
//    @ResponseBody
//    public void deleteDiaryEntry(
//            @RequestParam Integer id) {
//       diaryRecordService.deleteDiaryEntry(id);
//    }
//
//    @PostMapping("/searchDiary")
//    @PreAuthorize("hasAuthority('user:read_diary')")
//    public String searchDiary(
//            @RequestParam(required = false) String content,
//            @RequestParam(required = false) String dateFrom,
//            @RequestParam(required = false) String dateTo,
//            @RequestParam(required = false) Integer currentPage,
//            Model model) throws ParseException {
//
//        DiaryRecordFilter diaryRecordFilter = new DiaryRecordFilter();
//
//        if (StringUtils.hasText(content))
//            diaryRecordFilter.setContent(content);
//
//        if (StringUtils.hasText(dateFrom))
//            diaryRecordFilter.setCreateDateFrom(new SimpleDateFormat("dd.MM.yy").parse(dateFrom));
//
//        if (StringUtils.hasText(dateTo))
//            diaryRecordFilter.setCreateDateTo(new SimpleDateFormat("dd.MM.yy").parse(dateTo));
//
//        if (Objects.nonNull(currentPage))
//            diaryRecordFilter.setPage(currentPage);
//
//        fillData(model, diaryRecordFilter);
//
//        return "diary/fragments/diaryTableContent :: table";
//    }
//
//    @PostMapping("/initContentModal")
//    @PreAuthorize("hasAuthority('user:read_diary')")
//    public String initContentModal(
//            @RequestParam String content,
//            @RequestParam String createdDate,
//            Model model) throws ParseException {
//        model.addAttribute("content", content);
//        model.addAttribute("createdDate", new SimpleDateFormat("yy-MM-dd HH:mm:ss.S").parse(createdDate));
//        return "diary/modals/viewContent :: view-content";
//    }
//
//    private void fillData(Model model, DiaryRecordFilter filter) {
//        List<DiaryRecordEntity> diaryRecords = diaryRecordService.selectDiaryRecords(filter);
//        model.addAttribute("filter", filter);
//        model.addAttribute("diaryRecords", diaryRecords);
//        model.addAttribute("total", diaryRecordService.selectDiaryRecordsCount(filter));
//    }

}
