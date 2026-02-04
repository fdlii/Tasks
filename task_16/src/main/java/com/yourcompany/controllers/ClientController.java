package com.yourcompany.controllers;

import com.yourcompany.DTO.ClientDTO;
import com.yourcompany.mappers.ClientMapper;
import com.yourcompany.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    ClientService clientService;
    @Autowired
    ClientMapper clientMapper;
    @GetMapping
    public List<ClientDTO> getClients() {
        return clientMapper.toDTOList(clientService.getClients());
    }
}
