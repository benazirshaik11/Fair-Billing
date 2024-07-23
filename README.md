# FairBilling

FairBilling is a Java-based utility designed to process log files and calculate the total session durations for users. It reads log files containing user session start and end times and calculates the total time each user spent in their sessions.

## Table of Contents

- [Getting Started](#getting-started)
- [Usage](#usage)
- [Testing](#testing)
- [Error Handling](#error-handling)

--------------------------------------------------------------------------------------------------------------------------------------------------------

## Getting Started

### Prerequisites

- Java 8 or higher
- Maven (optional, for dependency management)

### Installation

1. ***Clone the repository:***

   ```bash
   git clone https://github.com/benazirshaik11/Fair-Billing.git
   cd Fair-Billing

2. ***Compile the project:***

If you are using Maven, you can compile the project with:

```bash
mvn clean install
```

Alternatively, you can compile the project manually:

```bash
javac -d target/classes src/main/java/fairbilling/service/FairBillingApp.java
```

--------------------------------------------------------------------------------------------------------------------------------------------------------

## Usage

To run the program, you need to provide the path to the log file as a command-line argument.

***Command Line***

```bash
java -cp target/classes fairbilling.service.FairBillingApp path/to/logfile.log
```

***Example***

```bash
java -cp target/classes fairbilling.service.FairBillingApp ./fair-billing/session_log.txt
```

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

```
- If the log file path is not provided, the program will throw an IllegalArgumentException.
- If the log file is not found, a FileNotFoundException will be thrown.
- If there are errors reading the file, an IOException will be thrown.
- If there are errors parsing the time, a ParseException will be thrown.
```






   
