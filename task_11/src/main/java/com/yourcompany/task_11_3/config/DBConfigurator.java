package com.yourcompany.task_11_3.config;

import com.yourcompany.task_8_1.ConfigProperty;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

public class DBConfigurator {
    private DBConfiguration configuration;
    private Properties props = new Properties();

    public DBConfigurator() {
        DBConfiguration conf = new DBConfiguration();
        try {
            for (Field field : conf.getClass().getFields()) {
                ConfigProperty configProperty = field.getAnnotation(ConfigProperty.class);
                props.load(new FileReader(configProperty.configFileName()));
                Object property;
                if (!configProperty.propertyName().isEmpty()) {
                    property = props.getProperty(configProperty.propertyName());
                }
                else {
                    property = props.getProperty(conf.getClass().getName() + "." + field.getName());
                }
                property = parseValue(property, configProperty);
                field.set(conf, property);
            }
        }
        catch (IOException ex) {
            System.out.println("Не удалось загрузить файл конфигурации.");
        }
        catch (IllegalAccessException e) {
            System.out.println("Не удалось выполнить конфигурацию. Проверьте, чтобы поля имели публичный доступ.");
        }
        finally {
            configuration = conf;
        }
    }

    public DBConfiguration getConfiguration() {
        return configuration;
    }

    private Object parseValue(Object object, ConfigProperty configProperty) {
        return switch (configProperty.type()) {
            case "int" -> Integer.parseInt((String) object);
            case "boolean" -> Boolean.parseBoolean((String) object);
            case "double" -> Double.parseDouble((String) object);
            default -> (String) object;
        };
    }
}
