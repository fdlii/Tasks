package actions.clientsActions;

import actions.IAction;
import task_3_4.BookStore;
import task_3_4.Client;

public class GetClientsAction implements IAction {
    @Override
    public void execute() {
        System.out.println("Список клиентов: ");
        for (Client client : BookStore.getClients()) {
            System.out.printf("ФИО : %s, возраст: %d.%n", client.getName(), client.getAge());
        }
    }
}
