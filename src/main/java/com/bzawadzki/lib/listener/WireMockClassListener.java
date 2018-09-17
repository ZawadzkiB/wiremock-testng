package com.bzawadzki.lib.listener;

import com.bzawadzki.lib.WireMockInstance;
import com.bzawadzki.lib.annotations.WireMockConfig;
import org.apache.commons.lang3.ArrayUtils;
import org.testng.IClassListener;
import org.testng.ITestClass;

public class WireMockClassListener implements IClassListener {


  @Override
  public void onBeforeClass(ITestClass iTestClass) {
    WireMockConfig[] wireMockConfig = iTestClass.getRealClass().getDeclaredAnnotationsByType(WireMockConfig.class);
    if (ArrayUtils.isNotEmpty(wireMockConfig)) {
      WireMockInstance.getInstance().startServerDefault(wireMockConfig[0]);
    } else {
      WireMockInstance.getInstance().startServerDefault();
    }
  }

  @Override
  public void onAfterClass(ITestClass iTestClass) {
    WireMockInstance.getInstance().stopServer();
  }


}
