package com.springboot.template.common.custom.valid.implement;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.springboot.template.common.custom.valid.annotation.ValidGlobalPhoneNumber;
import com.springboot.template.common.rule.Country;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GlobalPhoneNumberValidator implements ConstraintValidator<ValidGlobalPhoneNumber, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isEmpty()) {
            return true;
        }

        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phoneNumber;
        try {
            phoneNumber = phoneNumberUtil.parse(value, getCountryCodeByCountryNumber(value.split("-")[0]));
        } catch (NumberParseException | IllegalArgumentException e) {
            return false;
        }
        return phoneNumberUtil.isValidNumber(phoneNumber);
    }

    private String getCountryCodeByCountryNumber(String countryNumber) {
        Country country = Country.fromCountryCode(countryNumber.replace("+", ""));
        return country.getIsoCode();
    }
}
