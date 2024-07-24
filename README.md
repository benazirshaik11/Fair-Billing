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
### without Maven
Please set JAVA_HOME environment variable
example:
```bash
set JAVA_HOME="path_to_jdks\1.8.xxx";
set PATH=%JAVA_HOME%\bin;%PATH%
```
Please create a target/classes directory
example:
```bash
mkdir target\classes
```
compile java files into target/classes
```bash
javac -d target/classes src/main/java/com/britishtelecom/fairbilling/models/*.java src/main/java/com/britishtelecom/fairbilling/service/*.java src/main/java/com/britishtelecom/fairbilling/*.java
```
cd into target/classes
```bash
cd target/classes
```
Run main class with file path as 1st argument
```bash
java com.britishtelecom.fairbilling.FairBillingApp path_to_file/session_log.txt
```
### Using Maven

1.***Build the project***
```bash
mvn clean install
```

2.***Run the application***
```bash
java -jar target/fair-billing-1.0-SNAPSHOT.jar <path-to-log-file>
```


--------------------------------------------------------------------------------------------------------------------------------------------------------

## Usage
To use the Fair Billing application, provide the path to the log file as an argument. For example:

```bash
java -jar target/fair-billing-1.0-SNAPSHOT.jar <path-to-log-file>
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
- Call the FairBillingService to process the log file and generate reports.
- Call the FairBillingService to print reports.

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

Test cases for the FairBilling service can be found in the src/test/java/com/britishtelecom/fairbilling/ directory.
For complete test cases, refer to the FairBillingServiceTest class in the test directory.

***Running Tests***

You can run the tests using Maven:

```bash
mvn test
```

--------------------------------------------------------------------------------------------------------------------------------------------------------------

## Error Handling

- If the log file path is not provided, the program will exit with status 1 with message "File path must be provided as an argument.".
- If the log file is not found, a FileNotFoundException will be thrown.
- If there are errors reading the file, an IOException will be thrown.
- If there are errors parsing the time, a ParseException will be thrown.






   
