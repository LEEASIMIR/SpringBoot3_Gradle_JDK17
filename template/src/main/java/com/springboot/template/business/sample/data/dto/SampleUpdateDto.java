package com.springboot.template.business.sample.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.springboot.template.common.custom.valid.annotation.ValidGlobalPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class SampleUpdateDto {

    @NotNull
    private Long id;

    @Size(max = 50, message = "exception.valid.too.long")
    private String name;

    @ValidGlobalPhoneNumber(message = "exception.valid.unknown.phone")
    private String phone;

//    빈값 관련
//    @NotNull: null이 아니어야 합니다. (빈 문자열이나 공백은 허용)
//    @NotEmpty: null이 아니어야 하며, 컬렉션, 배열, 문자열은 비어 있지 않아야 합니다. (공백은 허용)
//    @NotBlank: null이 아니어야 하며, 공백 문자만으로 이루어져 있지 않아야 합니다.
//    숫자 관련
//    @Size(min=x, max=y): 컬렉션, 배열, 문자열의 크기가 지정된 범위 내에 있어야 합니다.
//    @Min(value=x): 숫자가 지정된 값 이상이어야 합니다.
//    @Max(value=x): 숫자가 지정된 값 이하여야 합니다.
//    @Positive: 양수(0 제외)여야 합니다.
//    @PositiveOrZero: 0 또는 양수여야 합니다.
//    @Negative: 음수(0 제외)여야 합니다.
//    @NegativeOrZero: 0 또는 음수여야 합니다.
//    날짜관련
//    @Future: 현재보다 미래의 날짜/시간이어야 합니다.
//    @FutureOrPresent: 현재 또는 미래의 날짜/시간이어야 합니다.
//    @Past: 현재보다 과거의 날짜/시간이어야 합니다.
//    @PastOrPresent: 현재 또는 과거의 날짜/시간이어야 합니다.
//    문자열 패턴
//    @Pattern(regexp="정규 표현식"): 문자열이 지정된 정규 표현식과 일치해야 합니다.
//    @Email: 올바른 이메일 주소 형식이어야 합니다.
//    @URL: 유효한 URL 형식이어야 합니다.
//    기타
//    @Valid: 객체 내부에 포함된 다른 객체에 대한 유효성 검증을 수행합니다. (예: DTO 안에 다른 DTO가 있을 경우)

}
