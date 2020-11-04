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



public class Gui_Rec{
	
	public static void main(String[] args) throws IOException {
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
		//button_enabler
		button.addActionListener(new ActionListener() {

	        @Override
	        public void actionPerformed(ActionEvent e) {
	            textArea.append("Sent Ack\n");

	        }
	    });
		
		f.setVisible(true);
		//Listener
		//try {
			DatagramSocket ds = new DatagramSocket(4455); // for testing port 80
			byte[] buf = new byte[400];
			
			DatagramPacket dp = new DatagramPacket(buf,buf.length);
			ds.receive(dp);
			
			String strRecv = new String(dp.getData(), 0, dp.getLength());
			System.out.println(strRecv);
			
			ds.close();
			
		//} catch(Exception e) {
			//System.out.printf("Error Caught");
		//}

	    

	    
		
	}
	


}