package com.kanezi.springdoc_openapi_showcase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(name = "email", description = "email API")
public interface EmailApi {

    // tag::validation-openapi[]
    record EmailRequest(@Email @NotBlank String to, @Size(min = 3, max = 100) String subject,@NotBlank String text) {}
    // end::validation-openapi[]

    record EmailResponse(String id, String message) {}

    record EmailError(String errorText) {}

    // tag::springdoc-openapi-method-annotations[]
    @Operation(summary = "sends email", tags = {"email"}
            , description = """
            sends email <strong>to</strong> and email address
            <br>
            with <strong>subject</strong>
            <br>
            and <strong>text</strong>
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "email successfully send",
                    content = @Content(schema = @Schema(implementation = EmailRequest.class)
                            , examples = @ExampleObject(value = """
                            {
                                "id": "message-id",
                                "message": "Queued. Thank you."
                            }
                            """)))
    })
    ResponseEntity<EmailResponse> sendEmail(
            @RequestBody(
                    description = "email message",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "to": "user@example.com",
                                "subject": "test mail",
                                "text": "Welcome to our site!"
                            }
                            """)))
            EmailRequest emailRequest);
    // end::springdoc-openapi-method-annotations[]

    @Operation(summary = "checks email id status")
    ResponseEntity<String> checkStatus(
            @Parameter(description = "id of the email message we want to check the status for", required = true)
            String id);

    // tag::spring-data-jpa-openapi-supported[]
    Page<Object> filterEmails(@ParameterObject Pageable pageable);
    // end::spring-data-jpa-openapi-supported[]
}
