package com.opensystem.bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opensystem.bank.model.Client;
import com.opensystem.bank.repository.ClientRepository;

@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;
	
	public Client findByName(String name)
	{
		return clientRepository.findByName(name);
	}
	
	public Client save(Client client)
	{
		return clientRepository.save(client);
	}
	
	public Client getById(int id)
	{
		return clientRepository.getById(id);
	}
}
