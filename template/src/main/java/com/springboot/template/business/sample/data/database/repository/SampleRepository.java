package com.springboot.template.business.sample.data.database.repository;

import com.springboot.template.business.sample.data.database.entity.SampleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleRepository extends JpaRepository<SampleEntity, Long>, JpaSpecificationExecutor<SampleEntity> {
    @Query("select s.name, s.phone from sample s")
    Page<SampleEntity> findNamePhone(Specification<SampleEntity> spec, Pageable pageable);
}
