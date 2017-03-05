package uk.co.ohpollux.me.fore.deep.to;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class TDFMLevel extends ApplicationAdapter {
    private SpriteBatch batch;
    private BitmapFont font;
    private ArrayList<TweetObject> tweetsOnScreen = new ArrayList<TweetObject>();
    private GlyphLayout layout;
    private Texture itsFullOfstars;
    private Texture groundMid;
    private Vector2 groundMidPosition;
    private Texture groundRight;
    private Vector2 groundRightPosition;
    private Texture groundLeft;
    private Vector2 groundLeftPosition;
    private int groundStart = 210;
    private int groundSpeed = 1;

    private int noOfFrames = 5;
    private int heightOfWalker = 100;

    private Animation<TextureRegion> walkAnimation;
    private Texture dudeSpritesheet;
    private float stateTime;

    private boolean walkingRight;

    @Override
    public void create() {
	Utils.setProperties();
	TwitterLogic.populateTweets();
	font = Utils.getFont();
	batch = new SpriteBatch();
	layout = new GlyphLayout();

	itsFullOfstars = new Texture(Gdx.files.internal("images/background.png"));
	groundMid = new Texture(Gdx.files.internal("images/ground.png"));
	groundMidPosition = new Vector2(0, 0 - groundMid.getHeight() + groundStart);
	groundRight = new Texture(Gdx.files.internal("images/ground.png"));
	groundRightPosition = new Vector2(groundMidPosition.x + groundMid.getWidth(), groundMidPosition.y);
	groundLeft = new Texture(Gdx.files.internal("images/ground.png"));
	groundLeftPosition = new Vector2(groundMidPosition.x - groundMid.getWidth(), groundMidPosition.y);
	dudeSpritesheet = new Texture(Gdx.files.internal("images/spritesheet.png"));

	setWalkAnimation();
	addNewTweets(true, true);

	stateTime = 0f;
	walkingRight = true;
    }

    private void setWalkAnimation() {
	TextureRegion[][] tmp = TextureRegion.split(dudeSpritesheet, dudeSpritesheet.getWidth() / noOfFrames,
		dudeSpritesheet.getHeight());

	TextureRegion[] walkFrames = new TextureRegion[noOfFrames];
	int index = 0;

	for (int j = 0; j < noOfFrames; j++) {
	    walkFrames[index++] = tmp[0][j];
	}

	walkAnimation = new Animation<TextureRegion>(0.15f, walkFrames);
	walkAnimation.setPlayMode(PlayMode.LOOP_PINGPONG);
    }

    private void addNewTweets(boolean movedRight, boolean firstRun) {
	while (tweetsOnScreen.size() < Integer.parseInt(Utils.numberOfTweetsOnScreen)) {
	    String tweetText = TwitterLogic.getRandomTweet();
	    layout.setText(font, tweetText);

	    Random rand = new Random();
	    int y = getY(rand);
	    int x = getX(movedRight, firstRun, rand);

	    TweetObject aTweet = new TweetObject(tweetText, new Vector2(x, y));
	    tweetsOnScreen.add(aTweet);
	}
    }

    private int getY(Random rand) {
	int y = getRandY(rand);

	// I don't want the tweets to collide on the y axis, there's plenty
	// of screen space
	Iterator<TweetObject> iter = tweetsOnScreen.iterator();
	while (iter.hasNext()) {
	    TweetObject aTweet = iter.next();

	    if (y >= aTweet.getPosition().y - layout.height * 2 && y <= aTweet.getPosition().y + layout.height * 2) {
		y = getRandY(rand);
		break;
	    }
	}
	return y;
    }

    private int getRandY(Random rand) {
	return rand.nextInt((Gdx.graphics.getHeight() - groundStart - heightOfWalker)) + groundStart + heightOfWalker;
	// return rand.nextInt((Gdx.graphics.getHeight() / 3) * 2) +
	// (Gdx.graphics.getHeight() / 3);
    }

    private int getX(boolean movedRight, boolean firstRun, Random rand) {
	int x = 0;

	if (firstRun) {
	    x = rand.nextInt((int) Gdx.graphics.getWidth() + (int) layout.width) - (int) layout.width + 2;
	} else {
	    if (movedRight) {
		x = Gdx.graphics.getWidth() - 2;
	    } else {
		x = (int) (0 - layout.width + 2);
	    }
	}
	return x;
    }

    @Override
    public void render() {
	Gdx.gl.glClearColor(0, 0, 0, 1);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	move();
	checkGround();

	batch.begin();

	batch.draw(itsFullOfstars, 0, 0);
	batch.draw(groundMid, groundMidPosition.x, groundMidPosition.y);
	batch.draw(groundRight, groundRightPosition.x, groundRightPosition.y);
	batch.draw(groundLeft, groundLeftPosition.x, groundLeftPosition.y);

	Iterator<TweetObject> iter = tweetsOnScreen.iterator();
	while (iter.hasNext()) {
	    TweetObject aTweet = iter.next();
	    font.draw(batch, aTweet.getText(), aTweet.getPosition().x, aTweet.getPosition().y);
	}

	TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);
	batch.draw(currentFrame,
		walkingRight ? (Gdx.graphics.getWidth() / 2)
			: (Gdx.graphics.getWidth() / 2) + currentFrame.getRegionWidth(),
		groundStart - 30, walkingRight ? currentFrame.getRegionWidth() : -currentFrame.getRegionWidth(),
		currentFrame.getRegionHeight());

	batch.end();
    }

    private void move() {
	if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
	    walkingRight = true;

	    updateTweets(true);
	    groundMidPosition.x -= groundSpeed;
	    groundRightPosition.x -= groundSpeed;
	    groundLeftPosition.x -= groundSpeed;

	    stateTime += Gdx.graphics.getDeltaTime();
	} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
	    walkingRight = false;

	    updateTweets(false);
	    groundMidPosition.x += groundSpeed;
	    groundRightPosition.x += groundSpeed;
	    groundLeftPosition.x += groundSpeed;

	    stateTime += Gdx.graphics.getDeltaTime();
	} else if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
	    Gdx.app.exit();
	}
    }

    public void updateTweets(boolean movingRight) {
	Iterator<TweetObject> iter = tweetsOnScreen.iterator();

	while (iter.hasNext()) {
	    TweetObject aTweet = iter.next();

	    if (movingRight) {
		aTweet.moveRight();
	    } else {
		aTweet.moveLeft();
	    }

	    layout.setText(font, aTweet.getText());

	    if (aTweet.getPosition().x > Gdx.graphics.getWidth() || aTweet.getPosition().x + layout.width < 0) {
		iter.remove();
	    }
	}

	addNewTweets(movingRight, false);
    }

    private void checkGround() {
	if (groundLeftPosition.x <= 0 - groundMid.getWidth() * 2) {
	    groundLeftPosition.x = groundRightPosition.x + groundMid.getWidth();
	} else if (groundLeftPosition.x >= groundMid.getWidth() * 2) {
	    groundLeftPosition.x = groundMidPosition.x - groundMid.getWidth();
	}

	if (groundMidPosition.x <= 0 - groundMid.getWidth() * 2) {
	    groundMidPosition.x = groundLeftPosition.x + groundMid.getWidth();
	} else if (groundMidPosition.x >= groundMid.getWidth() * 2) {
	    groundMidPosition.x = groundRightPosition.x - groundMid.getWidth();
	}

	if (groundRightPosition.x <= 0 - groundMid.getWidth() * 2) {
	    groundRightPosition.x = groundMidPosition.x + groundMid.getWidth();
	} else if (groundRightPosition.x >= groundMid.getWidth() * 2) {
	    groundRightPosition.x = groundLeftPosition.x - groundMid.getWidth();
	}
    }

    @Override
    public void dispose() {
	batch.dispose();
    }

    // TODO: Stop using after we're done
    public void populateWithTestData() {
	tweetsOnScreen.add(new TweetObject("What if dinosaurs had a Jesus?", new Vector2(100, 840)));
	tweetsOnScreen.add(new TweetObject("If the Earth is flying through space why doesn't water wobble?",
		new Vector2(200, 400)));
	tweetsOnScreen.add(new TweetObject("If we came from monkeys, why are there monkeys?", new Vector2(340, 600)));
	tweetsOnScreen.add(new TweetObject("Evolution is just a theory", new Vector2(401, 240)));
	tweetsOnScreen.add(new TweetObject("Horoscopes are real", new Vector2(240, 401)));
	tweetsOnScreen.add(new TweetObject("UFOs abduct 1,000 people a year, wake up sheeple", new Vector2(600, 340)));
	tweetsOnScreen.add(new TweetObject("Read four interesting quantum mechanics books today before breakfast",
		new Vector2(400, 200)));
	tweetsOnScreen.add(new TweetObject(
		"Explained to my professor today that he didn't really understand thermodynamics, he's such a tool",
		new Vector2(840, 100)));
	tweetsOnScreen.add(new TweetObject("The world came to an end in April the 5th of 2010", new Vector2(798, 300)));
	tweetsOnScreen.add(new TweetObject("Crystals can heal, man", new Vector2(555, 555)));
    }
}
