package com.TestAPI.TestAPI;

import static org.testng.AssertJUnit.assertTrue;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * Unit test for simple App.
 */
public class ApiKeyGenerator {

	String inputApiKey = "jfr9uihqvncOuii7lda5bDlsvOIDePcKTLWlzLte";
	public static String outputApiKey;

	/**
	 * Rigorous Test :-)
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void generateApiToken() throws InterruptedException {

		RequestSpecification httpRequest;
		Response response;
		RestAssured.baseURI = "https://api.data.gov";
		httpRequest = RestAssured.given();

		JSONObject requestParams = new JSONObject();
		requestParams.put("first_name", "Amandeep");
		requestParams.put("last_name", "Singh");
		requestParams.put("email", "amansingh.it985@gmail.com");
		requestParams.put("terms_and_conditions", 1);
		requestParams.put("registration_source", "web-admin");

		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Host", "api.data.gov");
		httpRequest.header("Origin", "https://api.nasa.gov");

		httpRequest.body(requestParams.toJSONString());

		response = httpRequest.queryParam("api_key", inputApiKey).request(Method.POST, "/api-umbrella/v1/users.json");

		Thread.sleep(5000);

		System.out.println(response.getBody().asString());

		Assert.assertEquals(response.getStatusCode(), 201);

		outputApiKey = response.body().path("user.api_key");

		System.out.println(outputApiKey);

	}
}
