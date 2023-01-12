package server.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import server.controller.*;

/**
 * This class contains the necessary functions to run a serverwindow which can output the serverlog to the user.
 */

public class ServerUI extends JPanel {

    private JTextArea textArea = new JTextArea();
    private JButton btnUpdate = new JButton("Show");
    private JLabel txtFromDate = new JLabel("From:");
    private JLabel txtToDate = new JLabel("To:");
    private JTextField fromDate = new JTextField("yyyy/MM/dd HH:mm");
    private JTextField toDate = new JTextField("yyyy/MM/dd HH:mm");
    private ServerController serverController;
    private JScrollPane scrollPane = new JScrollPane(textArea);
    private JPanel btnPanel = new JPanel(new GridLayout(1, 5));
    private Font font = new Font("MONOSPACED", Font.PLAIN, 8);

    /**
     * This is the Class constructor.
     * @param serverController
     */
    public ServerUI(ServerController serverController) {
        this.serverController = serverController;
        serverController.addServerUi(this);

        init();
    }

    /**
     * This function initiates the window and its buttons.
     */
    private void init() {
        setSize(500, 500);
        setLayout(new BorderLayout());

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(400, 400));
        textArea.setEditable(false);
        textArea.setSize(400, 400);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        add(scrollPane, BorderLayout.CENTER);

        fromDate.setFont(font);
        toDate.setFont(font);

        btnPanel.add(txtFromDate);
        btnPanel.add(fromDate);
        btnPanel.add(txtToDate);
        btnPanel.add(toDate);
        btnPanel.add(btnUpdate);

        add(btnPanel, BorderLayout.SOUTH);
        btnUpdate.addActionListener(new ButtonListener());

        JFrame frame = new JFrame("ServerUI");
        frame.add(this);
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocation(0, 0);
        frame.setResizable(false);
    }

    /**
     * This function inserts strings into the textarea when called.
     * @param str
     */
    public void updateTextArea(String str) {
        textArea.append(str + "\n");
    }

    /**
     * This function clears the textarea.
     */
    public void clearTextArea() {
        textArea.setText("");
    }

    /**
     * This function is called when the client has not logged any traffic and can therefore not show anything in the textarea.
     */
    public void noTraffic() {
        textArea.setText("No traffic to show in this time frame");
    }

    /**
     * This function is the buttonlistener which performs an action/calls a method when the button is pressed.
     */
    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                serverController.getTraffic(fromDate.getText(), toDate.getText());
            } catch (ParseException e1) {
                JOptionPane.showMessageDialog(null, "Date has to be in this format: yyyy/MM/dd HH:mm");
            }
        }
    }
}
