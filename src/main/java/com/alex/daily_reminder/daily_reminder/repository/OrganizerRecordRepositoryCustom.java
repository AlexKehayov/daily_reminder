package com.alex.daily_reminder.daily_reminder.repository;

import com.alex.daily_reminder.daily_reminder.filter.*;
import com.alex.daily_reminder.daily_reminder.model.DiaryRecordEntity;
import com.alex.daily_reminder.daily_reminder.model.OrganizerRecordEntity;
import com.alex.daily_reminder.daily_reminder.model.PlaceEntity;
import com.alex.daily_reminder.daily_reminder.util.DateUtil;
import com.alex.daily_reminder.daily_reminder.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class OrganizerRecordRepositoryCustom {


    @Autowired
    private EntityManager em;

    @Autowired
    private SecurityUtil securityUtil;

    public List<OrganizerRecordEntity> selectOrganizerEntries(OrganizerRecordFilter filter) {
        String buildQuery = buildQuery(filter, false);
        Query query = em.createNativeQuery(buildQuery, OrganizerRecordEntity.class);
        addQueryParams(filter, query);
        query.setMaxResults(filter.getPageSize());
        query.setFirstResult((filter.getPage() - 1) * filter.getPageSize());
        return query.getResultList();
    }

    public int selectOrganizerEntriesCount(OrganizerRecordFilter filter) {
        String buildQuery = buildQuery(filter, true);
        Query query = em.createNativeQuery(buildQuery);
        addQueryParams(filter, query);
        Number result = (Number) query.getSingleResult();
        return result.intValue();
    }

    private String buildQuery(OrganizerRecordFilter filter, boolean isCount) {
        StringBuilder queryBuilder = new StringBuilder("SELECT ");
        queryBuilder.append(isCount ? " COUNT(*) " : " * ");
        queryBuilder.append(" FROM daily_reminder.organizer_record d ");
        queryBuilder.append(" WHERE 1=1 AND d.user_username = :username ");

        if (StringUtils.hasText(filter.getTitle())) {
            queryBuilder.append(" AND LOWER(d.title) like LOWER(:title) ");
        }
        if (StringUtils.hasText(filter.getContent())) {
            queryBuilder.append(" AND LOWER(d.content) like LOWER(:content) ");
        }
        if (Objects.nonNull(filter.getFromDate())) {
            queryBuilder.append(" AND (d.fixed_date >= :fromDate OR d.from_date >= :fromDate) ");
        }
        if (Objects.nonNull(filter.getToDate())) {
            queryBuilder.append(" AND (d.fixed_date <= :toDate OR d.to_date <= :toDate) ");
        }

        if (!isCount) {
            String sortColumn = filter.getSortColumn();
            String sortOrder = filter.getSortOrder();
            if (!(Sortable.ASC_ORDER.equalsIgnoreCase(sortOrder) || Sortable.DESC_ORDER.equalsIgnoreCase(sortOrder))) {
                sortOrder = Sortable.ASC_ORDER;
            }
            String[] columns = OrganizerRecordSorterUtils.sorterColumnMap().get(sortColumn).split(",");
            String order = String.join(" " + sortOrder + " , ", columns) + " " + sortOrder;
            queryBuilder.append(" ORDER BY ").append(order);
        }
        return queryBuilder.toString();
    }

    private void addQueryParams(OrganizerRecordFilter filter, Query query) {

        query.setParameter("username", securityUtil.getLoggedUser().getUsername());

        if (StringUtils.hasText(filter.getTitle())) {
            query.setParameter("title", "%" + filter.getTitle() + "%");
        }
        if (StringUtils.hasText(filter.getContent())) {
            query.setParameter("content", "%" + filter.getContent() + "%");
        }
        Date fromDate = filter.getFromDate();
        if (Objects.nonNull(fromDate)) {
            query.setParameter("fromDate", fromDate);
        }
        Date toDate = filter.getToDate();
        if (Objects.nonNull(toDate)) {
            query.setParameter("toDate", DateUtil.toTheEndOfTheDay(toDate));
        }
    }

    public List<OrganizerRecordEntity> selectOrganizerrecordsForTomorrow() {
        Query query = em.createNativeQuery("select * from organizer_record x where date(x.fixed_date) = :tomorrow OR (x.from_date <= :tomorrow AND x.to_date >= :tomorrow)", OrganizerRecordEntity.class);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 1);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(c.getTime());
        query.setParameter("tomorrow", strDate);
        return query.getResultList();
    }

    public List<OrganizerRecordEntity> selectUpcomingTasks(Integer days) {
        Query query = em.createNativeQuery("select * from organizer_record x where ((x.fixed_date >= CURRENT_DATE() AND x.fixed_date <= :endDate) OR (x.from_date >= CURRENT_DATE() AND x.from_date <= :endDate)) AND x.is_done = false", OrganizerRecordEntity.class);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, days);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(c.getTime());
        query.setParameter("endDate", strDate);
        return query.getResultList();
    }

}
