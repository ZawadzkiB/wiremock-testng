package com.bzawadzki.lib.listener;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static io.restassured.RestAssured.given;

@Listeners(WireMockClassListener.class)
public class WireMockClassListenerTest {

  @BeforeClass
  public void setUpStubs(){
    stubFor(get("/stub1").willReturn(aResponse().withStatus(201)));
  }

  @Test
  public void testWithWireMockListenerOnSpecificPath() {
    given().port(8181).basePath("/stub1")
            .when().get()
            .then().statusCode(201);
  }

  @Test(dependsOnMethods = "testWithWireMockListenerOnSpecificPath")
  public void testWithWireMockListenerOnSpecificPathWithStubFromPreviousMethod() {
    given().port(8181).basePath("/stub1")
            .when().get()
            .then().statusCode(201);
  }

}
