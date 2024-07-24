package com.britishtelecom.fairbilling.service;

import com.britishtelecom.fairbilling.models.SessionsLog;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class FairBillingServiceTest {

    private File testFile;

    private final FairBillingService service = new FairBillingService();

    private static final String LOG_DIRECTORY = "src/test/resources/testLogs";

    @Test
    public void testNormalSessionsWithMultipleUsers() {
        String filePath = LOG_DIRECTORY + "/NormalSessionsWithMultipleUsers.log";

        SessionsLog sessionsLog = service.processLogFile(filePath);

        assertEquals(3, sessionsLog.getTotalUserSessions().get("user1"));
        assertEquals(71L, sessionsLog.getTotalDuration().get("user1"));
        assertEquals(2, sessionsLog.getTotalUserSessions().get("user2"));
        assertEquals(19L, sessionsLog.getTotalDuration().get("user2"));
    }

    @Test
    public void testWithOverlappingSessions() {
        String filePath = LOG_DIRECTORY + "/OverlappingSessions.log";
        SessionsLog sessionsLog = service.processLogFile(filePath);
        assertEquals(2, sessionsLog.getTotalUserSessions().get("user1"));
        assertEquals(88L, sessionsLog.getTotalDuration().get("user1"));

    }

    @Test
    public void testNoEndOrEndSession() {
        String filePath = LOG_DIRECTORY + "/NoEndOrEndSession.log";
        SessionsLog sessionsLog = service.processLogFile(filePath);
        assertEquals(2, sessionsLog.getTotalUserSessions().get("user1"));
        assertEquals(106L, sessionsLog.getTotalDuration().get("user1"));
        assertEquals(2, sessionsLog.getTotalUserSessions().get("user2"));
        assertEquals(136L, sessionsLog.getTotalDuration().get("user2"));

    }

    @Test
    public void testWithEmptyLogFile() {
        String filePath = LOG_DIRECTORY + "/EmptyLogFile.log";
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.processLogFile(filePath));
        assertEquals("No valid entries found in the log file", exception.getMessage());
    }

    @Test
    public void testWithInvalidEntries() {
        String filePath = LOG_DIRECTORY + "/InvalidEntries.log";
        SessionsLog sessionsLog = service.processLogFile(filePath);
        assertEquals(3, sessionsLog.getTotalUserSessions().size());
        assertEquals(3, sessionsLog.getTotalDuration().size());
        assertEquals(45L,sessionsLog.getTotalDuration().get("User") );
    }

    @Test
    public void testProcessLogFile_NonExistentFile() {
        String filePath = "path/to/nonexistent/logfile.txt";
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.processLogFile(filePath));
        assertEquals("java.io.FileNotFoundException: path\\to\\nonexistent\\logfile.txt (The system cannot find the path specified)", exception.getMessage());
    }

    @Test
    void testPrintMessage() {
        // Create a ByteArrayOutputStream to capture the output of System.out
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try {
            // Redirect System.out to the ByteArrayOutputStream
            System.setOut(new PrintStream(outputStream));

            // Call the method that prints the message
            service.printUserSessions(new SessionsLog(
                    new HashMap<String, Long>() {
                        {
                            put("user1", 1L);
                            put("user2", 2L);
                        }
                    },
                    new HashMap<String, Integer>() {{
                        put("user1", 3);
                        put("user2", 4);
                    }}
            ));

            // Assert that the expected message is printed
            assertEquals("user1 3 1\r\nuser2 4 2\r\n", outputStream.toString());
        } finally {
            // Restore the original System.out
            System.setOut(originalOut);
        }

    }
}
