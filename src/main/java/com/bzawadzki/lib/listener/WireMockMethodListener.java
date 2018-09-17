package com.bzawadzki.lib.listener;

import com.bzawadzki.lib.WireMockInstance;
import com.bzawadzki.lib.annotations.WireMockConfig;
import org.apache.commons.lang3.ArrayUtils;
import org.testng.*;

public class WireMockMethodListener implements IInvokedMethodListener {

  @Override
  public void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
    if (iInvokedMethod.isTestMethod()) {
      WireMockConfig[] wireMockConfig = iInvokedMethod.getTestMethod()
              .getConstructorOrMethod().getMethod().getDeclaredAnnotationsByType(WireMockConfig.class);
      if (ArrayUtils.isNotEmpty(wireMockConfig)) {
        WireMockInstance.getInstance().startServerDefault(wireMockConfig[0]);
      } else {
        WireMockInstance.getInstance().startServerDefault();
      }
    }
  }

  @Override
  public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
    if (iInvokedMethod.isTestMethod()) {
      WireMockConfig[] wireMockConfig = iInvokedMethod.getTestMethod()
              .getConstructorOrMethod().getMethod().getDeclaredAnnotationsByType(WireMockConfig.class);
      if (ArrayUtils.isNotEmpty(wireMockConfig)) {
        WireMockInstance.getInstance().stopServer(wireMockConfig[0]);
      } else {
        WireMockInstance.getInstance().stopServer();
      }
    }
  }
}
