package com.alex.daily_reminder.daily_reminder.controller;

import com.alex.daily_reminder.daily_reminder.model.DiaryRecordEntity;
import com.alex.daily_reminder.daily_reminder.security.model.UserDTO;
import com.alex.daily_reminder.daily_reminder.security.model.UserEntity;
import com.alex.daily_reminder.daily_reminder.service.DiaryRecordService;
import com.alex.daily_reminder.daily_reminder.util.SecurityUtil;
import com.alex.daily_reminder.daily_reminder.validation.DiaryEntryValidator;
import com.alex.daily_reminder.daily_reminder.validation.ValidationError;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {

    private final SecurityUtil securityUtil;
    private final DiaryEntryValidator diaryEntryValidator;
    private final DiaryRecordService diaryRecordService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public String getDiaryHome(Model model) {

        UserEntity loggedUser = securityUtil.getLoggedUser();
        if (Objects.nonNull(loggedUser)) {
            model.addAttribute("loggedUser", loggedUser);
        }
        return "diary/diary";
    }

    @PostMapping("/saveEntry")
    public String saveDiaryEntry(
            @RequestParam String content,
            @RequestParam(required = false) Float lat,
            @RequestParam(required = false) Float lng,
            Model model) {

        DiaryRecordEntity diaryRecordEntity = new DiaryRecordEntity();
        diaryRecordEntity.setContent(content);
        diaryRecordEntity.setCreatedDate(new Date());
        diaryRecordEntity.setGeoLat(lat);
        diaryRecordEntity.setGeoLng(lng);
        diaryRecordEntity.setUserUsername(securityUtil.getLoggedUser().getUsername());

        List<ValidationError> errors = diaryEntryValidator.validate(diaryRecordEntity);
        if(!CollectionUtils.isEmpty(errors)){
            model.addAttribute("errors", errors);
            model.addAttribute("content", content);
        } else {
            try {
                diaryRecordService.saveDiaryEntry(diaryRecordEntity);
            } catch (Exception e) {
                model.addAttribute("message", "An unexpected error occurred while saving the entry");
            }
        }

        return "diary/fragments/diaryContent :: diaryContent";
    }

}
