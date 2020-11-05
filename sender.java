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

		

		

		DatagramSocket ds = new DatagramSocket();


		byte[] in = Files.readAllBytes(Paths.get(name));

		InetAddress ip = InetAddress.getByName(host);



		byte[] receive = new byte[max_size];



		int i = 0;
		while(i<receive.length){
			String content= Byte.toString(receive[i]);
			if( content == "*"){
				break;
			}
			receive[i]= in[i];
			i++;
			
		}	
			

		DatagramPacket dp = new DatagramPacket(receive,receive.length, ip, udp_recive_port);


		

		ds.send(dp);



	}
}