package com.bzawadzki.lib.listener;

import com.bzawadzki.lib.annotation.WireMockTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;

@WireMockTest(port = 8081)
@Listeners(WireMockListener.class)
public class WireMockListenerDifferentPortTest {

  @BeforeClass
  public void setUpStubs(){
    stubFor(get(urlEqualTo("/any")).willReturn(aResponse().withStatus(200)));
  }

  @Test
  public void testWithWireMockListener() {
    stubFor(get(anyUrl()).willReturn(aResponse().withStatus(200)));
    given().port(8081).basePath("/any")
            .when().get()
            .then().statusCode(200);
  }

  @Test
  public void testWithWireMockListenerAgain() {
    stubFor(get(anyUrl()).willReturn(aResponse().withStatus(200)));
    given().port(8081).basePath("/any")
            .when().get()
            .then().statusCode(200);
  }

}
