package com.opensystem.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.opensystem.bank.model.Client;

public interface ClientRepository extends JpaRepository<Client, Integer> {
	
	Client findByName(String name);
//	List<Client> findByName(String name);
}
