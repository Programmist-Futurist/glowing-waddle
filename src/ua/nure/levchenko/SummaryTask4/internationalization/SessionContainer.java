package ua.nure.levchenko.SummaryTask4.internationalization;

import org.apache.log4j.Logger;

import java.util.HashMap;

public class SessionContainer extends HashMap<Integer, Language> {

    private static final Logger LOG = Logger.getLogger(SessionContainer.class);

    private static SessionContainer instance;

    private SessionContainer() {
    }

    public static synchronized SessionContainer getInstance() {
        if (instance == null) {
            instance = new SessionContainer();
            LOG.debug("SessionContainer initialized");
        }
        return instance;
    }


}
