package com.task_13.DAOs;

import com.task_13.entities.BookEntity;
import com.task_13.entities.RequestEntity;
import com.task_3_4.Book;
import com.task_3_4.Request;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookDAO extends GenericDAO<Book, Long, BookEntity> {
    private RequestDAO requestDAO = new RequestDAO();

    public BookDAO() {
        super(BookEntity.class);
    }

    @Override
    protected Book mapFromEntityToModel(BookEntity entity) {
        Book model = new Book(
                (int) entity.getId(),
                entity.getName(),
                entity.getAuthor(),
                entity.getDescription(),
                Date.from(entity.getPublished().atZone(ZoneId.systemDefault()).toInstant()),
                entity.getCountInStock(),
                entity.getPrice()
        );
        List<Request> requests = new ArrayList<>();
        for (RequestEntity requestEntity : entity.getRequests()) {
            requests.add(requestDAO.mapFromEntityToModel(requestEntity));
        }
        model.setRequests(requests);
        return model;
    }

    @Override
    protected BookEntity mapFromModelToEntity(Book model) {
        BookEntity entity = new BookEntity(
                model.getId(),
                model.getName(),
                model.getAuthor(),
                model.getDescription(),
                model.getPublished().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                model.isInStock(),
                model.getCountInStock(),
                model.getPrice()
        );
        List<RequestEntity> requestEntities = new ArrayList<>();
        for (Request request : model.getRequests()) {
            requestEntities.add(requestDAO.mapFromModelToEntity(request));
        }
        entity.setRequests(requestEntities);
        return entity;
    }
}
