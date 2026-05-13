package com.fred.orderreport.goldenmaster;

import legacy.OrderReportLegacy;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LegacyGoldenMasterTest {

    @Test
    void shouldMatchLegacyReport() throws IOException {

        String expected = Files.readString(
                Path.of("legacy/expected/report.txt")
        );

        // TODO inject new version output
        String actual = OrderReportLegacy.run();

        assertEquals(expected, actual);
    }
}