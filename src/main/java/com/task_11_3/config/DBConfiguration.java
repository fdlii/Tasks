package com.task_11_3.config;

import com.task_8_1.ConfigProperty;

public class DBConfiguration {
    @ConfigProperty(configFileName = "src/main/java/com/task_11_3/config/DBCfg.properties", propertyName = "BooksDBName", type = "String")
    public String booksTableName;
    @ConfigProperty(configFileName = "src/main/java/com/task_11_3/config/DBCfg.properties", propertyName = "RequestsDBName", type = "String")
    public String requestsTableName;
    @ConfigProperty(configFileName = "src/main/java/com/task_11_3/config/DBCfg.properties", propertyName = "ClientsDBName", type = "String")
    public String clientsTableName;
    @ConfigProperty(configFileName = "src/main/java/com/task_11_3/config/DBCfg.properties", propertyName = "OrdersDBName", type = "String")
    public String ordersTableName;
    @ConfigProperty(configFileName = "src/main/java/com/task_11_3/config/DBCfg.properties", propertyName = "MtoMDBName", type = "String")
    public String MMsTableName;
}
