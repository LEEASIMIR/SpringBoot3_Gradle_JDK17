package com.springboot.template.business.sample.data.database.entity;

import com.springboot.template.business.sample.data.dto.SampleSaveDto;
import com.springboot.template.common.model.data.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * 샘플 Entity
 * @author 이봉용
 * @date 25. 9. 7.
 */
@Getter
@Setter
@Entity(name = "sample")
public class SampleEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private  String name;

    @Column(nullable = false)
    private String phone;

}
