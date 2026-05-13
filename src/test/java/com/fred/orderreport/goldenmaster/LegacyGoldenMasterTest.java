package com.fred.orderreport.goldenmaster;

import com.fred.orderreport.orchestration.ReportGenerationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class LegacyGoldenMasterTest {

    @Autowired
    private ReportGenerationService reportGenerationService;

    @Test
    void shouldMatchLegacyReport() throws Exception {

        String expected = Files.readString(
                Path.of("legacy/expected/report.txt")
        );

        String actual = reportGenerationService.run();

        assertEquals(expected, actual);
    }
}