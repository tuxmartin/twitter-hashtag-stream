package eu.vancl.martin.twitter.service;

import java.util.LinkedList;
import java.util.Queue;

import eu.vancl.martin.twitter.start.Start;

public class SendTweetToSocket extends Thread {

	private Queue<String> tweets = new LinkedList<String>();
	
	private boolean busy = false;

	TcpClient tcp;

	@Override
	public void run() {

		tcp = new TcpClient(new Start());
		tcp.connect();
		// tcp.sendMessage("Ahoj svete!");
		// Thread.sleep(50);
		// tcp.disconnect();

		while (true) {
			if (!tweets.isEmpty()) {
				tcp.sendMessage(tweets.poll()); // Retrieves and removes the head of this queue, or returns null if this queue is empty.
			}
			
			try {
				if (busy) { // printer is busy
					Thread.sleep(1000); // FIXME: wait for "OK" signal from Arduino
				} else {
					Thread.sleep(10);
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public Queue<String> getTweets() {
		return tweets;
	}

	public void setTweets(Queue<String> tweets) {
		this.tweets = tweets;
	}

	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}
	
	
}
