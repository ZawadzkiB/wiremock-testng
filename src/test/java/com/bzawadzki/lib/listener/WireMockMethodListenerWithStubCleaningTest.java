package com.bzawadzki.lib.listener;

import com.bzawadzki.lib.annotations.WireMockConfig;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static io.restassured.RestAssured.given;

@Listeners(WireMockMethodListener.class)
public class WireMockMethodListenerWithStubCleaningTest {

  @WireMockConfig(cleanStubsAfterTest = true)
  @Test
  public void testWithWireMockListenerOnSpecificPath() {
    stubFor(get("/stub1").willReturn(aResponse().withStatus(201)));
    given().port(8181).basePath("/stub1")
            .when().get()
            .then().statusCode(201);
  }

  @Test(dependsOnMethods = "testWithWireMockListenerOnSpecificPath")
  public void testWithWireMockListenerOnSpecificPathWithStubFromPreviousMethod() {
    given().port(8181).basePath("/stub1")
            .when().get()
            .then().statusCode(404);
  }

  @WireMockConfig(port = 8282)
  @Test(dependsOnMethods = "testWithWireMockListenerOnSpecificPathWithStubFromPreviousMethod")
  public void testWithWireMockListenerOnDifferentPort() {
    stubFor(get("/stub2").willReturn(aResponse().withStatus(201)));
    given().port(8282).basePath("/stub2")
            .when().get()
            .then().statusCode(201);
  }
}
