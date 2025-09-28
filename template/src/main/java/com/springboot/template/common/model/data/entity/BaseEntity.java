package com.springboot.template.common.model.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * 단순 공통 필드
 * @author 이봉용
 * @date 25. 9. 7.
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Column(insertable = true, updatable = false, nullable = false)
    @CreatedDate
    private Instant createDate;//Instant UTC 로 저장함

    @Column(insertable = true, updatable = true, nullable = false)
    @LastModifiedDate
    private Instant updateDate;//Instant UTC 로 저장함
}
