package com.britishtelecom.fairbilling.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Stack;

public class FairBillingService {

    //Processes the log file and calculates the total duration and number of sessions for each user.
    public void processLogFile(String filePath, Map<String, Stack<Long>> sessions, Map<String, Integer> totalUserSessions, Map<String, Long> totalDuration) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            Long earliestTime = null;
            Long latestTime = null;
            String line;
            while ((line = br.readLine()) != null) {
                String[] record = line.split("\\s+");
                long timestamp = timeToSeconds(record[0]);
                if (earliestTime == null) {
                    earliestTime = timestamp;
                }
                latestTime = timestamp;
                String user = record[1];
                String action = record[2];

                if (action.equals("Start")) {
                    handleStartAction(sessions, totalUserSessions, timestamp, user);
                } else if (record[2].equals("End")) {
                    handleEndAction(sessions, totalDuration, totalUserSessions, timestamp, user, earliestTime);
                }
            }
            calculateRemainingSessions(sessions, totalDuration, latestTime);


        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

    }


    //Handles the 'Start' action by recording the session start time for a user
    public void handleStartAction(Map<String, Stack<Long>> sessions, Map<String, Integer> totalUserSessions, long timestamp, String user) {
        sessions.computeIfAbsent(user, k -> new Stack<>()).push(timestamp);
        totalUserSessions.put(user, totalUserSessions.getOrDefault(user, 0) + 1);

    }

   // Handles the 'End' action by calculating the session duration for a user
    public void handleEndAction(Map<String, Stack<Long>> sessions, Map<String, Long> totalDuration, Map<String, Integer> totalUserSessions, long timestamp, String user, long earliestTime) {
        Stack<Long> stack = sessions.get(user);
        if (stack != null && !stack.isEmpty()) {
            long duration = timestamp - stack.pop();
            totalDuration.put(user, totalDuration.getOrDefault(user, 0L) + duration);
        } else {
            totalDuration.put(user, totalDuration.getOrDefault(user, 0L) + timestamp - earliestTime);
            totalUserSessions.put(user, totalUserSessions.getOrDefault(user, 0) + 1);
        }

    }

    //Calculates the remaining sessions for users who did not end their session during the log file's duration
    public void calculateRemainingSessions(Map<String, Stack<Long>> sessions, Map<String, Long> totalDuration, Long latestTime) {
        for (Map.Entry<String, Stack<Long>> entry : sessions.entrySet()) {
            while (!entry.getValue().isEmpty()) {
                long duration = latestTime - entry.getValue().pop();
                totalDuration.put(entry.getKey(), totalDuration.getOrDefault(entry.getKey(), 0L) + duration);

            }
        }
    }


    // Prints the total sessions and the total duration of each user
    public void printUserSessions(Map<String, Integer> totalUserSessions, Map<String, Long> totalDuration) {
        for (String map : totalUserSessions.keySet()) {
            System.out.println(map + " " + totalUserSessions.get(map) + " " + totalDuration.get(map));
        }
    }

    //Converts a time string in HH:mm:ss format to seconds
    public static long timeToSeconds(String time) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date reference = dateFormat.parse(time);
        return (reference.getTime()) / 1000L;
    }
}
