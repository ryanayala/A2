import java.awt.*;
import java.io.IOException; 
import java.net.DatagramPacket; 
import java.net.DatagramSocket; 
import java.io.File; 
import java.util.Scanner; 
import java.net.*;

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


		//byte[] in =Files.readAllbytes(name);
		String me = "Hello Universe ";

		InetAddress ip = InetAddress.getByName(host);

		//byte[] in = me.getBytes();

		//byte[] receive = new byte[max_size];

		//for(int i =0; i<receive.length; i++){
			//receive[i]= in[i];
		//} 

		DatagramPacket dp = new DatagramPacket(me.getBytes(),me.length(), ip, udp_recive_port);

		ds.send(dp);









	}
}