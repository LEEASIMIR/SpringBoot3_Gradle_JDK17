package com.springboot.template.common.model.data.repository;

import com.springboot.template.business.member.data.database.entity.MemberEntity;
import com.springboot.template.common.model.data.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, String> {
}
