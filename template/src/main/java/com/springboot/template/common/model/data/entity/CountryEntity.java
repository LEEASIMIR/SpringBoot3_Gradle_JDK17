package com.springboot.template.common.model.data.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@Table(name = "country")
@NoArgsConstructor
@AllArgsConstructor
public class CountryEntity {
    @Id
    private String id;

    @Column(name = "country_name", nullable = false) // 'country_name' 컬럼, NOT NULL
    private String countryName;

    @Column(name = "iso_code", nullable = false, unique = true, length = 2) // 'iso_code' 컬럼, NOT NULL, UNIQUE, 길이 2
    private String isoCode;

    @Column(name = "country_code", nullable = false, length = 5) // 'country_code' 컬럼, NOT NULL, 길이 5
    private String countryCode;
}
