package com.yourcompany.task_8_1;

public class Configuration {
    @ConfigProperty(configFileName = "src/main/java/com/task_7_1/Config.properties", propertyName = "StaledBooksDeadline", type = "int")
    public int staledBooksDeadline;
    @ConfigProperty(configFileName = "src/main/java/com/task_7_1/Config.properties", propertyName = "CanCompleteRequest", type = "boolean")
    public boolean canCompleteRequest;
}
