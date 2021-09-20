package com.opensystem.bank.model;

import javax.persistence.*;

@Entity
@Table(name = "Debt")
public class Debt {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name = "amount")
	private int amount;
	
	@Column(name = "debtor")
	private int debtor;
	
	@Column(name = "creditor")
	private int creditor;

	public Debt()
	{
		
	}
	
	public Debt(int amount, int debtor, int creditor)
	{
		this.amount = amount;
		this.debtor = debtor;
		this.creditor = creditor;
	}
	
	public int getId() {
		return id;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getDebtor() {
		return debtor;
	}

	public void setDebtor(int debtor) {
		this.debtor = debtor;
	}

	public int getCreditor() {
		return creditor;
	}

	public void setCreditor(int creditor) {
		this.creditor = creditor;
	}

	public String toString()
	{
		return "Debt ID: " + getId() + "\n" +
				"Amount: " + getAmount() + "\n" +
				"Debtor: " + getDebtor() + "\n" +
				"Creditor: " + getCreditor();
	}
}
