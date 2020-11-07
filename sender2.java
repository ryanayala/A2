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
		byte[] receive;
		byte[] ack_array;
		byte[] message ;
		int endof = 0;
		

		DatagramSocket ds = new DatagramSocket(Integer.parseInt(args[2]));




		InetAddress ip = InetAddress.getByName(host);

		System.out.println(System.currentTimeMillis());




		File file = new File(name);
		while(true){


		 receive = new byte[max_size-1];

		 ack_array = new byte[1];

		message = new byte[max_size];

		

		ack_str = String.valueOf(ack);

		ack_array=ack_str.getBytes();



		try{
			is = new FileInputStream(file);
			is.skip(offset);
			is.read(receive,0,max_size-1);


			message=joinByteArray(ack_array,receive);


			if(is.read(receive,0,max_size-1)==-1){

				endof =1;
					
			}

			dp = new DatagramPacket(message,message.length, ip, Integer.parseInt(args[1]));

			ds.send(dp);

			
			ds.setSoTimeout(timer);



			while(true){
				try{
				offset+=dp.getLength();
				
				
    			ds.receive(dp);

    			

    			


    			String strRecv = new String(dp.getData(), 0, dp.getLength());

    			System.out.println(strRecv);


    			char a = Character.forDigit(ack, 10);;
    			char b = strRecv.charAt(0);
    			

    			if(a==b){
    				

    				if(ack ==1){
    					ack = 0;
    					
    				}
    				else if (ack==0) {
    				
    					ack =1;
    					
    				}
    				
    				message= null;
    				receive= null;
    				ack_array=null;
    				


    				break;

    			}


    		}catch (SocketTimeoutException e) {
        		break;  // Closing here would cause a SocketException
    		}

    		ds.send(dp);
    		ds.setSoTimeout(timer);

			}

			if(endof==1){
				break;
			}




		}catch(Exception e) {
         // if any I/O error occurs
         e.printStackTrace();
      }

  }

  		String end = String.valueOf(ack) + "EOF";


      	dp = new DatagramPacket(end.getBytes(),end.length(), ip,  Integer.parseInt(args[1]));

		ds.send(dp);
		ds.setSoTimeout(timer);


		while(true){
				try{

				offset+=dp.getLength();

    			ds.receive(dp);

    			

    			System.out.println("Received");
    			String strRecv = new String(dp.getData(), 0, dp.getLength());

    			System.out.println(strRecv);


    			char a = Character.forDigit(ack, 10);;
    			char b = strRecv.charAt(0);
    			

    			if(a==b){
    				

    				if(ack ==1){
    					ack = 0;
    					
    				}
    				else if (ack==0) {
    				
    					ack =1;
    					System.out.println(ack);
    				}
    				
    				message= null;
    				receive= null;
    				ack_array=null;	
    				break;

    			}

    		}catch (SocketTimeoutException e) {
        		break;  // Closing here would cause a SocketException
    		}
    		ds.send(dp);
    		ds.setSoTimeout(timer);
			}


	ds.close();
	System.out.println(System.currentTimeMillis());
	}
	public static byte[] joinByteArray(byte[] byte1, byte[] byte2) {

        return ByteBuffer.allocate(byte1.length + byte2.length)
                .put(byte1)
                .put(byte2)
                .array();

    }






}

