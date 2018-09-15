package com.bzawadzki.lib.listener;

import com.bzawadzki.lib.annotation.WireMockTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static io.restassured.RestAssured.given;

@Listeners(WireMockListener.class)
public class WireMockListenerKeepStubsTest {

  @WireMockTest(cleanStubs = false, restart = false)
  @Test
  public void testWithWireMockListenerOnSpecificPath() {
    stubFor(get("/stub1").willReturn(aResponse().withStatus(201)));
    given().port(8081).basePath("/stub1")
            .when().get()
            .then().statusCode(201);
  }

  @WireMockTest()
  @Test(dependsOnMethods = "testWithWireMockListenerOnSpecificPath")
  public void testWithWireMockListenerOnSpecificPathWithStubFromPreviousMethod() {
    given().port(8081).basePath("/stub1")
            .when().get()
            .then().statusCode(201);
  }
}
