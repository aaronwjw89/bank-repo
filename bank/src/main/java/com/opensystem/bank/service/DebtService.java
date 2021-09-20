package com.opensystem.bank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opensystem.bank.model.Debt;
import com.opensystem.bank.repository.DebtRepository;

@Service
public class DebtService {

	@Autowired
	private DebtRepository debtRepository;
	
	public List<Debt> findByCreditor(int id)
	{
		return debtRepository.findByCreditor(id);
	}
	
	public List<Debt> findByDebtor(int id)
	{
		return debtRepository.findByDebtor(id);
	}
	
	public Debt save(Debt debt)
	{
		return debtRepository.save(debt);
	}
	
	public void delete(Debt debt)
	{
		debtRepository.delete(debt);
	}
	
	public Debt findPayeeDebt(int debtor, int creditor) {
		return debtRepository.findByDebtorAndCreditor(debtor, creditor);
	}
}
