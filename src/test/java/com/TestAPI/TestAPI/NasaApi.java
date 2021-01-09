package com.TestAPI.TestAPI;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class NasaApi {

	Date date = new Date();

	@Test(priority = 0)
	public void positiveScenarioTC1() throws InterruptedException {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		RequestSpecification httpRequest;
		Response response;

		RestAssured.baseURI = "https://api.nasa.gov";
		RestAssured.basePath = "planetary/apod";

		httpRequest = RestAssured.given();

		response = httpRequest.queryParam("api_key", ApiKeyGenerator.outputApiKey)
				.queryParam("date", dateFormat.format(date)).queryParam("concept_tags", "True").get();

		Thread.sleep(5000);

		Assert.assertEquals(response.getStatusCode(), 200);

		System.out.println(response.body().asString());

		Assert.assertTrue(response.body().path("concepts").toString().contains("concept_tags"));
		Assert.assertTrue(response.body().path("date").equals(dateFormat.format(date)));
		Assert.assertTrue(response.body().path("explanation").toString().contains("image"));
		Assert.assertTrue(response.body().path("hdurl").toString().contains(".jpg"));
		Assert.assertTrue(response.body().path("media_type").toString().equals("image"));
		Assert.assertTrue(response.body().path("service_version").equals("v1"));
		Assert.assertNotNull(response.body().path("title"));
		Assert.assertTrue(response.body().path("url").toString().contains(".jpg"));
	}

	@Test(priority = 1)
	public void invalidTokenTC() throws InterruptedException {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		RequestSpecification httpRequest;
		Response response;

		RestAssured.baseURI = "https://api.nasa.gov";
		RestAssured.basePath = "planetary/apod";

		httpRequest = RestAssured.given();

		response = httpRequest.queryParam("api_key", "kjfhgkdj").queryParam("date", dateFormat.format(date))
				.queryParam("concept_tags", "True").get();

		Thread.sleep(5000);

		Assert.assertEquals(response.getStatusCode(), 403);

		System.out.println(response.body().asString());

		Assert.assertTrue(response.body().path("error.code").equals("API_KEY_INVALID"));
		Assert.assertTrue(response.body().path("error.message").toString().contains("An invalid api_key was supplied"));

	}

	@Test(priority = 2)
	public void withoutTokenTC() throws InterruptedException {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		RequestSpecification httpRequest;
		Response response;

		RestAssured.baseURI = "https://api.nasa.gov";
		RestAssured.basePath = "planetary/apod";

		httpRequest = RestAssured.given();

		response = httpRequest.queryParam("date", dateFormat.format(date)).queryParam("concept_tags", "True").get();

		Thread.sleep(5000);

		Assert.assertEquals(response.getStatusCode(), 403);

		System.out.println(response.body().asString());

		Assert.assertTrue(response.body().path("error.code").equals("API_KEY_MISSING"));
		Assert.assertTrue(response.body().path("error.message").toString().contains("No api_key was supplied"));

	}

	@Test(priority = 3)
	public void invalidDateFormatTC() throws InterruptedException {

		DateFormat dateFormat = new SimpleDateFormat("MM/yyyy/dd");

		RequestSpecification httpRequest;
		Response response;

		RestAssured.baseURI = "https://api.nasa.gov";
		RestAssured.basePath = "planetary/apod";

		httpRequest = RestAssured.given();

		response = httpRequest.queryParam("api_key", ApiKeyGenerator.outputApiKey)
				.queryParam("date", dateFormat.format(date)).queryParam("concept_tags", "True").get();

		Thread.sleep(5000);

		Assert.assertEquals(response.getStatusCode(), 400);

		System.out.println(response.body().asString());

		Assert.assertTrue(response.body().path("msg").toString().contains("does not match format"));

	}

	@Test(priority = 4)
	public void withoutDateParamTC() throws InterruptedException {

		RequestSpecification httpRequest;
		Response response;

		RestAssured.baseURI = "https://api.nasa.gov";
		RestAssured.basePath = "planetary/apod";

		httpRequest = RestAssured.given();

		response = httpRequest.queryParam("api_key", ApiKeyGenerator.outputApiKey).queryParam("concept_tags", "True")
				.get();

		Thread.sleep(5000);

		Assert.assertEquals(response.getStatusCode(), 200);

		System.out.println(response.body().asString());

		Assert.assertTrue(response.body().path("concepts").toString().contains("concept_tags"));
		Assert.assertTrue(response.body().path("explanation").toString().contains("image"));
		Assert.assertTrue(response.body().path("hdurl").toString().contains(".jpg"));
		Assert.assertTrue(response.body().path("media_type").toString().equals("image"));
		Assert.assertTrue(response.body().path("service_version").equals("v1"));
		Assert.assertNotNull(response.body().path("title"));
		Assert.assertTrue(response.body().path("url").toString().contains(".jpg"));
	}

	@Test(priority = 5)
	public void invalidBasePath() throws InterruptedException {

		RequestSpecification httpRequest;
		Response response;

		RestAssured.baseURI = "https://api.nasa.gov";
		RestAssured.basePath = "plbkbjb";

		httpRequest = RestAssured.given();

		response = httpRequest.queryParam("api_key", ApiKeyGenerator.outputApiKey).queryParam("concept_tags", "True")
				.get();

		Thread.sleep(5000);

		Assert.assertEquals(response.getStatusCode(), 404);

	}

}
