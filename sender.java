import java.awt.*;
import java.io.IOException; 
import java.net.DatagramPacket; 
import java.net.DatagramSocket; 
import java.io.File; 
import java.util.Scanner; 
import java.net.*;
import java.nio.*;
import java.nio.file.*;
import java.io.*;
import java.util.stream.*;
import java.nio.charset.StandardCharsets;



public class sender{
	public static void main(String[] args) throws Exception{
		String host = args[0];
		int udp_recive_port = Integer.parseInt(args[1]);
		int udp_sender_port= Integer.parseInt(args[2]);
		String name = args[3];
		int max_size = Integer.parseInt(args[4]);
		int timer = Integer.parseInt(args[5]); 

		System.out.println(host);
		System.out.println(udp_recive_port);
		System.out.println(udp_sender_port);
		System.out.println(name);
		System.out.println(max_size);
		System.out.println(timer);
		DatagramPacket dp;

		int ack = 0;
		

		

		DatagramSocket ds = new DatagramSocket(udp_sender_port);



		InetAddress ip = InetAddress.getByName(host);


		String str = "" + ack ;

		byte[] receive = new byte[max_size];

		File file = new File(name);

		Scanner sc = new Scanner(file);


		while(sc.hasNextLine()){


		while(sc.hasNextLine()){
			str = str + sc.nextLine();
			System.out.println(str);

			if(str.length()== receive.length-1){
				break;

			}
		}

		
		
		if(str.length()<receive.length){
			dp = new DatagramPacket(str.getBytes(),str.length(), ip, udp_recive_port);

		}

		else {

			 dp = new DatagramPacket(str.getBytes(),receive.length, ip, udp_recive_port);

			}	
		ds.send(dp);

		ds.setSoTimeout(timer);

		while(true) {


			try{
    			ds.receive(dp);

    			String strRecv = new String(dp.getData(), 0, dp.getLength());


    			if(strRecv.contains(str[0])){

    				if(ack ==1){
    					ack =0;
    				}
    				else{
    					ack =1;
    				}
    				str = "" + ack ;

    				break;

    			}

 
			}catch (SocketTimeoutException e) {
        		break;  // Closing here would cause a SocketException
    		}

    		ds.send(dp);
    		ds.setSoTimeout(timer);





		}

	}

	str= ack +"EOF";

	 dp = new DatagramPacket(str.getBytes(),str.length(), ip, udp_recive_port);

	 while(true) {


			try{
    			ds.receive(dp);

    			String strRecv = new String(dp.getData(), 0, dp.getLength());


    			if(strRecv.contains(str[0])){

    				if(ack ==1){
    					ack =0;
    				}
    				else{
    					ack =1;
    				}
    				str = "" + ack ;

    				break;

    			}

 
			}catch (SocketTimeoutException e) {
        		break;  // Closing here would cause a SocketException
    		}

    		ds.send(dp);
    		ds.setSoTimeout(timer);



	}

	ds.close();



	}
}