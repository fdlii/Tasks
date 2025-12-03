package com.task_8_2.interfaces;

public interface IBeanConfigurator {
    <T> Class<? extends T> getInterfaceImplementation(Class<T> implementation);
}
