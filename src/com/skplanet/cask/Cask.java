package com.skplanet.cask;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skplanet.cask.container.Container;
import com.skplanet.cask.container.config.ConfigReader;
import com.skplanet.cask.util.StringUtil;
 
public class Cask {
    private static Logger logger = LoggerFactory.getLogger(Cask.class);

    public static void main(String args[]) throws IOException {

        try {
            if (args.length != 1) {
                System.out.println("Usage : [version|start]");
                System.exit(-1);
            }

            ConfigReader.getInstance().init();

            if(args[0].equals("version")) {
                System.out.println("Version : "
                        + ConfigReader.getInstance().getVersion());
                System.exit(0);
            }

            logger.info("Version : {}", ConfigReader.getInstance().getVersion());
            logger.info("Program home : {} ", ConfigReader.getInstance().getHome());

            ShutdownHook shutdownHook = new ShutdownHook();
            Runtime.getRuntime().addShutdownHook(shutdownHook);

            Container container = null;

            if (args[0].equals("start")) {
                container = new Container();
                container.start();
            } else {
                System.out.println("Not Support");
            }
        } catch (Exception e) {
            logger.error(StringUtil.exception2Str(e));
        } finally {
        } // end of try-catch

    } // end of main method

}
