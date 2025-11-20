package task_5_1.actions;

import task_6_2.BookExeption;
import task_6_2.ClientExeption;
import task_6_2.OrderExeption;

import java.io.IOException;

public interface IAction {
    void execute() throws IOException, BookExeption, ClientExeption, OrderExeption;
}
