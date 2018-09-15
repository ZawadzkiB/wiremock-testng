package com.bzawadzki.lib.listener;

import com.bzawadzki.lib.annotation.WireMockTest;
import org.apache.http.conn.HttpHostConnectException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;

@Listeners(WireMockListener.class)
public class WireMockListenerTest {

  @WireMockTest
  @Test
  public void testWithWireMockListener() {
    stubFor(get(anyUrl()).willReturn(aResponse().withStatus(200)));
    given().port(8989).basePath("/any")
            .when().get()
            .then().statusCode(200);
  }

  @Test(expectedExceptions = HttpHostConnectException.class)
  public void testWithOutWireMockListener() {
    stubFor(get(anyUrl()).willReturn(aResponse().withStatus(200)));
    given().port(8989).basePath("/any")
            .when().get();
  }

}
