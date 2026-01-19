package com.task_3_4;

import com.task_8_2.interfaces.IFileManager;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileManager implements IFileManager {
    @Override
    public void importBooksFromCSVFile(String filename, List<Book> books) throws IOException {
        List<String[]> records = parseCSV(filename);

        for (String[] record : records) {
            boolean bookFound = false;

            for (Book b : books) {
                if (b.getName().equals(record[0])) {
                    b.setAuthor(record[1]);
                    b.setDescription(record[2]);
                    b.setPublished(new Date(record[3]));
                    b.setPrice(Double.parseDouble(record[4]));
                    b.setCountInStock(Integer.parseInt(record[5]));
                    bookFound = true;
                    break;
                }
            }

            if (!bookFound) {
                Book book = new Book(record[0],
                        record[1],
                        record[2],
                        new Date(record[3]),
                        Double.parseDouble(record[4]),
                        Integer.parseInt(record[5]));

                books.add(book);
            }
        }
    }

    @Override
    public void importOrdersFromCSVFile(String filename, List<Order> orders, List<Client> clients, List<Book> books) throws IOException {
        List<String[]> records = parseCSV(filename);

        for (String[] record : records) {
            boolean orderFound = false;

            boolean clientFound = false;
            Client foundedClient = null;
            for (Client c : clients) {
                if (c.getName().equals(record[3])) {
                    clientFound = true;
                    foundedClient = c;
                    break;
                }
            }
            if (!clientFound) {
                throw new RuntimeException();
            }

            for (Order o : orders) {
                if (o.getId() == Integer.parseInt(record[0])) {
                    o.setDiscount(Double.parseDouble(record[1]));
                    o.setExecutionDate(new Date(record[2]));
                    o.setClient(foundedClient);
                    orderFound = true;
                    break;
                }
            }

            if (!orderFound) {
                Order order = new Order(Double.parseDouble(record[1]),
                        new Date(record[2]),
                        foundedClient);

                if (record.length > 4) {
                    for (int i = 4; i < record.length; i++) {
                        for (Book b : books) {
                            if (b.getId() == Integer.parseInt(record[i])) {
                                order.addBook(b);
                                break;
                            }
                        }
                    }
                }

                foundedClient.addOrder(order);
                orders.add(order);
            }
        }
    }

    @Override
    public void importClientsFromCSVFile(String filename, List<Client> clients) throws IOException {
        List<String[]> records = parseCSV(filename);

        for (String[] record : records) {
            boolean clientFound = false;

            for (Client c : clients) {
                if (c.getName().equals(record[0])) {
                    c.setAge(Integer.parseInt(record[1]));
                    clientFound = true;
                    break;
                }
            }

            if (!clientFound) {
                Client client = new Client(record[0], Integer.parseInt(record[1]));
                clients.add(client);
            }
        }
    }

    @Override
    public void importRequestsFromCSVFile(String filename, List<Request> requests, List<Book> books) throws IOException {
        List<String[]> records = parseCSV(filename);

        for (String[] record : records) {
            boolean bookFound = false;
            Book foundedBook = null;

            for (Book b : books) {
                if (b.getId() == Integer.parseInt(record[0])) {
                    foundedBook = b;
                    bookFound = true;
                    break;
                }
            }

            if (bookFound) {
                Request request = new Request(foundedBook, Integer.parseInt(record[1]));
                request.setOpen(Boolean.parseBoolean(record[2]));
                requests.add(request);
            }
        }
    }

    @Override
    public void exportBooksIntoCSVFile(String fileName, List<Book> books) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (Book book : books) {
                writer.append(book.getName()).append(",");
                writer.append(book.getAuthor()).append(",");
                writer.append(book.getDescription()).append(",");
                writer.append(book.getPublished().toString()).append(",");
                writer.append(String.valueOf(book.getPrice())).append(",");
                writer.append(String.valueOf(book.getCountInStock())).append(",");

                List<Request> requests = book.getRequests();
                if (requests != null && !requests.isEmpty()) {
                    for (int i = 0; i < requests.size(); i++) {
                        if (i > 0) writer.append(",");
                        writer.append(String.valueOf(requests.get(i).getId()));
                    }
                }
                writer.append("\n");
            }
        }
    }

    @Override
    public void exportOrdersIntoCSVFile(String fileName, List<Order> orders) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (Order order : orders) {
                writer.append(String.valueOf(order.getId())).append(",");
                writer.append(String.valueOf(order.getDiscount())).append(",");
                writer.append(String.valueOf(order.getExecutionDate())).append(",");
                writer.append(String.valueOf(order.getClient().getName())).append(",");

                List<Book> books = order.getBooks();
                if (books != null && !books.isEmpty()) {
                    for (int i = 0; i < books.size(); i++) {
                        if (i > 0) writer.append(",");
                        writer.append(String.valueOf(books.get(i).getId()));
                    }
                }
                writer.append("\n");
            }
        }
    }

    @Override
    public void exportClientsIntoCSVFile(String fileName, List<Client> clients) throws IOException{
        try (FileWriter writer = new FileWriter(fileName)) {
            for (Client client : clients) {
                writer.append(client.getName()).append(",");
                writer.append(String.valueOf(client.getAge())).append(",");

                List<Order> orders = client.getOrders();
                if (orders != null && !orders.isEmpty()) {
                    for (int i = 0; i < orders.size(); i++) {
                        if (i > 0) writer.append(",");
                        writer.append(String.valueOf(orders.get(i).getId()));
                    }
                }
                writer.append("\n");
            }
        }
    }

    @Override
    public void exportRequestsIntoCSVFile(String fileName, List<Request> requests) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (Request request : requests) {
                writer.append(String.valueOf(request.getBook().getId())).append(",");
                writer.append(String.valueOf(request.getCount())).append(",");
                writer.append(String.valueOf(request.isOpen())).append(",");
                writer.append("\n");
            }
        }
    }

    private static List<String[]> parseCSV(String fileName) throws IOException {
        List<String[]> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                records.add(values);
            }
        }
        return records;
    }

//    @Override
//    public <T> void serializeObjects(List<T> objects, String entitiesCategory) throws IOException {
//        String fileName = entitiesCategory + ".json";
//        ObjectMapper objectMapper = new ObjectMapper();
//        File file = new File(fileName);
//        objectMapper.writeValue(file, objects);
//    }
//
//    @Override
//    public <T> List<T> deserializeObjects(Class<T> type, String fileName) throws IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, type);
//        return objectMapper.readValue(new File(fileName), listType);
//    }
}
