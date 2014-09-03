package eu.vancl.martin.twitter.start;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import eu.vancl.martin.twitter.service.SendTweetToSocket;
import eu.vancl.martin.twitter.service.TwitterStreamFind;

/* ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! 
 * Before start app run
 *    # chmod 777 /dev/ttyUSB0    
 *    $ socat TCP4-LISTEN:7777,fork /dev/ttyUSB0,b9600,raw
 * ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! 
 */

public class Start {
	static final Logger log = Logger.getLogger(Start.class); 	

	
	
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
		
		if (input.contains("OK")) {
			sendTweet.setBusy(false);	
		}
		
		if (input.contains("BUSY")) {
			sendTweet.setBusy(true);	
		}			
	}
	
	public void addTweetToList(String tweet) {				
		// UTF-8 tweet:
		//System.out.println("\n" + tweet + "\n");
		
		// ASCII tweet:
		try {
			byte[] asciiTweet = tweet.getBytes("UTF-8");
			String tweetToSend = "\n" + new String(asciiTweet, "US-ASCII") + "\n";
			sendTweet.getTweets().add(tweetToSend);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
	}
}
