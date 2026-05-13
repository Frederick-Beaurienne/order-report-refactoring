package com.fred.orderreport.integration;

import com.fred.orderreport.orchestration.ReportGenerationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReportApplicationIntegrationTest {

    @Autowired
    private ReportGenerationService reportGenerationService;

    @Test
    void shouldGenerateCompleteReport() throws Exception {

        String report = reportGenerationService.run();

        assertNotNull(report);
        assertFalse(report.isBlank());

        // Présence structure globale
        assertTrue(report.contains("Customer:"));
        assertTrue(report.contains("Subtotal:"));
        assertTrue(report.contains("Discount:"));
        assertTrue(report.contains("Tax:"));
        assertTrue(report.contains("Total:"));
        assertTrue(report.contains("Grand Total"));
        assertTrue(report.contains("Total Tax Collected"));

        // Vérification données legacy connues
        assertTrue(report.contains("Alice"));
        assertTrue(report.contains("EUR"));

        // Vérification format multi-lignes
        String[] lines = report.split("\n");
        assertTrue(lines.length > 10);

        // Vérification absence résultat vide
        assertFalse(report.trim().isEmpty());
    }
}