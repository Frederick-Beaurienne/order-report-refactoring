package com.fred.orderreport;

import com.fred.orderreport.orchestration.ReportGenerationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class OrderReportRefactoringApplication {

    public static void main(String[] args) throws Exception {

        ConfigurableApplicationContext context =
                SpringApplication.run(OrderReportRefactoringApplication.class, args);

        ReportGenerationService reportGenerationService =
                context.getBean(ReportGenerationService.class);

        reportGenerationService.run();
    }

}
