package com.agency.appointment.repository;

import com.agency.appointment.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.ListQuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends ListQuerydslPredicateExecutor<T>, JpaRepository<T, Long> {
}