package com.opensystem.bank;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import com.opensystem.bank.model.Client;
import com.opensystem.bank.service.ClientService;
import com.opensystem.bank.utils.Constants;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class BankApplicationTests {

	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private ClientService clientService;

	@Test
	void testParseCommand()
	{
		String url = "http://localhost:" + port + "/v1/home/";
		String command = "Test";
		String output = restTemplate.getForObject(url.concat(command), String.class);
		assertEquals(Constants.INVALID_COMMAND, output);
		
		command = "login";
		output = restTemplate.getForObject(url.concat(command), String.class);
		assertEquals(Constants.INVALID_LOGIN_ARGUMENT_LENGTH, output);
		
		command = "topup";
		output = restTemplate.getForObject(url.concat(command), String.class);
		assertEquals(Constants.INVALID_TOPUP_ARGUMENT_LENGTH, output);
		
		command = "pay";
		output = restTemplate.getForObject(url.concat(command), String.class);
		assertEquals(Constants.INVALID_PAY_ARGUMENT_LENGTH, output);
	}
	
	@Test
	void testLogin()
	{
		String url = "http://localhost:"+port+"/v1/home/";
		String command = "login Alice";
		String output = restTemplate.getForObject(url.concat(command), String.class);
		assertEquals("Hello Alice.\nYour balance is 0.\n", output);
		
		command = "login Bob";
		output = restTemplate.getForObject(url.concat(command), String.class);
		assertEquals("Hello Bob.\nYour balance is 0.\n", output);
	}
	
	@Test
	void testTopup()
	{
		String url = "http://localhost:"+port+"/v1/home/";
		String command = "topup 100";
		String output = restTemplate.getForObject(url.concat(command), String.class);
		assertEquals("Your balance is 100.\n", output);
	}
	
	@Test
	void testClientService()
	{
		Client client = clientService.findByName("Alice");
		assertEquals("Alice", client.getName());
		
		client = clientService.findByName("Bob");
		assertEquals("Bob", client.getName());
	}
}
