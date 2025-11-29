package com.task_5_1.actions.requestsActions;

import com.task_5_1.actions.IAction;
import com.task_3_4.BookStore;
import com.task_6_2.BookExeption;
import com.task_8_2.annotations.Inject;
import com.task_8_2.interfaces.IBookStore;

import java.util.Scanner;

public class MakeRequestAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    IBookStore bookStore;

    public MakeRequestAction(IBookStore bookStore) {
        this.bookStore = bookStore;
    }

    @Override
    public void execute() throws BookExeption {
        try {
            System.out.println("Введите название книги: ");
            String name = scanner.nextLine();
            System.out.println("Введите число книг на запрос: ");
            int count = scanner.nextInt();
            scanner.nextLine();
            boolean flag = bookStore.makeRequest(name, count);
        }
        catch (Exception exception) {
            throw new BookExeption("Введено невалидное значение книг на запрос. Попробуйте снова.");
        }
    }
}
