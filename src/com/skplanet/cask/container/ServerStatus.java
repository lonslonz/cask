package com.skplanet.cask.container;

public enum ServerStatus {
    RUNNING("running"),
    END("end"),
    ABNORMAL("abnormal"),
    ALL("all");
    
    private String status;
    
    ServerStatus(String status) {
        this.status = status;
    }
    public String getLowerStr() {
        return status;
    }

}
