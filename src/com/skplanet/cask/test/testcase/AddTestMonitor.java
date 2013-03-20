package com.skplanet.cask.test.testcase;

public class AddTestMonitor implements AddTestMonitorMBean {
    @Override
    public Integer getId() {
        return 1;
    }
    @Override
    public String getName() {
        return "mbean test";
    }
}
