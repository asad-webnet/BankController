package com.rest.bankservice;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
class BankserviceApplicationTests {

	@BeforeAll
	public static void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 8081;
	}
	@Test
	public void testCreatingAnAccount() {
		given()
				.param("accountNumber", 123)
				.param("accountHolder", "M asad")
				.post("/accounts")
				.then()
				.statusCode(HttpStatus.CREATED.value());

		given()
				.pathParam("accountNumber", 123)
				.delete("/accounts/{accountNumber}")
				.then()
				.statusCode(HttpStatus.OK.value());
	}

	@Test
	public void testDepositWithoutAccountExistence() {
		given()
				.param("accountNumber", 123)
				.param("amount", 50)
				.post("/accounts/deposit")
				.then()
				.statusCode(HttpStatus.NOT_FOUND.value());
	}

	@Test
	public void testWithdrawWithNoAmountInAccount() {

		// first create account
		given()
				.param("accountNumber", 123)
				.param("accountHolder", "M asad")
				.post("/accounts")
				.then()
				.statusCode(HttpStatus.CREATED.value());

		given()
				.param("accountNumber", 123)
				.param("amount", 50)
				.post("/accounts/withdraw")
				.then()
				.statusCode(HttpStatus.BAD_REQUEST.value());

		given()
				.pathParam("accountNumber", 123)
				.delete("/accounts/{accountNumber}")
				.then()
				.statusCode(HttpStatus.OK.value());
	}

	@Test
	public void testGetAccountByNumberWithoutExistence() {
		given()
				.pathParam("accountNumber", 999)
				.get("/accounts/{accountNumber}")
				.then()
				.statusCode(HttpStatus.NOT_FOUND.value());
	}

	@Test
	public void testRemoveAccount() {

		// first create account
		given()
				.param("accountNumber", 123)
				.param("accountHolder", "M asad")
				.post("/accounts")
				.then()
				.statusCode(HttpStatus.CREATED.value());

		given()
				.pathParam("accountNumber", 123)
				.delete("/accounts/{accountNumber}")
				.then()
				.statusCode(HttpStatus.OK.value());
	}
	@Test
	void contextLoads() {
	}

}
