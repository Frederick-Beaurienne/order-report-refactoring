package com.fred.orderreport;

import com.fred.orderreport.legacyintegration.ReportApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class OrderReportRefactoringApplication {

    public static void main(String[] args) throws Exception {

        ConfigurableApplicationContext context =
                SpringApplication.run(OrderReportRefactoringApplication.class, args);

        ReportApplication reportApplication =
                context.getBean(ReportApplication.class);

        reportApplication.run();
    }

}
