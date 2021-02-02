import java.awt.*;
import javax.swing.Timer; //for timer

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.IOException;
import java.util.*; 

public class Main
    extends JPanel implements ActionListener { 
    JTextArea textArea;
    VisualServer server;
    StocksDB allowedUsers;
    public String symbols[]={"FB", "VRTU","MSFT", "GOOGL", "YHOO", "XLNX", "TSLA", "TXN"};

    public Main(VisualServer server,StocksDB allowedUsers) {
        super(new GridBagLayout());
        textArea = new JTextArea(15, 60);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

         //Add Components to this panel.
            GridBagConstraints c = new GridBagConstraints();
            c.gridwidth = GridBagConstraints.REMAINDER;

            c.fill = GridBagConstraints.HORIZONTAL;

            c.fill = GridBagConstraints.BOTH;
            c.weightx = 20.0;
            c.weighty = 20.0;

            add(scrollPane, c);

        Timer timer = new Timer(500, this);
        timer.start();
        this.server = server;
        this.allowedUsers=allowedUsers;
        editTextArea(allowedUsers);
    }

    public void editTextArea(StocksDB allowedUsers){
        textArea.selectAll();
        textArea.replaceSelection(" ");
        textArea.append("  Symbol\t\tName\t\t\tPrice\n");
        textArea.append("  "+symbols[0]+"\t"+allowedUsers.findName(symbols[0])+"\t\t\t\t"+Double.toString(allowedUsers.findPrice(symbols[0]))+"\n");
        textArea.append("  "+symbols[1]+"\t"+allowedUsers.findName(symbols[1])+"\t\t"+Double.toString(allowedUsers.findPrice(symbols[1]))+"\n");
        textArea.append("  "+symbols[2]+"\t"+allowedUsers.findName(symbols[2])+"\t\t"+Double.toString(allowedUsers.findPrice(symbols[2]))+"\n");
        textArea.append("  "+symbols[3]+"\t"+allowedUsers.findName(symbols[3])+"\t\t"+Double.toString(allowedUsers.findPrice(symbols[3]))+"\n");
        textArea.append("  "+symbols[4]+"\t"+allowedUsers.findName(symbols[4])+"\t\t\t"+Double.toString(allowedUsers.findPrice(symbols[4]))+"\n");
        textArea.append("  "+symbols[5]+"\t"+allowedUsers.findName(symbols[5])+"\t\t\t\t"+Double.toString(allowedUsers.findPrice(symbols[5]))+"\n");
        textArea.append("  "+symbols[6]+"\t"+allowedUsers.findName(symbols[6])+"\t\t\t\t"+Double.toString(allowedUsers.findPrice(symbols[6]))+"\n");
        textArea.append("  "+symbols[7]+"\t"+allowedUsers.findName(symbols[7])+"\t"+Double.toString(allowedUsers.findPrice(symbols[7]))+"\n");
    }
    
    public void actionPerformed(ActionEvent e) { 
        String newline = server.getMSG();
        if(newline != null) {
            editTextArea(allowedUsers);

            //Make sure the new text is visible, even if there
            //was a selection in the text area.
            //textArea.setCaretPosition(textArea.getDocument().getLength());
        }

    }

    public static void main(String [] args) throws IOException { 
	//Create and set up the window.
        JFrame frame = new JFrame("TextDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        StocksDB allowedUsers = new StocksDB("stocks.csv","Symbol","Price","Security Name");
        VisualServer server = new VisualServer(MainServer.BASE_PORT,allowedUsers);
        //Add contents to the window.
        frame.add(new Main(server,allowedUsers));

        //Display the window.
        frame.pack();
        frame.setVisible(true);
        server.server_loop();
    }
}
	