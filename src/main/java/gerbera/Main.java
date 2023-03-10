package gerbera;

import gerbera.fdsnws.GerberaServer;

import io.vertx.core.Vertx;

/**
 *
 * @author Julian Peña.
 */
public class Main {
	
	// vertx logging stuff
    static {
        System.setProperty("vertx.logger-delegate-factory-class-name",
                "io.vertx.core.logging.SLF4JLogDelegateFactory");
        System.setProperty("logback.configurationFile", "logback.xml");
    }
	
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new GerberaServer());
	}
	
}
