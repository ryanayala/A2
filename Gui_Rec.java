import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JFrame;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import java.awt.Shape;
import javax.swing.Box;
import javax.swing.border.EmptyBorder;
 
import java.awt.Insets;
import java.awt.Dimension;

import javax.swing.JTextField;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;
import java.awt.event.WindowEvent;
import java.awt.Color;
import java.net.*;
import java.io.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner; 
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;



public class Gui_Rec{
	public DatagramSocket ds = null;
	public int portsend = -1;
	public String ipsend = null;
	public int acknum = 0;
	public int waitingFor = 0;
	public Gui_Rec() {
		//Window placement and size
		JFrame f = new JFrame("Receiver");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(750, 750);
	    f.setLocation(300,200);
	    //layout
	    f.setLayout(new GridLayout(0,2));
	    
		final JTextArea textArea = new JTextArea(10, 40);
		f.add(textArea);
		
		
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new BoxLayout(textPanel,BoxLayout.Y_AXIS));
		
		 JTextField ipField = new JTextField(13);
		 JTextField UDPportsend = new JTextField(13);
		 JTextField UDPportreceive = new JTextField(13);
		 JTextField fileField = new JTextField(13);
		 final JButton button = new JButton("Return Ack");
		
		textPanel.add(new JLabel("IP FIIELD"));
		textPanel.add(ipField);
		textPanel.add(new JLabel("UDP SEND FIIELD"));
		textPanel.add(UDPportsend);
		textPanel.add(new JLabel("UDP RECEIVE FIIELD"));
		textPanel.add(UDPportreceive);
		textPanel.add(new JLabel("FILE NAME REQUEST FIIELD"));
		textPanel.add(fileField);
		textPanel.add(button);
		
		f.add(textPanel);

		final JTextField incom_ip = new JTextField();

		incom_ip.setText("Waiting For Ip");
		incom_ip.setBackground(Color.RED);

		button.addActionListener(new ActionListener() {
			
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	try {
	        		portsend = Integer.parseInt(UDPportsend.getText());
	        		ipsend = ipField.getText();
	        		ds = new DatagramSocket(Integer.parseInt(UDPportreceive.getText())); // for testing port 4455
	        		File file = new File(fileField.getText());   
	        		//editing file
	        		System.out.println("listening");
	        		receiveFile(fileField.getText(), ds);
	        		if (!ds.equals(null)) {
	        			ds.close();
	        			ds = null;
	        		}
	        	}catch(Exception fileE) {
	        		System.out.println("Failed To Edit and Write Socket...");
	        	}
	        	

	        }
	    });
		f.setVisible(true);
	}
	
	
	public static void main(String[] args) throws IOException {
		Gui_Rec gr = new Gui_Rec();
	}
	
	
	public void receiveFile(String fileName, DatagramSocket datasocket) {
		try {
			FileWriter fw = new FileWriter(fileName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            //adding data to the file.
            readSocketAppendToFile(pw, datasocket);
            //closing file
            pw.close();
            bw.close();
            fw.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readSocketAppendToFile(PrintWriter printW, DatagramSocket datasocket) throws IOException {
		byte[] buf = new byte[400];
		String strRecv = "";
		
		while(!strRecv.equals("EOF")) {
			DatagramPacket dp = new DatagramPacket(buf,buf.length);
			datasocket.receive(dp);
			strRecv = new String(dp.getData(), 0, dp.getLength());
			System.out.println(strRecv);
			if (!strRecv.equals("EOF")) {
				printW.print(strRecv);
				
			}
			ack();
		}
			
	}
	
	public void ack() throws IOException{
		InetAddress ip = InetAddress.getByName(ipsend);
		byte[] b = (new String("ACK"+Integer.toString(acknum)).getBytes());
		DatagramPacket d = new DatagramPacket(b,4, ip, portsend);
		ds.send(d);
	}
}

