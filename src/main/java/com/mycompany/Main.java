package com.mycompany;

import com.mycompany.model.Beer;
import com.mycompany.services.BeerRessource;

import static fr.ybonnel.simpleweb4j.SimpleWeb4j.*;

/**
 * Main class.
 */
public class Main {

    private static boolean dev = true;

    public static boolean isDev() {
        return dev;
    }

    /**
     * Start the server.
     * @param port http port to listen.
     * @param waitStop true to wait the stop.
     */
    public static void startServer(int port, boolean waitStop) {
        // Set the http port.
        setPort(port);
        // Set the path to static resources.
        setPublicResourcesPath("/com/mycompany/public");

        if (isDev()) {
            setHibernateCfgPath("/com/mycompany/config/hibernate.cfg-dev.xml");
        } else {
            setHibernateCfgPath("/com/mycompany/config/hibernate.cfg-prod.xml");
        }

        setEntitiesClasses(Beer.class);

        resource(new BeerRessource("/beer"));
        // Start the server.
        start(waitStop);
    }

    /**
     * @return port to use
     */
    private static int getPort() {
        // Heroku
        String herokuPort = System.getenv("PORT");
        if (herokuPort != null) {
            dev = false;
            return Integer.parseInt(herokuPort);
        }

        // Cloudbees
        String cloudbeesPort = System.getProperty("app.port");
        if (cloudbeesPort != null) {
            dev = false;
            return Integer.parseInt(cloudbeesPort);
        }

        // Default port;
        return 9999;
    }

    public static void main(String[] args) {
        // For main, we want to wait the stop.
        startServer(getPort(), true);
    }
}
