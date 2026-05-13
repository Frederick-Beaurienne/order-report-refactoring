package com.fred.orderreport.goldenmaster;

import com.fred.orderreport.legacyintegration.ReportApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class LegacyGoldenMasterTest {

    @Autowired
    private ReportApplication reportApplication;

    @Test
    void shouldMatchLegacyReport() throws Exception {

        String expected = Files.readString(
                Path.of("legacy/expected/report.txt")
        );

        String actual = reportApplication.run();

        assertEquals(expected, actual);
    }
}