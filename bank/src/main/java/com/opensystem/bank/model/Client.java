package com.opensystem.bank.model;

import javax.persistence.*;

@Entity
@Table(name = "Client")
public class Client {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "balance")
	private int balance;
	
	public Client()
	{
		
	}
	
	public Client(String name, int balance)
	{
		this.name = name;
		this.balance = balance;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getBalance() {
		return balance;
	}
	
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	public String toString()
	{
		return "Client ID: " + getId() + "\n" +
				"Name: " + getName() + "\n" +
				"Balance: " + getBalance();
	}
}
