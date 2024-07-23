package com.britishtelecom.fairbilling.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class FairBillingServiceTest {

    private File testFile;

    private final FairBillingService service=new FairBillingService();


    private final String logDirectory = "src/test/resources/testLogs";

    @BeforeEach
    public void setUp() throws IOException {
        // Create the directory if it does not exist
        Path path = Paths.get(logDirectory);
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
    }

    @Test
    public void testBasicFunctionality() throws IOException, ParseException{
        String filePath = logDirectory+ "/BasicFunctionality.log";
        List<String> lines = Arrays.asList(
                "00:05:00 user1 Start",
                "00:05:12 user1 End"
        );
        Files.write(Paths.get(filePath), lines);

        Map<String, Stack<Long>> sessions = new HashMap<>();
        Map<String, Integer> totalUserSessions = new HashMap<>();
        Map<String, Long> totalDuration = new HashMap<>();

        service.processLogFile(filePath,sessions,totalUserSessions,totalDuration);

        assertEquals(1,totalUserSessions.get("user1"));
        assertEquals(12L, totalDuration.get("user1"));
    }
    @Test
    public void testMultipleSessionsforOneUser() throws IOException, ParseException {
        String filePath=logDirectory+ "/MultipleSessionsforOneUser.log";

        List<String> lines= Arrays.asList(
                "00:05:00 user1 Start",
                "00:05:12 user1 End",
                "00:05:15 user1 Start",
                "00:05:25 user1 End"
        );

        Files.write(Paths.get(filePath),lines);

        Map<String, Stack<Long>> sessions = new HashMap<>();
        Map<String, Integer> totalUserSessions = new HashMap<>();
        Map<String, Long> totalDuration = new HashMap<>();

        service.processLogFile(filePath,sessions,totalUserSessions,totalDuration);
        assertEquals(2,totalUserSessions.get("user1"));
        assertEquals(22L, totalDuration.get("user1"));

    }

    @Test
    public void testMultipleUsersSessions() throws IOException, ParseException{
        String filePath= logDirectory+ "/MultipleUsersSessions.log";
        List<String> lines= Arrays.asList(
                "00:05:00 user1 Start",
                "00:05:12 user1 End",
                "00:05:15 user2 Start",
                "00:05:25 user2 End"
        );

        Files.write(Paths.get(filePath),lines);

        Map<String, Stack<Long>> sessions = new HashMap<>();
        Map<String, Integer> totalUserSessions = new HashMap<>();
        Map<String, Long> totalDuration = new HashMap<>();

        service.processLogFile(filePath,sessions,totalUserSessions,totalDuration);

        assertEquals(1,totalUserSessions.get("user1"));
        assertEquals(12L, totalDuration.get("user1"));
        assertEquals(1,totalUserSessions.get("user2"));
        assertEquals(10L, totalDuration.get("user2"));

    }
    @Test
    public void testWithOverlappingSessions() throws IOException, ParseException{
        String filePath= logDirectory+ "/OverlappingSessions.log";
        List<String> lines= Arrays.asList(
                "00:05:00 user1 Start",
                "00:05:12 user1 Start",
                "00:05:15 user1 End",
                "00:06:25 user1 End"
        );

        Files.write(Paths.get(filePath),lines);

        Map<String, Stack<Long>> sessions = new HashMap<>();
        Map<String, Integer> totalUserSessions = new HashMap<>();
        Map<String, Long> totalDuration = new HashMap<>();

        service.processLogFile(filePath,sessions,totalUserSessions,totalDuration);

        assertEquals(2,totalUserSessions.get("user1"));
        assertEquals(88L, totalDuration.get("user1"));

    }

    @Test
    public void testWithNoEndSessions() throws IOException, ParseException{
        String filePath= logDirectory+ "/NoEndSession.log";
        List<String> lines= Arrays.asList(
                "00:05:00 user1 Start",
                "00:05:12 user1 Start",
                "00:05:15 user1 End"
        );

        Files.write(Paths.get(filePath),lines);

        Map<String, Stack<Long>> sessions = new HashMap<>();
        Map<String, Integer> totalUserSessions = new HashMap<>();
        Map<String, Long> totalDuration = new HashMap<>();

        service.processLogFile(filePath,sessions,totalUserSessions,totalDuration);

        assertEquals(2,totalUserSessions.get("user1"));
        assertEquals(18L, totalDuration.get("user1"));

    }

    @Test
    public void testWithNoStartSessions() throws IOException, ParseException{
        String filePath= logDirectory+ "/NoStartSession.log";
        List<String> lines= Arrays.asList(
                "00:05:12 user1 Start",
                "00:05:15 user1 End",
                "00:05:43 user1 End"
        );

        Files.write(Paths.get(filePath),lines);

        Map<String, Stack<Long>> sessions = new HashMap<>();
        Map<String, Integer> totalUserSessions = new HashMap<>();
        Map<String, Long> totalDuration = new HashMap<>();

        service.processLogFile(filePath,sessions,totalUserSessions,totalDuration);

        assertEquals(2,totalUserSessions.get("user1"));
        assertEquals(34L, totalDuration.get("user1"));
    }

    @Test
    public void testWithEmptyLogFile() throws IOException, ParseException{
        String filePath= logDirectory+ "/EmptyLogFile.log";
        List<String> lines=Collections.emptyList();
        Files.write(Paths.get(filePath),lines);

        Map<String, Stack<Long>> sessions = new HashMap<>();
        Map<String, Integer> totalUserSessions = new HashMap<>();
        Map<String, Long> totalDuration = new HashMap<>();

        service.processLogFile(filePath,sessions,totalUserSessions,totalDuration);

        assertEquals(0,totalUserSessions.size());
        assertEquals(0, totalDuration.size());
    }
}
