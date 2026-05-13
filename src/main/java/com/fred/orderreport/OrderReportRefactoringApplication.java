package com.fred.orderreport;

import com.fred.orderreport.legacyintegration.ReportApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderReportRefactoringApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(OrderReportRefactoringApplication.class, args);

        ReportApplication.run();
    }

}
