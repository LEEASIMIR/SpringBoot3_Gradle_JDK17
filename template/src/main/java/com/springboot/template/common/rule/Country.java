package com.springboot.template.common.rule;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Country {
    SOUTH_KOREA("South Korea", "KR", "82"),
    UNITED_STATES("United States", "US", "1"),
    JAPAN("Japan", "JP", "81"),
    CHINA("China", "CN", "86"),
    UNITED_KINGDOM("United Kingdom", "GB", "44"),
    FRANCE("France", "FR", "33"),
    GERMANY("Germany", "DE", "49"),
    AUSTRALIA("Australia", "AU", "61"),
    CANADA("Canada", "CA", "1"),
    SPAIN("Spain", "ES", "34"),
    RUSSIA("Russia", "RU", "7");

    private final String countryName;
    private final String isoCode;
    private final String countryCode;

    public static Country fromIsoCode(String isoCode) {
        for (Country country : Country.values()) {
            if (country.isoCode.equalsIgnoreCase(isoCode)) {
                return country;
            }
        }
        throw new IllegalArgumentException("Invalid ISO code: " + isoCode);
    }

    public static Country fromCountryCode(String countryCode) {
        for (Country country : Country.values()) {
            if (country.countryCode.equals(countryCode)) {
                return country;
            }
        }
        throw new IllegalArgumentException("Invalid country calling code: " + countryCode);
    }
}
