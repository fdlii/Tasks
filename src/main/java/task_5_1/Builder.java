package task_5_1;

import task_5_1.actions.booksActions.*;
import task_5_1.actions.clientsActions.AddClientAction;
import task_5_1.actions.clientsActions.ExportClientsFromCSVFileAction;
import task_5_1.actions.clientsActions.GetClientsAction;
import task_5_1.actions.clientsActions.ImportClientsFromCSVFileAction;
import task_5_1.actions.ordersActions.*;
import task_5_1.actions.requestsActions.ExportRequestsFromCSVFileAction;
import task_5_1.actions.requestsActions.GetBookRequestsAction;
import task_5_1.actions.requestsActions.ImportRequestsFromCSVFileAction;
import task_5_1.actions.requestsActions.MakeRequestAction;

import java.util.Iterator;

public class Builder {
    private Menu rootMenu;

    public Menu getRootMenu() {
        return rootMenu;
    }

    public void buildMenu() {
        Menu menu = new Menu("Главное меню");

        menu.addItem("Работа с книгами", null, new Menu("Работа с книгами"));
        menu.addItem("Работа с клиентами",null, new Menu("Работа с клиентами"));
        menu.addItem("Работа с заказами",null, new Menu("Работа с заказами"));
        menu.addItem("Работа с запросами",null, new Menu("Работа с запросами"));

        Iterator<MenuItem> menuIt = menu.menuItems.iterator();
        Menu innerMenu1 = menuIt.next().getNextMenu();

        innerMenu1.addItem("Добавление книги", new AddBookAction(), null);
        innerMenu1.addItem("Добавление книги на склад", new AddInStockAction(), null);
        innerMenu1.addItem("Удаление книги со склада", new DebitFromStockAction(), null);
        innerMenu1.addItem("Удаление книги", new DeleteBookAction(), null);
        innerMenu1.addItem("Получение всех книг", new GetBooksAction(), null);
        innerMenu1.addItem("Получение залежавшихся книг", new GetStaledBooksAction(), null);
        innerMenu1.addItem("Импорт книг", new ImportBooksFromCSVFileAction(), null);
        innerMenu1.addItem("Экспорт книг", new ExportBooksFromCSVFileAction(), null);

        Menu innerMenu2 = menuIt.next().getNextMenu();

        innerMenu2.addItem("Добавление клиента", new AddClientAction(), null);
        innerMenu2.addItem("Получение всех клиентов", new GetClientsAction(), null);
        innerMenu2.addItem("Импорт клиентов", new ImportClientsFromCSVFileAction(), null);
        innerMenu2.addItem("Экспорт клиентов", new ExportClientsFromCSVFileAction(), null);

        Menu innerMenu3 = menuIt.next().getNextMenu();

        innerMenu3.addItem("Отмена заказа", new CancelOrderAction(), null);
        innerMenu3.addItem("Выполнение заказа", new CompleteOrderAction(), null);
        innerMenu3.addItem("Создание заказа", new CreateOrderAction(), null);
        innerMenu3.addItem("Получение числа заказов за промежуток времени", new GetCompletedOrdersCountForTimeSpanAction(), null);
        innerMenu3.addItem("Получение всех заказов за промежуток времени", new GetCompletedOrdersForTimeSpanAction(), null);
        innerMenu3.addItem("Получение суммы дохода за промежуток времени", new GetEarnedFundsForTimeSpanAction(), null);
        innerMenu3.addItem("Получение заказа по идентификатору", new GetOrderByIdAction(), null);
        innerMenu3.addItem("Получение всех заказов", new GetOrdersAction(), null);
        innerMenu3.addItem("Импорт заказов", new ImportOrdersFromCSVFileAction(), null);
        innerMenu3.addItem("Экспорт заказов", new ExportOrdersFromCSVFileAction(), null);

        Menu innerMenu4 = menuIt.next().getNextMenu();

        innerMenu4.addItem("Получение запросов у книги", new GetBookRequestsAction(), null);
        innerMenu4.addItem("Создание запроса", new MakeRequestAction(), null);
        innerMenu4.addItem("Импорт запросов", new ImportRequestsFromCSVFileAction(), null);
        innerMenu4.addItem("Экспорт запросов", new ExportRequestsFromCSVFileAction(), null);

        rootMenu = menu;
    }
}
