package com.springboot.template.business.member.data.database.entity;

import com.springboot.template.common.model.data.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * 샘플 Entity
 * @author 이봉용
 * @date 25. 9. 7.
 */
@Getter
@Setter
@Entity
@Table(name = "role")
public class RoleEntity extends BaseEntity {

    @Id
    private String role;
    private String description;

}
