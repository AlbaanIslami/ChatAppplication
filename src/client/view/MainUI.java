package client.view;

import client.controller.ClientController;
import javax.swing.*;

/**
 * This class acts only as a forwarder of functions for the other UI classes. Each function creates the
 * JFrame necessary for each window to be displayed.
 */

public class MainUI
{
    private ClientController clientController;
    public MainUI(ClientController clientController)
    {
        this.clientController = clientController;
    }
    public void showMessageInFrame(final IncomingMessageUI im) {
        JFrame frame = new JFrame("New Message!!");
        frame.add(im);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocation(200, 120);
        frame.setResizable(false);
        im.setFrame(frame);
    }

    public void writeMessage(final MessageUI nmUI) {
        JFrame frame = new JFrame("Send Message");
        frame.add(nmUI);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocation(10, 120);
        frame.setResizable(false);
        nmUI.setFrame(frame);
    }

    public void showTerminalInFrame(final ListOfUsersUI ui) {
        JFrame frame = new JFrame("ChitChat :)");
        frame.add(ui);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocation(400, 0);
        frame.setResizable(false);
    }
}
