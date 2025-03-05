package com.jishop.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductDataJobRunner {

    private final JobLauncher jobLauncher;
    private final Job productDataImportJob;

    public void runJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("start", 0L)
                    .addLong("limit", 100000L)
                    .toJobParameters();

            log.info("Batch Job 실행 시작");
            jobLauncher.run(productDataImportJob, jobParameters);
            log.info("Batch Job 실행 완료");
        } catch (Exception e) {
            log.error("Batch Job 실행 중 오류 발생", e);
        }
    }
}
