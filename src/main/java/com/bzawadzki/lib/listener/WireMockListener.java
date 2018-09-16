package com.bzawadzki.lib.listener;

import com.bzawadzki.lib.annotation.WireMockTest;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.apache.commons.lang3.ArrayUtils;
import org.testng.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;
import static com.github.tomakehurst.wiremock.core.WireMockApp.FILES_ROOT;
import static com.github.tomakehurst.wiremock.core.WireMockApp.MAPPINGS_ROOT;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class WireMockListener implements IClassListener{

    private static WireMockServer wireMockServer;
    private WireMockTest wireMockTest;

    @Override
    public void onBeforeClass(ITestClass iTestClass) {
        wireMockTest = getWireMockTestAnnotation(iTestClass.getRealClass());
        if(Objects.nonNull(wireMockTest)){
            startServer(wireMockTest);
        }
    }

    @Override
    public void onAfterClass(ITestClass iTestClass) {
        wireMockTest = getWireMockTestAnnotation(iTestClass.getRealClass());
        if(Objects.nonNull(wireMockTest)){
            stopServer(wireMockTest);
        }
    }

    private WireMockTest getWireMockTestAnnotation(Class clazz){
        WireMockTest wireMockAnnotation = null;

        WireMockTest[] classAnnotation = (WireMockTest[]) clazz.getDeclaredAnnotationsByType(WireMockTest.class);
        if (ArrayUtils.isNotEmpty(classAnnotation)) {
            wireMockAnnotation = classAnnotation[0];
        }

        return wireMockAnnotation;
    }

    private static File setupTempFileRoot() {
        try {
            File root = Files.createTempDirectory("tempWireMock").toFile();
            new File(root, MAPPINGS_ROOT).mkdirs();
            new File(root, FILES_ROOT).mkdirs();
            return root;
        } catch (IOException e) {
            return throwUnchecked(e, File.class);
        }
    }

    private void startServer(WireMockTest wireMockTest){
        WireMockConfiguration options = getWireMockConfiguration(wireMockTest);
        if(Objects.isNull(wireMockServer)){
            wireMockServer = new WireMockServer(options);
        }
        if(!wireMockServer.isRunning()){
            wireMockServer.start();
        }
        WireMock.configureFor(wireMockServer.port());
    }

    private WireMockConfiguration getWireMockConfiguration(WireMockTest wireMockTest) {
        WireMockConfiguration options = new WireMockConfiguration();
        if(wireMockTest.useTempFolders()){
            options = wireMockConfig().withRootDirectory(setupTempFileRoot().getAbsolutePath());
        }
        options.port(wireMockTest.port());
        return options;
    }

    private void stopServer(WireMockTest wireMockTest){
        if(wireMockTest.cleanStubs()){
            wireMockServer.getStubMappings().forEach(sm -> wireMockServer.removeStub(sm));
        }
        if(wireMockTest.restart()){
            wireMockServer.stop();
        }
    }

}
