package eu.vancl.martin.twitter.service;

import java.util.Date;

import org.apache.log4j.Logger;

import eu.vancl.martin.twitter.start.Start;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;

// For debug edit "twitter4j.properties"
public class TwitterStreamFind extends Thread {
	static final Logger log = Logger.getLogger(TwitterStreamFind.class);
	
	Start instance;
	String keywords[];
	
	public TwitterStreamFind(Start instance, String keywords[]) {
		this.keywords = keywords;
		this.instance = instance;
	}
	
	@Override
	public void run() {
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();

        StatusListener listener = new StatusListener() {

            @Override
            public void onException(Exception arg0) {
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice arg0) {
            }

            @Override
            public void onScrubGeo(long arg0, long arg1) {
            }        

            @Override
            public void onTrackLimitationNotice(int arg0) {
            }

			@Override
			public void onStallWarning(StallWarning arg0) {				
			}
			
			@Override
            public void onStatus(Status status) {        	
                User user = status.getUser();
                
                String tweet;
            	tweet = "---------------------------------------" + "\n"
            			+ new Date() + "\n"
            			+ "ID= " + status.getId() + "\n"
            			+ "USER= " + status.getUser().getScreenName() + "\n"
            			+ "TEXT= " + status.getText() + "\n"
            			+ "---------------------------------------";
            	log.debug(tweet);
            	instance.sendTweet(" @" + status.getUser().getScreenName() + ": " + status.getText());
            }

        };
        FilterQuery fq = new FilterQuery();

        fq.track(keywords);

        twitterStream.addListener(listener);
        twitterStream.filter(fq);  

    }
}