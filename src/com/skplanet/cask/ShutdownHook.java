package com.skplanet.cask;


import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skplanet.cask.container.Container;
import com.skplanet.cask.container.config.ConfigReader;
import com.skplanet.cask.container.dao.ServerRuntimeDao;
import com.skplanet.cask.util.StringUtil;

public class ShutdownHook extends Thread {
    private Logger logger = LoggerFactory.getLogger(ShutdownHook.class);

    @Override
    public void run() {
        
        logger.info("Shutdown Program...... ");
        
        try {
            if(ConfigReader.getInstance().getServerConfig().saveRuntimeIntoDb()) {
                int serverRuntimeId = Container.getServerRuntimeDbPkId();
                if(serverRuntimeId != -1) {
                    ServerRuntimeDao dao = new ServerRuntimeDao();
                    dao.updateEnd(serverRuntimeId);
                }
            }
        } catch(SQLException e) {
            logger.error(StringUtil.exception2Str(e));
        } finally {
            logger.info("Shutdown Completed.");
        }
        
    }
}
