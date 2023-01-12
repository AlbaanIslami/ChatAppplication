package server.controller;

import java.net.*;
import server.model.*;
import server.view.*;

/**
 * Main Method Used to run a server instance. It makes a new serverController object, a server object and a UI object
 * which is then used to configure and run a server window.
 */

public class ServerLauncher {
    public static void main(String[] args) {
        ServerController serverController = new ServerController();
        Server server = new Server(60000, serverController);
        ServerUI serverUi = new ServerUI(serverController);

        try {
            System.out.println(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
