package com.skplanet.cask.test.testcase;

public class TestDefaultMonitor implements TestDefaultMonitorMBean {
    @Override
    public Integer getId() {
        return 1;
    }
    @Override
    public String getName() {
        return "mbean test";
    }
}
