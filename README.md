# FairBilling

FairBilling is a Java-based utility designed to process log files and calculate the total session durations for users. It reads log files containing user session start and end times and calculates the total time each user spent in their sessions.

## Table of Contents

- [Getting Started](#getting-started)
- [Usage](#usage)
- [Features](#features)
- [Testing](#testing)
- [Error Handling](#error-handling)

--------------------------------------------------------------------------------------------------------------------------------------------------------

## Getting Started

### Prerequisites

- Java 8 or higher
- Maven (optional, for dependency management)

### Installation

To install and run this project, follow these steps:

1. ***Clone the repository***
    ```bash
    git clone https://github.com/yourusername/fair-billing.git
    cd fair-billing
    ```

2. ***Build the project***
    ```bash
    mvn clean install
    ```

3. ***Run the application***
    ```bash
    java -jar target/fair-billing-1.0-SNAPSHOT.jar <path-to-log-file>
    ```


--------------------------------------------------------------------------------------------------------------------------------------------------------

## Usage
To use the Fair Billing application, provide the path to the log file as an argument. For example:

```bash
java -jar target/fair-billing-1.0-SNAPSHOT.jar ./fair-billing/session_log.txt
```

If no arguments are passed, the application will generate an error indicating that the log file path is required.

***Log File Format***

The log file should have entries in the following format:

 HH:mm:ss UserName Action

- HH:mm:ss is the timestamp.
- UserName is the user's identifier.
- Action can be either Start or End.

***Example:***

```
09:00:00 Alice Start
09:15:00 Bob Start
09:30:00 Alice End
10:00:00 Bob End
```

--------------------------------------------------------------------------------------------------------------------------------------------------------

## Features

- Calculate the total sessions and duration of each user's sessions.
- Generate detailed user session reports.
- Handle overlapping and multiple sessions.
- Flexible and easy-to-use configuration.


***Main Class: FairBillingApp:***

The FairBillingApp class is the entry point of the application. It handles the command-line arguments, initializes the necessary data structures, and invokes the service class to process the log file.

**Key Responsibilities**
- Parse command-line arguments to get the log file path.
- Initialize data structures to store session data.
- Call the FairBillingService to process the log file and generate reports.

----------------------------------------------------------------------------------------------------------------------------------------------

***Service Class: FairBillingService:***

The FairBillingService class contains the core logic for processing the log file, calculating session durations, and handling various session scenarios.

**Key Methods**
- processLogFile: Reads the log file and processes each line to calculate session durations.
- handleStartAction: Handles the "Start" action by recording the start time of a session.
- handleEndAction: Handles the "End" action by calculating the duration of a session.
- calculateRemainingSessions: Calculates the duration for any sessions that did not have an end time.
- printUserSessions: Prints the session data for each user.

--------------------------------------------------------------------------------------------------------------------------------------------------------



## Testing

Test cases for the FairBilling service can be found in the src/test/java/fairbilling/service directory. Here are some examples:

***Basic Functionality Test***
```Java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class FairBillingServiceTest {
    @Test
    public void testBasicFunctionality() {
        String filePath = "testLogs/BasicFunctionality.log";
        Map<String, Stack<Long>> sessions = new HashMap<>();
        Map<String, Integer> totalUserSessions = new HashMap<>();
        Map<String, Long> totalDuration = new HashMap<>();

        FairBillingService service = new FairBillingService();
        service.processLogFile(filePath, sessions, totalUserSessions, totalDuration);

        assertEquals(1,totalUserSessions.get("user1"));
        assertEquals(12L, totalDuration.get("user1"));
    }
}
```
For complete test cases, refer to the FairBillingServiceTest class in the test directory.

***Running Tests***

You can run the tests using Maven:

```bash
mvn test
```

--------------------------------------------------------------------------------------------------------------------------------------------------------------

## Error Handling

- If the log file path is not provided, the program will throw an IllegalArgumentException.
- If the log file is not found, a FileNotFoundException will be thrown.
- If there are errors reading the file, an IOException will be thrown.
- If there are errors parsing the time, a ParseException will be thrown.






   
