package com.skplanet.cask.container.monitor;



public interface SimpleServiceMonitorMBean {
    
    public int getTotalCalled();
    public long getTotalElapsed();
    public float getTps();
    public void reset();
    public long getErrorCount();
}
