package com.skplanet.cask.test;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import com.skplanet.cask.test.result.TCase;
import com.skplanet.cask.test.result.TSuite;
import com.skplanet.cask.test.result.TSuites;
import com.skplanet.cask.test.testcase.ApiTest;
import com.skplanet.cask.test.testcase.ConfigReaderTest;
import com.skplanet.cask.test.testcase.ContainerTest;


public class Runner {

    private static TSuites testSuites = new TSuites();
    private static List<Result> testResults = new ArrayList<Result>();
    
    public static void runTest(String testName, Class<?> testClass[]) {
        
        TestRuntime.printSuite(testName);
        TSuite suite = new TSuite();
        suite.setName(testName);
        
        float totalTime = 0;
        int totalFail = 0;
        int totalTests = 0;
        for(int i = 0; i < testClass.length; ++i) {
            Result temp = JUnitCore.runClasses(testClass[i]);
            testResults.add(temp);
            
            TCase tcase = new TCase();
            tcase.setName(testName + "-" + testClass[i].getName());
            tcase.setClassname(testClass[i].getName());
            tcase.setTime(temp.getRunTime() / 1000);
            
            suite.addTestCase(tcase);
            
            totalTime += temp.getRunTime();
            totalFail += temp.getFailureCount();
            totalTests += temp.getRunCount();
        }
        
        suite.setTime(totalTime);
        suite.setFailures(totalFail);
        suite.setTests(totalTests);
    
        testSuites.addTestSuite(suite);
    }
    

    private static void summerizeResults() {
        
        
        Iterator<TSuite> it = testSuites.getTestSuites().iterator();
        
        float totalTime = 0;
        int totalFail = 0;
        int totalTests = 0;
        
        while(it.hasNext()) {
            TSuite suite = it.next();
            totalFail += suite.getFailures();
            totalTests += suite.getTests();
            totalTime += suite.getTime();
        }
        
        testSuites.setFailures(totalFail);
        testSuites.setTests(totalTests);
        testSuites.setTime(totalTime);
    }
    
    private static void printFails() {
        
        for(int i = 0; i < testResults.size(); ++i) {
            Result item = testResults.get(i);
            if(item.getFailureCount() > 0) {
                Iterator<Failure> it = item.getFailures().iterator();
                while(it.hasNext()) {
                    System.out.println(it.next());
                }
            }
        }
    }
    
    public static Class<?>[] normalTest() {
        return new Class[] { ConfigReaderTest.class,
                             ContainerTest.class };   
    }
    public static Class<?>[] apiDbTest() {
        return new Class[] { ConfigReaderTest.class, 
                             ContainerTest.class,
                             ApiTest.class };
    }
    
    public static void setTestSuitesName(String name) {
        testSuites.setName(name);
    }
    public static void produceTestResult() throws Exception  {
    
        boolean success = TestRuntime.getInstance().assertDiff();
        
        summerizeResults();
        
        if(success) {
            System.out.println("-----");
            System.out.printf("Test : Success (%.2f sec)\n", 
                              testSuites.getTime() / 1000);
        } 
        else {
            System.out.println("-----");
            System.out.printf("Test : Fail (%.2f sec)\n", 
                              testSuites.getTime() / 1000);
            System.out.println("Check files : .org file and .now file (${server.home}/test)");
            System.out.println("Use file diff program : Windows Winmerge, Linux tkdiff...");
            
        }
        //TestRuntime.getInstance().close();
        System.out.printf("Junit result >> total : %d tests, failures : %d tests\n", 
                          testSuites.getTests(), 
                          testSuites.getFailures());
        
        System.out.println("-----");
        printFails();
        
        TestRuntime.getInstance().writeReport(testSuites);
        
        if(success) {
            System.exit(0);
        }
        else {
            System.exit(-1);
        }
    }
    
    public static void initServerHome() throws Exception {
        TestRuntime.getInstance().init(System.getProperty("server.home"));
    }
    
    public static void main(String args[]) throws Exception {
        
        initServerHome();
        runAll();
        produceTestResult();
    }
    
    public static void runAll() throws Exception {
        TestRuntime.getInstance().changeConfig4Init();
        
        setTestSuitesName("Cask test");
        runTest("Normal test",
                normalTest());
        
        TestRuntime.getInstance().setupDbEnv();
        TestRuntime.getInstance().setupFtpEnv();
        TestRuntime.getInstance().changeConfig4DbTest();
        
        runTest("Api & DB test", 
                apiDbTest());        
        TestRuntime.getInstance().changeConfig4Init();
    }
}
