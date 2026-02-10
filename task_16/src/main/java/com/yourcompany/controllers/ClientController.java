package com.yourcompany.controllers;

import com.yourcompany.DTO.ClientDTO;
import com.yourcompany.exceptions.ClientNotFoundException;
import com.yourcompany.mappers.ClientMapper;
import com.yourcompany.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    ClientService clientService;
    @Autowired
    ClientMapper clientMapper;

    @GetMapping
    public List<ClientDTO> getClients() throws ClientNotFoundException {
        return clientMapper.toDTOList(clientService.getClients());
    }

    @PostMapping
    public ClientDTO addClient(@RequestBody ClientDTO clientDTO) {
        clientService.addClient(clientDTO.getName(), clientDTO.getAge());
        return clientDTO;
    }

    @GetMapping("/import")
    public void importFromCSV() throws IOException {
        clientService.importClientsFromCSVFile("task_6/src/main/java/com/yourcompany/task_6_1/Clients.csv");
    }

    @GetMapping("/export")
    public void exportInCSV() throws IOException, ClientNotFoundException {
        clientService.exportClientsIntoCSVFile("task_6/src/main/java/com/yourcompany/task_6_1/Clients.csv");
    }
}
