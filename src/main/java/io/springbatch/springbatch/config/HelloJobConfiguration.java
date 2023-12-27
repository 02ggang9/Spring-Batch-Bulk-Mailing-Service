package io.springbatch.springbatch.config;

import io.springbatch.springbatch.entity.Member;
import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class HelloJobConfiguration {

    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job mailJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new JobBuilder("mailJob", jobRepository)
                .start(helloStep1(jobRepository, platformTransactionManager))
                .next(helloStep2(jobRepository, platformTransactionManager))
                .build();
    }

    @Bean
    public Step helloStep1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("helloStep1", jobRepository)
                .<Member, Member>chunk(10, platformTransactionManager)
                .reader(mailItemReader())
                .writer(mailItemWriter())
                .build();

    }

    @Bean
    public ItemWriter<Member> mailItemWriter() {
        return items -> {
            for (Member member : items) {
                System.out.println("Id :" + member.getId() + ", Name :" + member.getName() + ", Email :" + member.getEmail());
            }
        };
    }

    @Bean
    public ItemReader<Member> mailItemReader() {
        return new JpaPagingItemReaderBuilder<Member>()
                .name("memberMailReader")
                .entityManagerFactory(this.entityManagerFactory)
                .pageSize(10)
                .queryString("SELECT m FROM Member m")
                .build();

    }

    @Bean
    public Step helloStep2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("helloStep2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("=======================");
                    System.out.println(" >> Step2 was executed");
                    System.out.println("=======================");

                    return RepeatStatus.FINISHED;
                }, platformTransactionManager).build();

    }



}
