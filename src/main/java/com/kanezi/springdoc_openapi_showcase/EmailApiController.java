package com.kanezi.springdoc_openapi_showcase;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// tag::spring-rest-controller[]
@RestController
@RequestMapping("/api/v1/email")
public class EmailApiController implements EmailApi {

    @Override
    @PostMapping
    public ResponseEntity<EmailResponse> sendEmail(@RequestBody @Valid EmailRequest emailRequest) {
        return ResponseEntity.ok(new EmailResponse("message-id", "Queued. Thank you."));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<String> checkStatus(@PathVariable String id) {
        return ResponseEntity.ok(id);
    }

    @GetMapping("/filter")
    public Page<Object> filterEmails(Pageable pageable) {
        throw new RuntimeException("TODO");
    }
}
// end::spring-rest-controller[]