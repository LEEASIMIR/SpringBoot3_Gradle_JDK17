package com.springboot.template.business.member.api;

import com.springboot.template.business.member.data.dto.MemberDto;
import com.springboot.template.business.member.data.dto.MemberSaveDto;
import com.springboot.template.business.member.data.dto.MemberWithDrawDto;
import com.springboot.template.business.member.service.MemberService;
import com.springboot.template.common.model.ApiResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/member/api")
public class MemberApi {

    private final MemberService memberService;

    @GetMapping("/info/{username}")
    public ApiResponseEntity<MemberDto> get(@PathVariable String username) {
        return ApiResponseEntity.ok(memberService.get(username));
    }

    @PostMapping("/join-admin")
    public ApiResponseEntity<MemberDto> joinAdmin(@RequestBody @Valid MemberSaveDto dto) {
        return ApiResponseEntity.ok(memberService.create(dto, List.of("ADMIN")));
    }

    @PostMapping("/join")
    public ApiResponseEntity<MemberDto> join(@RequestBody @Valid MemberSaveDto dto) {
        return ApiResponseEntity.ok(memberService.create(dto, List.of("USER")));
    }

    @PostMapping("/update")
    public ApiResponseEntity<MemberDto> update(@RequestBody @Valid MemberSaveDto dto) {
        return ApiResponseEntity.ok(memberService.update(dto, List.of("USER")));
    }

    @PostMapping("/withDraw")
    public ApiResponseEntity<Boolean> withDraw(@RequestBody @Valid MemberWithDrawDto dto) {
        return ApiResponseEntity.ok(memberService.withDraw(dto));
    }

}
