package task_8_1;

public class Configuration {
    @ConfigProperty(configFileName = "src/main/java/task_7_1/Config.properties", propertyName = "StaledBooksDeadline", type = "int")
    public int staledBooksDeadline;
    @ConfigProperty(configFileName = "src/main/java/task_7_1/Config.properties", propertyName = "CanCompleteRequest", type = "boolean")
    public boolean canCompleteRequest;
}
