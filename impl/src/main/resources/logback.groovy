appender("Console out", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{20} - %msg%n"
    }
}

appender("File out", RollingFileAppender) {
    file = "logs/logback.log"
    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = "logs/archived/logback-%d{yyyy-MM-dd}.zip"
        maxHistory = 20
        totalSizeCap = "5KB"
    }

    encoder(PatternLayoutEncoder) {
        pattern = "%d{dd.MM.yyyy HH:mm:ss} [%thread] %-5level - %msg%n"
    }
}

root(INFO, ["Console out", "File out"])