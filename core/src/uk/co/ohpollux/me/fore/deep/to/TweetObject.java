package uk.co.ohpollux.me.fore.deep.to;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

public class TweetObject {
    private String text;
    private Vector2 position;
    private int speed;

    public TweetObject(String text, Vector2 position) {
	this.text = text;
	this.position = position;

	Random rand = new Random();
	this.speed = rand.nextInt(5) + 1;
    }

    public String getText() {
	return text;
    }

    public Vector2 getPosition() {
	return position;
    }

    public void moveLeft() {
	position.x += speed;
    }

    public void moveRight() {
	position.x -= speed;
    }
}
