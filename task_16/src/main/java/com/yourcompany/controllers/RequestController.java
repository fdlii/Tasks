package com.yourcompany.controllers;

import com.yourcompany.DTO.RequestDTO;
import com.yourcompany.exceptions.BookNotFoundException;
import com.yourcompany.exceptions.RequestNotFoundException;
import com.yourcompany.mappers.RequestMapper;
import com.yourcompany.services.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/request")
public class RequestController {
    @Autowired
    RequestService requestService;
    @Autowired
    RequestMapper requestMapper;

    @GetMapping
    public List<RequestDTO> getRequests() throws RequestNotFoundException {
        return requestMapper.toDTOList(requestService.getRequests());
    }

    @GetMapping("/{book_name}")
    public List<RequestDTO> getBookRequests(@PathVariable("book_name") String bookName) throws BookNotFoundException, RequestNotFoundException {
        return requestMapper.toDTOList(requestService.getBookRequests(bookName));
    }

    @PostMapping
    public RequestDTO makeRequest(@RequestBody RequestDTO requestDTO) throws BookNotFoundException {
        requestService.makeRequest(requestDTO.getBookName(), requestDTO.getCount());
        return requestDTO;
    }

    @GetMapping("/import")
    public void importFromCSV() throws IOException, BookNotFoundException {
        requestService.importRequestsFromCSVFile("task_6/src/main/java/com/yourcompany/task_6_1/Requests.csv");
    }

    @GetMapping("/export")
    public void exportInCSV() throws IOException, RequestNotFoundException {
        requestService.exportRequestsIntoCSVFile("task_6/src/main/java/com/yourcompany/task_6_1/Requests.csv");
    }
}
