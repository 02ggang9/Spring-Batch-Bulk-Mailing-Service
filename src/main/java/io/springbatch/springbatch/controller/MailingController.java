package io.springbatch.springbatch.controller;

import io.springbatch.springbatch.dto.MonthRepostRequest;
import io.springbatch.springbatch.service.MonthMailingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailingController {

    private final MonthMailingService mailingService;

    @PostMapping("/month-report")
    public ResponseEntity<Void> reportMonthSummary(@RequestBody @Valid MonthRepostRequest request) throws Exception {
        mailingService.runMailingBatch(request.getJobName(), request.getSubject(), request.getReportMessage());
        return ResponseEntity.noContent().build();
    }
}
