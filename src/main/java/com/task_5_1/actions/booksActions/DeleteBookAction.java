package com.task_5_1.actions.booksActions;

import com.task_5_1.actions.IAction;
import com.task_3_4.BookStore;
import com.task_6_2.BookExeption;
import com.task_8_2.annotations.Inject;
import com.task_8_2.interfaces.IBookStore;

import java.util.Scanner;

public class DeleteBookAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    IBookStore bookStore;

    public DeleteBookAction(IBookStore bookStore) {
        this.bookStore = bookStore;
    }

    @Override
    public void execute() {
            System.out.println("Введите название книги: ");
            String name = scanner.nextLine();
            bookStore.deleteBook(name);
    }
}
