package com.britishtelecom.fairbilling;

import com.britishtelecom.fairbilling.models.SessionsLog;
import com.britishtelecom.fairbilling.service.FairBillingService;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class FairBillingIntegrationTest {

    private File testFile;

    private final FairBillingService service = new FairBillingService();

    private static final String LOG_DIRECTORY = "src/test/resources/testLogs";

    @Test
    public void testAllCasesSessions() {
        String filePath = LOG_DIRECTORY + "/AllCasesSessions.log";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        SessionsLog sessionsLog = service.processLogFile(filePath);

        try {
            // Redirect System.out to the ByteArrayOutputStream
            System.setOut(new PrintStream(outputStream));

            // Call the method that prints the message
            service.printUserSessions(sessionsLog);

            assertEquals(4, sessionsLog.getTotalUserSessions().get("ALICE99"));
            assertEquals(240L, sessionsLog.getTotalDuration().get("ALICE99"));
            assertEquals(3, sessionsLog.getTotalUserSessions().get("CHARLIE"));
            assertEquals(37L, sessionsLog.getTotalDuration().get("CHARLIE"));

            // Assert that the expected message is printed
            assertEquals("ALICE99 4 240\r\nCHARLIE 3 37\r\n", outputStream.toString());
        } finally {
            // Restore the original System.out
            System.setOut(originalOut);
        }

    }
}
