package com.skplanet.cask.container.monitor;

public class SimpleServiceMonitor implements SimpleServiceMonitorMBean {

    private int totalCalled = 0;
    private long totalElapsed = 0;
    private long errorCount = 0;
    

    @Override
    public int getTotalCalled() {
        return totalCalled;
    }

    @Override
    public long getTotalElapsed() {
        return totalElapsed;
    }
    
    @Override
    public float getTps() {
        if (totalElapsed == 0) {
            return 0;
        }
        return totalCalled / ((float)totalElapsed / 1000);
    }

    @Override
    public synchronized void reset() {
        totalCalled = 0;
        totalElapsed = 0;
        errorCount = 0;
    }

    @Override
    public long getErrorCount() {
        return errorCount;
    }

    public synchronized void incTotalCalled(int totalCalled) {
        this.totalCalled += totalCalled;
    }

    public synchronized void incTotalElapsed(long elapsed) {
        this.totalElapsed += elapsed;
    }
    
    public synchronized void incOneWork(long elapsed) {
        this.totalElapsed += elapsed;
        this.totalCalled++;
    }
    
    public synchronized void incErrorCount() {
        this.errorCount++;
    }
}
