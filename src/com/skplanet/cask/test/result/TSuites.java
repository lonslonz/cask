package com.skplanet.cask.test.result;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//<?xml version="1.0" encoding="UTF-8"?>
//<testsuite errors="" failures="" hostname="" name="" tests="" time="" timestamp="">
//    <testcase classname="" name="" time="">
//        <error message="" type=""/>
//    </testcase>
//    <testcase classname="" name="" time="">
//        <failure message="" type=""/>
//    </testcase>
//    <system-out/>
//    <system-err/>
//</testsuite>

@XmlRootElement(name="testsuites")
@XmlAccessorType(XmlAccessType.FIELD)
public class TSuites {

    @XmlAttribute
    private int errors = 0;
    @XmlAttribute
    private int failures = 0;
    @XmlAttribute
    private String name;
    @XmlAttribute
    private int tests = 0;
    @XmlAttribute
    private float time;
    
    @XmlElement(name = "testsuite")
    private List<TSuite> testSuites = new ArrayList<TSuite>();
    
    public List<TSuite> getTestSuites() {
        return testSuites;
    }
    public void setTestSuites(List<TSuite> testSuites) {
        this.testSuites = testSuites;
    }
    public void addTestSuite(TSuite e) {
        this.testSuites.add(e);
    }
    public int getErrors() {
        return errors;
    }
    public void setErrors(int errors) {
        this.errors = errors;
    }
    public int getFailures() {
        return failures;
    }
    public void setFailures(int failures) {
        this.failures = failures;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getTests() {
        return tests;
    }
    public void setTests(int tests) {
        this.tests = tests;
    }
    public float getTime() {
        return time;
    }
    public void setTime(float time) {
        this.time = time;
    }
    
}
