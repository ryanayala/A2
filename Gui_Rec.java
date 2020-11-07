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
	public String acknum = "0";
	public JLabel packetSeq = new JLabel("packet sequence: 0");
	public int seqCount = 0;
	public int dataRecieved = 0;
	public int reliableTrans = 1;
	
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
		 final JButton button = new JButton("Receive");
		 final JButton buttonToggle = new JButton("currently: reliable");
		 ///unreliable
		textPanel.add(packetSeq);
		textPanel.add(new JLabel("IP FIIELD"));
		textPanel.add(ipField);
		textPanel.add(new JLabel("UDP SEND FIIELD"));
		textPanel.add(UDPportsend);
		textPanel.add(new JLabel("UDP RECEIVE FIIELD"));
		textPanel.add(UDPportreceive);
		textPanel.add(new JLabel("FILE NAME REQUEST FIIELD"));
		textPanel.add(fileField);
		textPanel.add(button);
		textPanel.add(buttonToggle);
		
		f.add(textPanel);

		final JTextField incom_ip = new JTextField();

		incom_ip.setText("Waiting For Ip");
		incom_ip.setBackground(Color.RED);

		button.addActionListener(new ActionListener() {
			
	        @Override
	        public void actionPerformed(ActionEvent e)  {
	        	button.disable();
	        	(new Thread(new SocketReader( UDPportsend, UDPportreceive, fileField,  ipField))).start();
	        	button.enable();
	        }
	    });
		
		buttonToggle.addActionListener(new ActionListener() {
			
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	if (buttonToggle.getText().equals("currently: reliable")) {
	        		reliableTrans = 0;
	        		buttonToggle.setText("currently: unreliable");
	        	} else {
	        		reliableTrans = 1;
	        		buttonToggle.setText("currently: reliable");
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
            acknum = "0";
            dataRecieved = 0;
            seqCount = 0;
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
			System.out.print(dataRecieved);
			dataRecieved+=1;
			DatagramPacket dp = new DatagramPacket(buf,buf.length);
			datasocket.receive(dp);
			if (reliableTrans == 1 || dataRecieved%10 != 0) {
				
				strRecv = new String(dp.getData(), 0, dp.getLength());
				if(strRecv.substring(0,1).equals(acknum)) {
					
					seqCount+=1;
					packetSeq.setText("packet sequence: "+ Integer.toString(seqCount));
					System.out.println(strRecv);//can be removed later
					strRecv=strRecv.substring(1);
					if (!strRecv.equals("EOF")) {
						
						printW.print(strRecv);
						
					}
					ack();
				}
			}
			
			
		}
		
			
	}
	
	public void ack() throws IOException{
		InetAddress ip = InetAddress.getByName(ipsend);
		byte[] b = (new String(acknum+"ACK").getBytes());
		DatagramPacket d = new DatagramPacket(b,4, ip, portsend);
		ds.send(d);
		if(acknum.equals("1")) {
			acknum = "0";
		} else {
			acknum = "1";
		}
	}
	
	
	public class SocketReader implements Runnable{
		Gui_Rec _parent;
		JTextField _UDPportsend;
		JTextField _UDPportreceive;
		JTextField _fileField;
		JTextField _ipField;
		
		public SocketReader(JTextField UDPportsend,JTextField UDPportreceive,JTextField fileField, JTextField ipField) {
	
			_UDPportsend = UDPportsend;
			_UDPportreceive = UDPportreceive;
			_fileField = fileField;
			_ipField = ipField;
		}
		public void run() {
			try {
        		portsend = Integer.parseInt(_UDPportsend.getText());
        		ipsend = _ipField.getText();
        		packetSeq.setText("packet sequence: 0");
        		ds = new DatagramSocket(Integer.parseInt(_UDPportreceive.getText())); // for testing port 4455
        		File file = new File(_fileField.getText());   
        		//editing file
        		System.out.println("listening");
        		receiveFile(_fileField.getText(), ds);
        		if (!ds.equals(null)) {
        			ds.close();
        			ds = null;
        		}
        	}catch(Exception fileE) {
        		System.out.println("Failed To Edit and Write Socket...");
        	}   
			
		}
	}
	
}


