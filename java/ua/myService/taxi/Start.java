package ua.myService.taxi;

import jakarta.xml.ws.Endpoint;
import ua.myService.taxi.service.LINK;
import ua.myService.taxi.service.ServiceDAOImpl;

import java.util.Scanner;
import java.util.logging.Logger;

public class Start {
    private static final Logger LOG = Logger.getLogger(Start.class.getName());

    public static void main(String[] args) {
//        System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
//        System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");

        Endpoint ep = Endpoint.publish(LINK.ADDRESS, ServiceDAOImpl.getInstance());
        LOG.info("Server ready at " + LINK.ADDRESS + "?wsdl ...\n");

        Scanner sc = new Scanner(System.in);
        sc.nextLine();
        sc.close();
        LOG.info("Server exiting");
        ep.stop();
    }
}
