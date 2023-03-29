package com.johnburitto.tdddev.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class DataUtilsTest {
    @Test
    void itShouldValidPhoneNumberLessThan3Digits() {
        //Given
        String phoneNumber = "12";

        //When

        //Then
        assertFalse(DataUtils.isValidPhoneNumber(phoneNumber));
    }

    @Test
    void itShouldValidPhoneNumberGateThan12Digits() {
        //Given
        String phoneNumber = "1234567890563";

        //When

        //Then
        assertFalse(DataUtils.isValidPhoneNumber(phoneNumber));
    }

    @Test
    void itShouldNotValidPhoneNumbersGrateThan3AndLessThan12Digits() {
        //Given
        String phoneNumber = "1234567890";

        //When

        //Then
        assertTrue(DataUtils.isValidPhoneNumber(phoneNumber));
    }

    @Test
    void itShouldValidPhoneNumbersWithBrackets() {
        //Given
        String phoneNumber = "(1234)56789056";

        //When

        //Then
        assertTrue(DataUtils.isValidPhoneNumber(phoneNumber));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1234567890",
            "(1234)56789056",
            "12-3456-7890",
            "12 3456 7890",
            "(1234) 567890",
            "+1(234)567890",
            "+1234567890",
            "+1234 567890",
            "+1 234 567890",
            "+1-234-5678-90",
            "+1-234-56-7890",
    })
    void itShouldValidatePhoneNumber(String phoneNumber) {
        //Given

        //When

        //Then
        assertTrue(DataUtils.isValidPhoneNumber(phoneNumber));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "12345AD890",
            "#1234567890",
            "1234_5678_90",
            "UA 12345 678",
            "+1{234}5678",
    })
    void itShouldNotValidateNumber(String phoneNumber) {
        //Given

        //When

        //Then
        assertFalse(DataUtils.isValidPhoneNumber(phoneNumber));
    }
}