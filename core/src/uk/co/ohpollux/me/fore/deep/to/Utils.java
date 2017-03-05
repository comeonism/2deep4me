package uk.co.ohpollux.me.fore.deep.to;

import java.util.Properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class Utils {
    public static String oAuthConsumerKey = "";
    public static String oAuthConsumerSecret = "";
    public static String oAuthAccessToken = "";
    public static String oAuthAccessTokenSecret = "";
    public static String tag = "";
    public static String numberOfTweets = "";
    public static String numberOfTweetsOnScreen = "";

    public static void setProperties() {
	try {
	    Properties props = new Properties();
	    FileHandle internal = Gdx.files.internal("config.properties");

	    if (internal.read() != null) {
		props.load(internal.read());
	    }

	    oAuthConsumerKey = props.getProperty("OAuthConsumerKey");
	    oAuthConsumerSecret = props.getProperty("OAuthConsumerSecret");
	    oAuthAccessToken = props.getProperty("OAuthAccessToken");
	    oAuthAccessTokenSecret = props.getProperty("OAuthAccessTokenSecret");
	    tag = props.getProperty("tag");
	    numberOfTweets = props.getProperty("tweetNumber");
	    numberOfTweetsOnScreen = props.getProperty("tweetsOnScreen");
	} catch (Exception e) {
	    System.out.println("File not found :(    -> " + e);
	}
    }

    public static BitmapFont getFont() {
	FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Inkburro.ttf"));
	FreeTypeFontParameter parameter = new FreeTypeFontParameter();
	parameter.size = 20;
	parameter.color = Color.LIGHT_GRAY;
	BitmapFont font = generator.generateFont(parameter);
	generator.dispose();

	return font;
    }
}
