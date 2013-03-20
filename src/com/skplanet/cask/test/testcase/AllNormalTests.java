package com.skplanet.cask.test.testcase;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ConfigReaderTest.class, ContainerTest.class})
public class AllNormalTests {
    private final String testName = "normal tests";
    public String getTestName() {
        return testName;
    }
}
