package com.bzawadzki.lib.listener;

import com.bzawadzki.lib.annotation.WireMockTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@WireMockTest
@Listeners(WireMockListener.class)
public class WireMockDependsOnKeepStubsTest {

  @Test(dependsOnGroups = {"keepStubs"})
  public void testWithWireMockListenerDependsOnStubsFromOtherClass() {
    given().port(8181).basePath("/stub1")
            .when().get()
            .then().statusCode(201);
  }
}
