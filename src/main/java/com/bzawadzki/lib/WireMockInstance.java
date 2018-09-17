package com.bzawadzki.lib;

import com.bzawadzki.lib.annotations.WireMockConfig;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;
import static com.github.tomakehurst.wiremock.core.WireMockApp.FILES_ROOT;
import static com.github.tomakehurst.wiremock.core.WireMockApp.MAPPINGS_ROOT;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class WireMockInstance {

  private static WireMockInstance instance;
  private static WireMockServer wireMockServer;
  private boolean useTempFolders;
  private int port;

  private WireMockInstance() { }

  public static WireMockInstance getInstance() {
    if(Objects.isNull(instance)){
      instance = new WireMockInstance();
    }
    return instance;
  }

  public void startServerDefault(WireMockConfig wireMockConfig) {
    this.port = wireMockConfig.port();
    this.useTempFolders = wireMockConfig.useTempFolder();
    startServer();
  }

  public void startServerDefault(){
    this.port = 8181;
    this.useTempFolders = true;
    startServer();
  }

  private void startServer() {
    if (Objects.isNull(wireMockServer)) {
      wireMockServer = new WireMockServer(getWireMockConfiguration());
    }
    if (!wireMockServer.isRunning()) {
      wireMockServer.start();
    }
    if(wireMockServer.port()!= this.port){
      wireMockServer.stop();
      wireMockServer = new WireMockServer(getWireMockConfiguration());
      wireMockServer.start();
    }
    WireMock.configureFor(wireMockServer.port());
  }

  public void stopServer(WireMockConfig wireMockConfig) {
    stopServer(wireMockConfig.cleanStubsAfterTest());
  }

  public void stopServer() {
    stopServer(false);
  }

  private void stopServer(boolean cleanStubs){
    if(cleanStubs) {
      wireMockServer.getStubMappings().forEach(sm -> wireMockServer.removeStub(sm));
    }
    wireMockServer.stop();
  }


  private File setupTempFileRoot() {
    try {
      File root = Files.createTempDirectory("tempWireMock").toFile();
      new File(root, MAPPINGS_ROOT).mkdirs();
      new File(root, FILES_ROOT).mkdirs();
      return root;
    } catch (IOException e) {
      return throwUnchecked(e, File.class);
    }
  }

  private WireMockConfiguration getWireMockConfiguration() {
    WireMockConfiguration options = new WireMockConfiguration();
    if (useTempFolders) {
      options = wireMockConfig().withRootDirectory(setupTempFileRoot().getAbsolutePath());
    }
    options.port(port);
    return options;
  }

}
