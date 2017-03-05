package uk.co.ohpollux.me.fore.deep.to;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterLogic {
    private static ArrayList<String> tweetsList = new ArrayList<String>();
    private static boolean ableToGetTweets = true;

    public static void populateTweets() {
	ArrayList<Status> tweets = new ArrayList<Status>();
	int maxTweets = Integer.parseInt(Utils.numberOfTweets);
	Query query = new Query(Utils.tag);

	ConfigurationBuilder configBuilder = new ConfigurationBuilder();

	configBuilder.setDebugEnabled(true).setOAuthConsumerKey(Utils.oAuthConsumerKey)
		.setOAuthConsumerSecret(Utils.oAuthConsumerSecret).setOAuthAccessToken(Utils.oAuthAccessToken)
		.setOAuthAccessTokenSecret(Utils.oAuthAccessTokenSecret);

	TwitterFactory twitterFactory = new TwitterFactory(configBuilder.build());
	Twitter twitter = twitterFactory.getInstance();

	while (tweets.size() < maxTweets && ableToGetTweets) {
	    if (maxTweets - tweets.size() > 10) {
		query.setCount(10);
	    } else {
		query.setCount(maxTweets - tweets.size());
	    }

	    try {
		QueryResult result = twitter.search(query);
		tweets.addAll(result.getTweets());
	    } catch (Exception te) {
		ableToGetTweets = false;
	    }
	}

	Iterator<Status> iter = tweets.iterator();
	while (iter.hasNext()) {
	    Status status = iter.next();
	    tweetsList.add(status.getText());
	}
    }

    public static String getRandomTweet() {
	if (tweetsList.isEmpty()) {
	    return "How can mirrors be real when our eyes aren't real?";
	} else {
	    Random randIndex = new Random();
	    int index = randIndex.nextInt(tweetsList.size() - 1);
	    return tweetsList.get(index);
	}
    }
}
