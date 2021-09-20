package com.opensystem.bank.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opensystem.bank.model.Client;
import com.opensystem.bank.model.Debt;
import com.opensystem.bank.service.ClientService;
import com.opensystem.bank.service.DebtService;
import com.opensystem.bank.utils.Constants;

@CrossOrigin
@RestController
@RequestMapping("v1/home")
public class BankController {

	@Autowired
	private ClientService clientService;
	
	@Autowired
	private DebtService debtService;
	
	private String loggedInClientName;
	
	@GetMapping("/{command}")
	public String parseCommand(@PathVariable String command)
	{
		String output = "";
		
		if (command.startsWith(Constants.LOGIN_COMMAND))
		{
			output = parseLoginCommand(command);
		}
		else if (command.startsWith(Constants.TOPUP_COMMAND))
		{
			output = parseTopupCommmand(command);
		}
		else if (command.startsWith(Constants.PAY_COMMAND))
		{
			output = parsePayCommmand(command);
		}
		else
		{
			output = Constants.INVALID_COMMAND;
		}
		
		return output;
	}
	
	public String processLogin(String clientName)
	{
		StringBuffer stringBuffer = new StringBuffer();
		
		// Retrieve client info
		Client client = clientService.findByName(clientName);
		
		if (client == null)
		{
			// Create client
			client = new Client(clientName, Constants.STARTING_BALANCE);
			client = clientService.save(client);
		}
		
		stringBuffer.append("Hello " + client.getName() + ".\n");
		loggedInClientName = client.getName();
		
		// Check if anyone owes user money
		List<Debt> creditorList = debtService.findByCreditor(client.getId());
		
		if (creditorList != null && creditorList.size() > 0)
		{
			for (Debt debt: creditorList)
			{
				// Retrieve Debtor name
				Client debtor = clientService.getById(debt.getDebtor());
				stringBuffer.append("Owing " + debt.getAmount() + " from " + debtor.getName() + ".\n");
			}
		}
		
		// Check current balance
		stringBuffer.append("Your balance is " + client.getBalance() + ".\n");
		
		// Check if user owes anyone money
		List<Debt> debtorList = debtService.findByDebtor(client.getId());
		
		if (debtorList != null && debtorList.size() > 0)
		{
			for (Debt debt: debtorList)
			{
				Client creditor = clientService.getById(debt.getCreditor());
				stringBuffer.append("Owing " + debt.getAmount() + " to " + creditor.getName() + ".\n");
			}
		}
		
		System.out.println(stringBuffer.toString());
		return stringBuffer.toString();
	}
	
	public String processTopup(String amount)
	{
		StringBuffer stringBuffer = new StringBuffer();
		Client client = null;
		List<Debt> debtRemovalList = new ArrayList<Debt>();
		
		try
		{
			// Validations
			if (loggedInClientName == null)
				stringBuffer.append(Constants.INVALID_LOGIN);
				
			int topupAmount = Integer.parseInt(amount);
			
			if (topupAmount <= 0)
				stringBuffer.append(Constants.INVALID_TOPUP_AMOUNT);
			else
			{
				client = clientService.findByName(loggedInClientName);
				
				if (client != null)
				{
					// Check if user owes anyone money
					List<Debt> debtorList = debtService.findByDebtor(client.getId());
					
					if (debtorList != null && debtorList.size() > 0)
					{
						for (Debt debt: debtorList)
						{
							Client creditor = clientService.getById(debt.getCreditor());
							
							// Able to fully pay off debt
							if (topupAmount >= debt.getAmount())
							{
								topupAmount -= debt.getAmount();
								debtRemovalList.add(debt);
								stringBuffer.append("Transferred " + debt.getAmount() + " to " + creditor.getName() + ".\n");
								// Update creditor balance
								creditor.setBalance(creditor.getBalance() + debt.getAmount());
								clientService.save(creditor);
							}
							// Not enough to clear debt, pay off outstanding debt with top up amount first
							else if (topupAmount < debt.getAmount() && topupAmount != 0)
							{
								debt.setAmount(debt.getAmount() - topupAmount);
								debt = debtService.save(debt);
								stringBuffer.append("Transferred " + topupAmount + " to " + creditor.getName() + ".\n");
								// Update creditor balance
								creditor.setBalance(creditor.getBalance() + topupAmount);
								clientService.save(creditor);
								topupAmount = 0;
							}
						}
						
						for (Debt debt: debtRemovalList)
						{
							debtService.delete(debt);
						}
					}
					
					client.setBalance(client.getBalance() + topupAmount);
					client = clientService.save(client);
					stringBuffer.append("Your balance is " + client.getBalance() + ".\n");
					
					// Check if user owes anyone money
					List<Debt> debtorListLatest = debtService.findByDebtor(client.getId());
					
					if (debtorListLatest != null && debtorListLatest.size() > 0)
					{
						for (Debt debt: debtorListLatest)
						{
							Client creditor = clientService.getById(debt.getCreditor());
							stringBuffer.append("Owing " + debt.getAmount() + " to " + creditor.getName() + ".\n");
						}
					}
				}
			}
		}
		catch (NumberFormatException nfe)
		{
			stringBuffer.append(Constants.INVALID_TOPUP_AMOUNT);
		}
		
		System.out.println(stringBuffer.toString());
		return stringBuffer.toString();
	}
	
	public String processPay(String toClient, String amount)
	{
		StringBuffer stringBuffer = new StringBuffer();
		int payAmount;
		
		// Validations
		if (loggedInClientName == null)
			return Constants.INVALID_LOGIN;

		try
		{
			payAmount = Integer.parseInt(amount);
			
			if (payAmount <= 0)
				return Constants.INVALID_PAYMENT_AMOUNT;
		}
		catch (NumberFormatException nfe)
		{
			 return Constants.INVALID_PAYMENT_AMOUNT;
		}
		
		Client payer = clientService.findByName(loggedInClientName);
		Client payee = clientService.findByName(toClient);
		
		if (payee == null)
			return "Client does not exist!";

		if (Objects.equals(payer, payee))
			return "Unable to pay yourself";
		
		// Check if the person that user is paying, owes the user money
		Debt debt = debtService.findPayeeDebt(payee.getId(), payer.getId());
		
		if (debt != null)
		{
			// Reduce the debt amount
			if (payAmount < debt.getAmount())
			{
				debt.setAmount(debt.getAmount() - payAmount);
				debt = debtService.save(debt);
				stringBuffer.append("Owing " + debt.getAmount() + " from " + payee.getName() + ".\n");
			}
			// Enough to clear payee debt
			else //if (payAmount >= debt.getAmount())
			{
				debtService.delete(debt);
				payAmount -= debt.getAmount();
				// Update own balance with remaining amount
				payer.setBalance(payer.getBalance() - payAmount);
				payer = clientService.save(payer);
			}
			
			stringBuffer.append("Your balance is " + payer.getBalance() + ".\n");
		}
		else
		{
			// Payer will be in debt
			if (payAmount >= payer.getBalance())
			{
				stringBuffer.append("Transferred " + payer.getBalance() + " to " + payee.getName() + ".\n");
				// Update payee balance
				payee.setBalance(payee.getBalance() + payer.getBalance());
				
				int debtAmount = payAmount - payer.getBalance();
				payer.setBalance(Constants.STARTING_BALANCE);
				payer = clientService.save(payer);
				stringBuffer.append("Your balance is " + payer.getBalance() + ".\n");
				
				// Check if payer owes payee money
				Debt checkDebt = debtService.findPayeeDebt(payer.getId(), payee.getId());
				
				if (checkDebt != null)
				{
					checkDebt.setAmount(checkDebt.getAmount() + debtAmount);
					checkDebt = debtService.save(checkDebt);
					stringBuffer.append("Owing " + checkDebt.getAmount() + " to " + payee.getName() + ".\n");
				}
				else
				{
					Debt newDebt = new Debt(debtAmount, payer.getId(), payee.getId());
					debtService.save(newDebt);
					stringBuffer.append("Owing " + debtAmount + " to " + payee.getName() + ".\n");
				}
			}
			else
			{
				stringBuffer.append("Transferred " + payAmount + " to " + payee.getName() + ".\n");
				payer.setBalance(payer.getBalance() - payAmount);
				payer = clientService.save(payer);
				stringBuffer.append("Your balance is " + payer.getBalance() + ".\n");
				// Update payee balance
				payee.setBalance(payee.getBalance() + payAmount);
			}
			payee = clientService.save(payee);
		}
		
		System.out.println(stringBuffer.toString());
		return stringBuffer.toString();
	}
	
	private String parseLoginCommand(String command)
	{
		String[] tokens = command.split(Constants.DELIMITER_SPACE);
		
		if (tokens.length != Constants.LOGIN_ARGUMENT_LENGTH)
		{
			return Constants.INVALID_LOGIN_ARGUMENT_LENGTH;
		}
		else
		{
			return processLogin(tokens[1]);
		}
	}
	
	private String parseTopupCommmand(String command)
	{
		String[] tokens = command.split(Constants.DELIMITER_SPACE);
		
		if (tokens.length != Constants.TOPUP_ARGUMENT_LENGTH)
		{
			return Constants.INVALID_TOPUP_ARGUMENT_LENGTH;
		}
		else
		{
			return processTopup(tokens[1]);
		}
	}
	
	private String parsePayCommmand(String command)
	{
		String[] tokens = command.split(Constants.DELIMITER_SPACE);
		
		if (tokens.length != Constants.PAY_ARGUMENT_LENGTH)
		{
			return Constants.INVALID_PAY_ARGUMENT_LENGTH;
		}
		else
		{
			return processPay(tokens[1], tokens[2]);
		}
	}
}
