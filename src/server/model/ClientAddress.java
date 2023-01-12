package server.model;

import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class stores a socket and ObjectOutputStream for a user. It contains a constructor and get functions.
 */
public class ClientAddress {
    private ObjectOutputStream oos;
    private Socket socket;

    public ClientAddress(ObjectOutputStream oos, Socket socket) {
        this.oos = oos;
        this.socket = socket;
    }

    public ObjectOutputStream getOos() {
        return this.oos;
    }

    public Socket getSocket() {
        return this.socket;
    }
}
