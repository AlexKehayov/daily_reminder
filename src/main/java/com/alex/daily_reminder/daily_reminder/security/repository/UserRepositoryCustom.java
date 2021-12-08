package com.alex.daily_reminder.daily_reminder.security.repository;

import com.alex.daily_reminder.daily_reminder.filter.Sortable;
import com.alex.daily_reminder.daily_reminder.filter.UserFilter;
import com.alex.daily_reminder.daily_reminder.filter.UserSorterUtils;
import com.alex.daily_reminder.daily_reminder.security.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustom {


    @Autowired
    private EntityManager em;

    public List<UserEntity> selectUserEntries(UserFilter filter) {
        String buildQuery = buildQuery(filter, false);
        Query query = em.createNativeQuery(buildQuery, UserEntity.class);
        addQueryParams(filter, query);
        query.setMaxResults(filter.getPageSize());
        query.setFirstResult((filter.getPage() - 1) * filter.getPageSize());
        return query.getResultList();
    }

    public int selectUserEntriesCount(UserFilter filter) {
        String buildQuery = buildQuery(filter, true);
        Query query = em.createNativeQuery(buildQuery);
        addQueryParams(filter, query);
        Number result = (Number) query.getSingleResult();
        return result.intValue();
    }

    private String buildQuery(UserFilter filter, boolean isCount) {
        StringBuilder queryBuilder = new StringBuilder("SELECT ");
        queryBuilder.append(isCount ? " COUNT(*) " : " * ");
        queryBuilder.append(" FROM daily_reminder.users d where 1=1");

        if (StringUtils.hasText(filter.getUsername())) {
            queryBuilder.append(" AND LOWER(d.username) like LOWER(:username) ");
        }
        if (StringUtils.hasText(filter.getFirstName())) {
            queryBuilder.append(" AND LOWER(d.first_name) like LOWER(:firstName) ");
        }
        if (StringUtils.hasText(filter.getLastName())) {
            queryBuilder.append(" AND LOWER(d.last_name) like LOWER(:lastName) ");
        }
        if (StringUtils.hasText(filter.getEmail())) {
            queryBuilder.append(" AND LOWER(d.email) like LOWER(:email) ");
        }

        if (!isCount) {
            String sortColumn = filter.getSortColumn();
            String sortOrder = filter.getSortOrder();
            if (!(Sortable.ASC_ORDER.equalsIgnoreCase(sortOrder) || Sortable.DESC_ORDER.equalsIgnoreCase(sortOrder))) {
                sortOrder = Sortable.ASC_ORDER;
            }
            String[] columns = UserSorterUtils.sorterColumnMap().get(sortColumn).split(",");
            String order = String.join(" " + sortOrder + " , ", columns) + " " + sortOrder;
            queryBuilder.append(" ORDER BY ").append(order);
        }
        return queryBuilder.toString();
    }

    private void addQueryParams(UserFilter filter, Query query) {

        if (StringUtils.hasText(filter.getUsername())) {
            query.setParameter("username", "%" + filter.getUsername() + "%");
        }
        if (StringUtils.hasText(filter.getFirstName())) {
            query.setParameter("firstName", "%" + filter.getFirstName() + "%");
        }
        if (StringUtils.hasText(filter.getLastName())) {
            query.setParameter("lastName", "%" + filter.getLastName() + "%");
        }
        if (StringUtils.hasText(filter.getEmail())) {
            query.setParameter("email", "%" + filter.getEmail() + "%");
        }

    }

}
