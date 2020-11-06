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
import java.io.FileInputStream;
import java.io.FileInputStream;
import java.lang.Object;
import java.util.Arrays; 




public class sender2{
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

		String ack_str =null;

		InputStream is = null;
		int offset =0;

		

		DatagramSocket ds = new DatagramSocket(udp_sender_port);




		InetAddress ip = InetAddress.getByName(host);


		File file = new File(name);


		while(true){
		byte[] receive = new byte[max_size-1];

		byte[] ack_array = new byte[1];

		byte[] message = new byte[max_size];

		ack_str = String.valueOf(ack);

		ack_array=ack_str.getBytes();


		try{
			is = new FileInputStream(file);

			is.read(receive,offset,max_size-1);

			message=joinByteArray(ack_array,receive);


			if(is.read(receive,offset,max_size-1)==-1){
				break;
			}

			//for(byte b:message) {
         
            // convert byte to character
            //char c = (char)b;
            
            // prints character
            //System.out.print(c);
         //}


			dp = new DatagramPacket(message,message.length, ip, udp_recive_port);


			ds.send(dp);

			System.out.println("sent");




			ds.setSoTimeout(timer);



			while(true){
				try{

				offset+=dp.getLength();
    			ds.receive(dp);

    			System.out.println("Received");



    			String strRecv = new String(dp.getData(), 0, dp.getLength());



    			char a = (char) ack;
    			char b = strRecv.charAt(0);

    			if(a==b){
    				System.out.println("it worked");

    				if(ack ==1){
    					ack = 0;
    				}
    				else{
    					ack =1;
    				}
    				

    				break;

    			}


    		}catch (SocketTimeoutException e) {
        		break;  // Closing here would cause a SocketException
    		}

    		ds.send(dp);
    		ds.setSoTimeout(timer);

			}




		}catch(Exception e) {
         // if any I/O error occurs
         e.printStackTrace();
      }

  }

  String end = String.valueOf(ack) +"EOF";

      ds.close();
      dp = new DatagramPacket(end.getBytes(),end.length(), ip, udp_recive_port);
			ds.send(dp);


	}



	public static byte[] joinByteArray(byte[] byte1, byte[] byte2) {

        return ByteBuffer.allocate(byte1.length + byte2.length)
                .put(byte1)
                .put(byte2)
                .array();

    }






}

