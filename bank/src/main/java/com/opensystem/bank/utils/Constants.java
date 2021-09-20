package com.opensystem.bank.utils;

public class Constants {

	public static final int STARTING_BALANCE = 0;
	
	public static final String LOGIN_COMMAND = "login";
	public static final String TOPUP_COMMAND = "topup";
	public static final String PAY_COMMAND = "pay";
	
	public static final String DELIMITER_SPACE = " ";
	
	public static final int LOGIN_ARGUMENT_LENGTH = 2;
	public static final int TOPUP_ARGUMENT_LENGTH = 2;
	public static final int PAY_ARGUMENT_LENGTH = 3;
	
	public static final String INVALID_COMMAND = "Please use the following commands: (login, topup, pay)";
	public static final String INVALID_LOGIN = "Please ensure that you are logged in to a valid account.";
	public static final String INVALID_TOPUP_AMOUNT = "Please ensure that topup amount is a positive integer.";
	public static final String INVALID_PAYMENT_AMOUNT = "Please ensure that payment amount is a positive integer.";
	public static final String INVALID_LOGIN_ARGUMENT_LENGTH = "Invalid number of arguments for login command.";
	public static final String INVALID_TOPUP_ARGUMENT_LENGTH = "Invalid number of arguments for topup command.";
	public static final String INVALID_PAY_ARGUMENT_LENGTH = "Invalid number of arguments for pay command.";
}
