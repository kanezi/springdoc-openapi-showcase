package com.kanezi.springdoc_openapi_showcase;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;


@WebMvcTest
class EmailApiExceptionTranslatorTest {

    @Autowired
    MockMvcTester mockMvcTester;

    @Test
    void validationErrorReturnsErrorTextJson()  {
        String invalidRequest = """
                {
                  "to": "user_example.com",
                  "subject": "te",
                  "text": "Welcome to our site!"
                }
                """;

        mockMvcTester
                .post()
                .uri("/api/v1/email")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(invalidRequest)
                .assertThat()
                .hasStatus(HttpStatus.BAD_REQUEST)
                .hasContentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .bodyJson()
                .extractingPath("$.errorText")
                .asString()
                .contains("to: must be a well-formed email address")
                .contains("subject: size must be between 3 and 100");
    }


}