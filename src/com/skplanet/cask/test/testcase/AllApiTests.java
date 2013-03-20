package com.skplanet.cask.test.testcase;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({ ConfigReaderTest.class, ContainerTest.class, ApiTest.class })
public class AllApiTests {
    private final String testName = "api & db tests";
    public String getTestName() {
        return testName;
    }
}
