package eu.vancl.martin.twitter.service;

import java.util.ArrayList;
import java.util.List;

import eu.vancl.martin.twitter.start.Start;

public class SendTweetToSocket extends Thread {

	private List<String> tweets = new ArrayList<String>();

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
				tcp.sendMessage(tweets.get(0));
				tweets.remove(0);
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<String> getTweets() {
		return tweets;
	}
	
	public void setTweets(List<String> tweets) {
		this.tweets = tweets;
	}

}
