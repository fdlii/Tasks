package com.task_5_1;

import com.task_5_1.actions.booksActions.*;
import com.task_5_1.actions.ordersActions.*;
import com.task_5_1.actions.clientsActions.AddClientAction;
import com.task_5_1.actions.clientsActions.ExportClientsFromCSVFileAction;
import com.task_5_1.actions.clientsActions.GetClientsAction;
import com.task_5_1.actions.clientsActions.ImportClientsFromCSVFileAction;
import com.task_5_1.actions.requestsActions.ExportRequestsFromCSVFileAction;
import com.task_5_1.actions.requestsActions.GetBookRequestsAction;
import com.task_5_1.actions.requestsActions.ImportRequestsFromCSVFileAction;
import com.task_5_1.actions.requestsActions.MakeRequestAction;
import com.task_8_2.interfaces.IBookStore;

import java.util.Iterator;

public class Builder {
    private Menu rootMenu;

    public Menu getRootMenu() {
        return rootMenu;
    }

    public void buildMenu(IBookStore bookStore) {
        Menu menu = new Menu("Главное меню");

        menu.addItem("Работа с книгами", null, new Menu("Работа с книгами"));
        menu.addItem("Работа с клиентами",null, new Menu("Работа с клиентами"));
        menu.addItem("Работа с заказами",null, new Menu("Работа с заказами"));
        menu.addItem("Работа с запросами",null, new Menu("Работа с запросами"));

        Iterator<MenuItem> menuIt = menu.menuItems.iterator();
        Menu innerMenu1 = menuIt.next().getNextMenu();

        innerMenu1.addItem("Добавление книги", new AddBookAction(bookStore), null);
        innerMenu1.addItem("Добавление книги на склад", new AddInStockAction(bookStore), null);
        innerMenu1.addItem("Удаление книги со склада", new DebitFromStockAction(bookStore), null);
        innerMenu1.addItem("Удаление книги", new DeleteBookAction(bookStore), null);
        innerMenu1.addItem("Получение всех книг", new GetBooksAction(bookStore), null);
        innerMenu1.addItem("Получение залежавшихся книг", new GetStaledBooksAction(bookStore), null);
        innerMenu1.addItem("Импорт книг", new ImportBooksFromCSVFileAction(bookStore), null);
        innerMenu1.addItem("Экспорт книг", new ExportBooksFromCSVFileAction(bookStore), null);

        Menu innerMenu2 = menuIt.next().getNextMenu();

        innerMenu2.addItem("Добавление клиента", new AddClientAction(bookStore), null);
        innerMenu2.addItem("Получение всех клиентов", new GetClientsAction(bookStore), null);
        innerMenu2.addItem("Импорт клиентов", new ImportClientsFromCSVFileAction(bookStore), null);
        innerMenu2.addItem("Экспорт клиентов", new ExportClientsFromCSVFileAction(bookStore), null);

        Menu innerMenu3 = menuIt.next().getNextMenu();

        innerMenu3.addItem("Отмена заказа", new CancelOrderAction(bookStore), null);
        innerMenu3.addItem("Выполнение заказа", new CompleteOrderAction(bookStore), null);
        innerMenu3.addItem("Создание заказа", new CreateOrderAction(bookStore), null);
        innerMenu3.addItem("Получение числа выполненных заказов за промежуток времени", new GetCompletedOrdersCountForTimeSpanAction(bookStore), null);
        innerMenu3.addItem("Получение всех заказов за промежуток времени", new GetCompletedOrdersForTimeSpanAction(bookStore), null);
        innerMenu3.addItem("Получение суммы дохода за промежуток времени", new GetEarnedFundsForTimeSpanAction(bookStore), null);
        innerMenu3.addItem("Получение заказа по идентификатору", new GetOrderByIdAction(bookStore), null);
        innerMenu3.addItem("Получение всех заказов", new GetOrdersAction(bookStore), null);
        innerMenu3.addItem("Импорт заказов", new ImportOrdersFromCSVFileAction(bookStore), null);
        innerMenu3.addItem("Экспорт заказов", new ExportOrdersFromCSVFileAction(bookStore), null);

        Menu innerMenu4 = menuIt.next().getNextMenu();

        innerMenu4.addItem("Получение запросов у книги", new GetBookRequestsAction(bookStore), null);
        innerMenu4.addItem("Создание запроса", new MakeRequestAction(bookStore), null);
        innerMenu4.addItem("Импорт запросов", new ImportRequestsFromCSVFileAction(bookStore), null);
        innerMenu4.addItem("Экспорт запросов", new ExportRequestsFromCSVFileAction(bookStore), null);

        rootMenu = menu;
    }
}
