package client.controller;

import client.model.*;
import client.view.*;
import javax.swing.*;

/**
 * Main Method Used to run a client instance. It makes a new ClientController object, a client object and a UI object
 * which is then used to configure and run a login window first then a client window.
 */

public class ClientLauncher {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ClientController clientController = new ClientController();
				Client client = new Client("localhost", 60000, clientController);
				LoginUI ui = new LoginUI(clientController);
			}
		});
	}
}
