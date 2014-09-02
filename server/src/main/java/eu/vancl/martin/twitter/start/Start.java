package eu.vancl.martin.twitter.start;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import eu.vancl.martin.twitter.service.SendTweetToSocket;
import eu.vancl.martin.twitter.service.TcpClient;
import eu.vancl.martin.twitter.service.TwitterStreamFind;

/* ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! 
 * Before start app run
 *    socat TCP4-LISTEN:7777,fork /dev/ttyUSB0,b9600,raw
 * ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! 
 */

public class Start {
	static final Logger log = Logger.getLogger(Start.class); 	

	boolean waitForPrinter = false;
	
	private static SendTweetToSocket sendTweet;
	
	public static void main(String[] args) throws InterruptedException {
		DOMConfigurator.configure("log4j.xml");

    	log.info("Starting application...");    
    	
    	sendTweet = new SendTweetToSocket();
    	sendTweet.start();
                
        // Search on Twitter:
        String keywords[] = {"linux"}; // "#linux"
        
        TwitterStreamFind simpleStream = new TwitterStreamFind(new Start(), keywords);
        simpleStream.start();      
        
                
        log.info("---EXIT---"); 
	}
	
	public void tcpReceive(String input) {		
		System.out.println("RS-232___ " + input);
		
		if (input.contains("BUSY")) {
			waitForPrinter = true;			
		}
	}
	
	public void addTweetToList(String tweet) {				
		// UTF-8 tweet:
		//tcp.sendMessage("\n" + tweet + "\n");
		
		// ASCII tweet:
		try {
			byte[] asciiTweet = tweet.getBytes("UTF-8");
			sendTweet.getTweets().add("\n" + new String(asciiTweet, "US-ASCII") + "\n");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
	}
}
