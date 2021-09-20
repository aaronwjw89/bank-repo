package com.opensystem.bank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.opensystem.bank.model.Debt;

public interface DebtRepository extends JpaRepository<Debt, Integer> {

	List<Debt> findByCreditor(int id);
	List<Debt> findByDebtor(int id);
	Debt findByDebtorAndCreditor(int debtor, int creditor);
}
