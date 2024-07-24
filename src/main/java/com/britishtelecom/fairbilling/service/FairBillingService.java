package com.britishtelecom.fairbilling.service;

import com.britishtelecom.fairbilling.models.SessionsLog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Pattern;

public class FairBillingService {


    /**
     * Processes the log file and calculates the total duration and number of sessions for each user.
     *
     * @param filePath The path to the log file.
     * @return A SessionsLog object containing the sessions, total duration, and total user sessions.
     * @throws RuntimeException If an IOException or ParseException occurs during file processing.
     */
    public SessionsLog processLogFile(String filePath) {

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            Map<String, Stack<Long>> sessions = new HashMap<>();
            Map<String, Long> totalDuration = new HashMap<>();
            Map<String, Integer> totalUserSessions = new HashMap<>();
            Long earliestTime = null;
            Long latestTime = null;
            String line;
            while ((line = br.readLine()) != null) {
                String[] record = line.split("\\s+");
                if (checkValidEntry(record)){
                    long timestamp = timeToSeconds(record[0]);
                    if (earliestTime == null) {
                        earliestTime = timestamp;
                    }
                    latestTime = timestamp;
                    String user = record[1];
                    String action = record[2];

                    if (action.equals("Start")) {
                        handleStartAction(sessions, totalUserSessions, timestamp, user);
                    } else if (action.equals("End")) {
                        handleEndAction(sessions, totalDuration, totalUserSessions, timestamp, user, earliestTime);
                    }
                }
            }
            calculateRemainingSessions(sessions, totalDuration, latestTime);
            if(totalUserSessions.isEmpty())
                throw new RuntimeException("No valid entries found in the log file");
            return new SessionsLog(totalDuration, totalUserSessions);

        } catch (IOException | ParseException exception) {
            System.out.println("Exception occurred :" + exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    /**
     * Prints the total sessions and the total duration of each user.
     *
     * @param sessionsLog The SessionsLog object containing the sessions, total duration, and total user sessions.
     *
     * This method iterates over the keys of the totalUserSessions map in the SessionsLog object.
     * For each user, it prints the user's name, the total number of sessions, and the total duration of all sessions.
     * The output format is: "username totalSessions totalDuration".
     */
    public void printUserSessions(SessionsLog sessionsLog) {
        for (String map : sessionsLog.getTotalUserSessions().keySet()) {
            System.out.println(map + " " + sessionsLog.getTotalUserSessions().get(map) + " " + sessionsLog.getTotalDuration().get(map));
        }
    }

    private boolean checkValidEntry(String[] record) {
        Pattern timePattern = Pattern.compile("^([0-1][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$");
        Pattern usernamePattern = Pattern.compile("^[A-Za-z0-9]+$");
        if (record.length != 3 || !timePattern.matcher(record[0]).matches() || !usernamePattern.matcher(record[1]).matches())
            return false;
        return true;
    }

    //Handles the 'Start' action by recording the session start time for a user
    private void handleStartAction(Map<String, Stack<Long>> sessions, Map<String, Integer> totalUserSessions, long timestamp, String user) {
        sessions.computeIfAbsent(user, k -> new Stack<>()).push(timestamp);
        totalUserSessions.put(user, totalUserSessions.getOrDefault(user, 0) + 1);

    }

    // Handles the 'End' action by calculating the session duration for a user
    private void handleEndAction(Map<String, Stack<Long>> sessions, Map<String, Long> totalDuration, Map<String, Integer> totalUserSessions, long timestamp, String user, long earliestTime) {
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
    private void calculateRemainingSessions(Map<String, Stack<Long>> sessions, Map<String, Long> totalDuration, Long latestTime) {
        for (Map.Entry<String, Stack<Long>> entry : sessions.entrySet()) {
            while (!entry.getValue().isEmpty()) {
                long duration = latestTime - entry.getValue().pop();
                totalDuration.put(entry.getKey(), totalDuration.getOrDefault(entry.getKey(), 0L) + duration);
            }
        }
    }

    //Converts a time string in HH:mm:ss format to seconds
    private long timeToSeconds(String time) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date reference = dateFormat.parse(time);
        return (reference.getTime()) / 1000L;
    }
}
