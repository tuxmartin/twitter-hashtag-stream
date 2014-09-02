package eu.vancl.martin.twitter.start;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import eu.vancl.martin.twitter.service.TcpClient;
import eu.vancl.martin.twitter.service.TwitterStreamFind;
import eu.vancl.martin.twitter.service.UDPClient;

/* ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! 
 * Before start app run
 *    socat TCP4-LISTEN:7777,fork /dev/ttyUSB0,b9600,raw
 * ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! 
 */

public class Start {
	static final Logger log = Logger.getLogger(Start.class); 	

	//static UDPClient udpClient;
	static TcpClient tcp;
	
	public static void main(String[] args) throws InterruptedException {
		DOMConfigurator.configure("log4j.xml");

    	log.info("Starting application...");        

        //udpClient = new UDPClient("127.0.0.1", 1234);
        
        tcp = new TcpClient(new Start());
        tcp.connect();
        //tcp.sendMessage("Ahoj svete!");
        //Thread.sleep(50);
        //tcp.disconnect();
        
        // Search on Twitter:
        String keywords[] = {"linux"}; // "#linux"

        TwitterStreamFind simpleStream = new TwitterStreamFind(new Start(), keywords);
        simpleStream.start();      
        
                
        log.info("---EXIT---"); 
	}
	
	public void tcpReceive(String input) {		
		System.out.println("RS-232___ " + input);
	}
	
	public void sendTweet(String tweet) {				
		// UTF-8 tweet:
		//udpClient.send("\n" + tweet + "\n");		
		//tcp.sendMessage("\n" + tweet + "\n");
		
		// ASCII tweet:
		try {
			byte[] asciiTweet = tweet.getBytes("UTF-8");
			//udpClient.send("\n" + new String(asciiTweet, "US-ASCII") + "\n");
			tcp.sendMessage("\n" + new String(asciiTweet, "US-ASCII") + "\n");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
	}
}
