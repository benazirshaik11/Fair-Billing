package com.britishtelecom.fairbilling;

import com.britishtelecom.fairbilling.service.FairBillingService;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class FairBillingApp {
    public static void main(String[] args) {
        // Print error message if no arguments are provided
        if (args == null || args.length == 0) {
            System.out.println("File path must be provided as an argument.");
            System.exit(1);
        }

        String filePath = args[0];
        Map<String, Stack<Long>> sessions = new HashMap();
        Map<String, Long> totalDuration = new HashMap();
        Map<String, Integer> totalUserSessions = new HashMap();

        // Create a new instance of the FairBillingService and process the log file
        FairBillingService service = new FairBillingService();
        try {
            service.processLogFile(filePath,sessions,totalUserSessions, totalDuration );
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Print the total sessions and duration of each user
        service.printUserSessions(totalUserSessions, totalDuration);

    }
}
