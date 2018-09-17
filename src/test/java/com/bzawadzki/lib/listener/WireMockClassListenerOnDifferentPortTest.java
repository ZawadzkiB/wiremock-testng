package com.bzawadzki.lib.listener;

import com.bzawadzki.lib.annotations.WireMockConfig;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;

@WireMockConfig(port = 8081)
@Listeners(WireMockClassListener.class)
public class WireMockClassListenerOnDifferentPortTest {

  @BeforeClass
  public void setUpStubs(){
    stubFor(get(urlEqualTo("/any")).willReturn(aResponse().withStatus(200)));
  }

  @WireMockConfig(port = 8081)
  @Test
  public void testWithWireMockListener() {
    stubFor(get(anyUrl()).willReturn(aResponse().withStatus(200)));
    given().port(8081).basePath("/any")
            .when().get()
            .then().statusCode(200);
  }

  @WireMockConfig(port = 8081)
  @Test
  public void testWithWireMockListenerAgain() {
    stubFor(get(anyUrl()).willReturn(aResponse().withStatus(200)));
    given().port(8081).basePath("/any")
            .when().get()
            .then().statusCode(200);
  }

}
