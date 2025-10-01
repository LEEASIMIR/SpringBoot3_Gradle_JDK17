package com.springboot.template.security;

import com.springboot.template.business.member.data.database.entity.MemberEntity;
import com.springboot.template.business.member.data.database.entity.RoleEntity;
import com.springboot.template.business.member.data.database.repository.MemberRepository;
import com.springboot.template.business.member.data.dto.MemberDto;
import com.springboot.template.business.member.data.dto.MemberPrivateDto;
import com.springboot.template.business.member.data.dto.RoleDto;
import com.springboot.template.business.member.service.MemberService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberService.getDetachedMemberEntity(username)
                 .map(this::createUserDetails)
                 .orElseThrow(() -> new UsernameNotFoundException("not found user"));
    }

    private UserDetails createUserDetails(MemberEntity member) {
         return User.builder()
                 .username(member.getUsername())
                 .password(member.getPassword())
                 .roles(member.getRoles().stream().map(RoleEntity::getRole).collect(Collectors.joining()))
                 .build();
     }
}
