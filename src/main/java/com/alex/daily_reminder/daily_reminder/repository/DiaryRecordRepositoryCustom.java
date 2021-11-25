package com.alex.daily_reminder.daily_reminder.repository;

import com.alex.daily_reminder.daily_reminder.filter.DiaryRecordFilter;
import com.alex.daily_reminder.daily_reminder.filter.DiaryRecordSorterUtils;
import com.alex.daily_reminder.daily_reminder.filter.Sortable;
import com.alex.daily_reminder.daily_reminder.model.DiaryRecordEntity;
import com.alex.daily_reminder.daily_reminder.util.DateUtil;
import com.alex.daily_reminder.daily_reminder.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class DiaryRecordRepositoryCustom {


    @Autowired
    private EntityManager em;

    @Autowired
    private SecurityUtil securityUtil;

    public List<DiaryRecordEntity> selectConfigParams(DiaryRecordFilter filter) {
        String buildQuery = buildQuery(filter, false);
        Query query = em.createNativeQuery(buildQuery, DiaryRecordEntity.class);
        addQueryParams(filter, query);
        query.setMaxResults(filter.getPageSize());
        query.setFirstResult((filter.getPage() - 1) * filter.getPageSize());
        return query.getResultList();
    }

    public int selectConfigParamsCount(DiaryRecordFilter filter) {
        String buildQuery = buildQuery(filter, true);
        Query query = em.createNativeQuery(buildQuery);
        addQueryParams(filter, query);
        Number result = (Number) query.getSingleResult();
        return result.intValue();
    }

    private String buildQuery(DiaryRecordFilter filter, boolean isCount) {
        StringBuilder queryBuilder = new StringBuilder("SELECT ");
        queryBuilder.append(isCount ? " COUNT(*) " : " * ");
        queryBuilder.append(" FROM daily_reminder.diary_record d ");
        queryBuilder.append(" WHERE 1=1 AND d.user_username = :username ");

        if (StringUtils.hasText(filter.getContent())) {
            queryBuilder.append(" AND LOWER(d.content) like LOWER(:content) ");
        }
        if (Objects.nonNull(filter.getCreateDateFrom())) {
            queryBuilder.append(" AND d.created_date >= :createDateFrom ");
        }
        if (Objects.nonNull(filter.getCreateDateTo())) {
            queryBuilder.append(" AND d.created_date <= :createDateTo ");
        }

        if (!isCount) {
            String sortColumn = filter.getSortColumn();
            String sortOrder = filter.getSortOrder();
            if (!(Sortable.ASC_ORDER.equalsIgnoreCase(sortOrder) || Sortable.DESC_ORDER.equalsIgnoreCase(sortOrder))) {
                sortOrder = Sortable.ASC_ORDER;
            }
            String[] columns = DiaryRecordSorterUtils.sorterColumnMap().get(sortColumn).split(",");
            String order = String.join(" " + sortOrder + " , ", columns) + " " + sortOrder;
            queryBuilder.append(" ORDER BY ").append(order);
        }
        return queryBuilder.toString();
    }

    private void addQueryParams(DiaryRecordFilter filter, Query query) {

        query.setParameter("username", securityUtil.getLoggedUser().getUsername());

        if (StringUtils.hasText(filter.getContent())) {
            query.setParameter("content", "%" + filter.getContent() + "%");
        }
        Date createDateFrom = filter.getCreateDateFrom();
        if (Objects.nonNull(createDateFrom)) {
            query.setParameter("createDateFrom", createDateFrom);
        }
        Date createDateTo = filter.getCreateDateTo();
        if (Objects.nonNull(createDateTo)) {
            query.setParameter("createDateTo", DateUtil.toTheEndOfTheDay(createDateTo));
        }

    }

}
