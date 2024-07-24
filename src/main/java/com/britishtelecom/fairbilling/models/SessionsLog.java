package com.britishtelecom.fairbilling.models;

import java.util.Map;

public class SessionsLog {

    private final Map<String, Long> totalDuration;
    private final Map<String, Integer> totalUserSessions;

    //can be replaced with lombok annotation
    public SessionsLog(Map<String, Long> totalDuration, Map<String, Integer> totalUserSessions) {
        this.totalDuration = totalDuration;
        this.totalUserSessions = totalUserSessions;
    }

    // getters and setters
    public Map<String, Long> getTotalDuration() {
        return totalDuration;
    }

    public Map<String, Integer> getTotalUserSessions() {
        return totalUserSessions;
    }
}
