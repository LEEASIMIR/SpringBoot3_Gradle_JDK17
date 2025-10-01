package com.springboot.template.business.member.data.database.entity;

import com.springboot.template.common.model.data.entity.BaseEntity;
import com.springboot.template.common.model.data.entity.CountryEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;

/**
 * 샘플 Entity
 * @author 이봉용
 * @date 25. 9. 7.
 */
@Getter
@Builder
@Entity
@Table(name = "member",
    indexes = {
        @Index(name = "idx_username", columnList = "username")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "unique_username", columnNames = {"username"})
    }
)
@NoArgsConstructor
@AllArgsConstructor
public class MemberEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private  String username;

    @Column(nullable = false)
    private String password;

    public void setEncryptedPassword(String password) {
        if(password == null || password.isEmpty()) return;
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String email;

    @Column
    private Instant lastLoginDate;

    @Column
    private Instant lastChangePasswordDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "member_role",
            joinColumns = {@JoinColumn(name = "member_id")},
            inverseJoinColumns = {@JoinColumn(name = "member_role")}
    )
    List<RoleEntity> roles;

    public void setRoles(List<RoleEntity> roles) {
        if(roles == null || roles.isEmpty()) return;
        this.roles = roles;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "member_country",
            joinColumns = {@JoinColumn(name = "member_id")},
            inverseJoinColumns = {@JoinColumn(name = "member_country")}
    )
    List<CountryEntity> countries;

    public void setCountries(List<CountryEntity> countries) {
        if(countries == null || countries.isEmpty()) return;
        this.countries = countries;
    }

}
