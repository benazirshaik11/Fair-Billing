package com.britishtelecom.fairbilling;

import com.britishtelecom.fairbilling.models.SessionsLog;
import com.britishtelecom.fairbilling.service.FairBillingService;

public class FairBillingApp {
    public static void main(String[] args) {
        // Print error message if no arguments are provided
        if (args == null || args.length == 0) {
            System.out.println("File path must be provided as an argument.");
            System.exit(1);
        }
        String filePath = args[0];

        // Create a new instance of the FairBillingService and process the log file
        FairBillingService service = new FairBillingService();
        try{
            SessionsLog totalUserSessions = service.processLogFile(filePath);
            service.printUserSessions(totalUserSessions);
        }
        catch (Exception e) {
            System.out.println("processing log file failed due to: " + e.getMessage());
            System.exit(1);
        }
    }
}
