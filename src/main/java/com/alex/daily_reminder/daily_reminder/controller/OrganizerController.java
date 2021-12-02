package com.alex.daily_reminder.daily_reminder.controller;

import com.alex.daily_reminder.daily_reminder.filter.OrganizerRecordFilter;
import com.alex.daily_reminder.daily_reminder.model.OrganizerRecordEntity;
import com.alex.daily_reminder.daily_reminder.model.PlaceEntity;
import com.alex.daily_reminder.daily_reminder.security.model.UserEntity;
import com.alex.daily_reminder.daily_reminder.service.OrganizerRecordService;
import com.alex.daily_reminder.daily_reminder.service.PlacesService;
import com.alex.daily_reminder.daily_reminder.util.JsonUtil;
import com.alex.daily_reminder.daily_reminder.util.SecurityUtil;
import com.alex.daily_reminder.daily_reminder.validation.OrganizerEntryValidator;
import com.alex.daily_reminder.daily_reminder.validation.ValidationError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/organizer")
public class OrganizerController {

    private final SecurityUtil securityUtil;
    private final OrganizerEntryValidator organizerEntryValidator;
    private final OrganizerRecordService organizerRecordService;
    private final PlacesService placesService;

    @GetMapping
    public String getOrganizerHome(Model model) {

        UserEntity loggedUser = securityUtil.getLoggedUser();
        if (Objects.nonNull(loggedUser)) {
            model.addAttribute("loggedUser", loggedUser);
        }

        OrganizerRecordFilter organizerRecordFilter = new OrganizerRecordFilter();
        fillData(model, organizerRecordFilter);
        model.addAttribute("organizerEntry", new OrganizerRecordEntity());
        return "organizer/organizer";
    }

    @PostMapping("/saveEntry")
    public String saveOrganizerEntry(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) Boolean isFixedDate,
            @RequestParam(required = false) String fixedDate,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) Boolean isFixedTime,
            @RequestParam(required = false) String fixedTime,
            @RequestParam(required = false) String fromTime,
            @RequestParam(required = false) String toTime,
            @RequestParam(required = false) Double geoLat,
            @RequestParam(required = false) Double geoLng,
            @RequestParam(required = false) String geoPlace,
            Model model) throws ParseException {

        OrganizerRecordEntity organizerRecordEntity = new OrganizerRecordEntity();
        organizerRecordEntity.setCreatedDate(new Date());
        organizerRecordEntity.setUserUsername(securityUtil.getLoggedUser().getUsername());
        organizerRecordEntity.setTitle(title);
        organizerRecordEntity.setContent(content);
        organizerRecordEntity.setIsFixedDate(isFixedDate);
        if (StringUtils.hasText(fixedDate))
            organizerRecordEntity.setFixedDate(new SimpleDateFormat("dd.MM.yy").parse(fixedDate));
        if (StringUtils.hasText(fromDate))
            organizerRecordEntity.setFromDate(new SimpleDateFormat("dd.MM.yy").parse(fromDate));
        if (StringUtils.hasText(toDate))
            organizerRecordEntity.setToDate(new SimpleDateFormat("dd.MM.yy").parse(toDate));
        organizerRecordEntity.setIsFixedTime(isFixedTime);
        if (StringUtils.hasText(fixedTime))
            organizerRecordEntity.setFixedTime(LocalTime.parse(fixedTime));
        if (StringUtils.hasText(fromTime))
            organizerRecordEntity.setFromTime(LocalTime.parse(fromTime));
        if (StringUtils.hasText(toTime))
            organizerRecordEntity.setToTime(LocalTime.parse(toTime));
        if(Objects.nonNull(geoLat))
            organizerRecordEntity.setGeoLat(geoLat);
        if(Objects.nonNull(geoLng))
            organizerRecordEntity.setGeoLng(geoLng);
        if (StringUtils.hasText(geoPlace))
            organizerRecordEntity.setGeoPlace(geoPlace);

        List<ValidationError> errors = organizerEntryValidator.validate(organizerRecordEntity);
        if (!CollectionUtils.isEmpty(errors)) {
            model.addAttribute("errors", errors);
            model.addAttribute("organizerEntry", organizerRecordEntity);
        } else {
            try {
                organizerRecordService.saveOrganizerEntry(organizerRecordEntity);
                model.addAttribute("organizerEntry", new OrganizerRecordEntity());
            } catch (Exception e) {
                model.addAttribute("organizerEntry", organizerRecordEntity);
                model.addAttribute("message", "An unexpected error occurred while saving the entry");
            }
        }

        return "organizer/fragments/organizerContent :: organizerContent";
    }

    @PostMapping("/deleteEntry")
    @ResponseBody
    public void deleteDiaryEntry(
            @RequestParam Integer id) {
        organizerRecordService.deleteOrganizerEntry(id);
    }

    @PostMapping("/searchOrganizer")
    public String searchDiary(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) Integer currentPage,
            Model model) throws ParseException {

        OrganizerRecordFilter organizerRecordFilter = new OrganizerRecordFilter();

        if (StringUtils.hasText(title))
            organizerRecordFilter.setTitle(title);

        if (StringUtils.hasText(content))
            organizerRecordFilter.setContent(content);

        if (StringUtils.hasText(fromDate))
            organizerRecordFilter.setFromDate(new SimpleDateFormat("dd.MM.yy").parse(fromDate));

        if (StringUtils.hasText(toDate))
            organizerRecordFilter.setToDate(new SimpleDateFormat("dd.MM.yy").parse(toDate));

        if (Objects.nonNull(currentPage))
            organizerRecordFilter.setPage(currentPage);

        fillData(model, organizerRecordFilter);

        return "organizer/fragments/organizerTableContent :: table";
    }

    @PostMapping("/initContentModal")
    public String initContentModal(
            @RequestParam Integer id,
            Model model) {
        model.addAttribute("organizerEntry", organizerRecordService.selectOrganizerRecordById(id));
        return "organizer/modals/viewContent :: view-content";
    }

    @PostMapping("/initGeoLocationModal")
    public String initGeoLocationModal(
            @RequestParam Integer id,
            Model model) {
        model.addAttribute("organizerEntry", organizerRecordService.selectOrganizerRecordById(id));
        return "organizer/modals/viewGeoLocation :: view-geo-location";
    }

    @PostMapping("/locationsAutoComplete")
    public String locationsAutoComplete(@RequestParam String name, Model model) {
        List<PlaceEntity> placeEntities = placesService.autoCompletePlaces(name);
        model.addAttribute("items", placeEntities);
        return "organizer/fragments/locationsDropdown :: data";
    }

    private void fillData(Model model, OrganizerRecordFilter filter) {
        List<OrganizerRecordEntity> organizerRecords = organizerRecordService.selectOrganizerRecords(filter);
        model.addAttribute("filter", filter);
        model.addAttribute("organizerRecords", organizerRecords);
        model.addAttribute("total", organizerRecordService.selectOrganizerRecordsCount(filter));
    }

}
