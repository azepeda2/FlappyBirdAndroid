package com.example.alejandrozepeda.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import org.w3c.dom.Text;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
    //ShapeRenderer shapeRenderer;
	Texture background;
    Texture gameover;
	Texture[] birds;
	Texture topTube;
	Texture bottomTube;
    Animation animation;
	int gameState;
    int score;
    int scoringTube;
    BitmapFont font;
    Circle birdCircle;
    Rectangle[] topRectangles;
    Rectangle[] bottomRectangles;
    float flapState;
    float birdY;
	float velocity;
	float gravity;
	float gap;
	float maximumTubeOffset;
	float[] tubeOffset;
	float[] tubeX;
	float tubeVelocity;
	int numberOfTubes;
	float distanceBetweenTubes;
	Random random;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
        //shapeRenderer = new ShapeRenderer();
		background = new Texture("bg.png");
        gameover = new Texture("gameover.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
        animation = new Animation(1f/10f, birds);
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10f);
        birdCircle = new Circle();
		random = new Random();
		gravity = 1.5f;
		gap = 500f;
		maximumTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 -100;
		tubeVelocity = 4;
		numberOfTubes = 4;
		tubeX = new float[numberOfTubes];
		tubeOffset = new float[numberOfTubes];
        topRectangles = new Rectangle[numberOfTubes];
        bottomRectangles = new Rectangle[numberOfTubes];
		distanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 4;

        startGame();
	}

	public void startGame() {
        score = 0;
        scoringTube = 0;
        flapState = 0;
        gameState = 0;
        velocity = 0;
        birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;

        for (int i = 0; i < numberOfTubes; i++) {
            tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;
            tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
            topRectangles[i] = new Rectangle();
            bottomRectangles[i] = new Rectangle();
        }
    }

	@Override
	public void render () {
        flapState += Gdx.graphics.getDeltaTime();

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameState == 1) {
            if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2) {
                score++;

                Gdx.app.log("Score", String.valueOf(score));

                if (scoringTube < numberOfTubes - 1) {
                    scoringTube++;
                } else {
                    scoringTube = 0;
                }
            }

			if (Gdx.input.justTouched()) {
				velocity = -25f;
			}

			for (int i = 0; i < numberOfTubes; i++) {
				if (tubeX[i] < - topTube.getWidth()) {
					tubeX[i] += numberOfTubes * distanceBetweenTubes;
				} else {
					tubeX[i] = tubeX[i] - tubeVelocity;
				}

				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

                topRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
                bottomRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
			}

			if (birdY > 0) {
				velocity += gravity;
				birdY -= velocity;
			} else {
                gameState = 2;
            }

		} else if (gameState == 0){
			if (Gdx.input.justTouched()) {
				Gdx.app.log("Touched", "Begin Game");
				gameState = 1;
			}
		} else if (gameState == 2){
            batch.draw(gameover, Gdx.graphics.getWidth() / 2 - gameover.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameover.getHeight() / 2);

            if (Gdx.input.justTouched()) {
                startGame();
            }
        }

	/*	if (flapState == 0) {
			flapState = 1;
		} else {
			flapState = 0;
		}*/

		batch.draw((Texture) animation.getKeyFrame(flapState, true), Gdx.graphics.getWidth() / 2 - birds[0].getWidth() / 2, birdY);
        font.draw(batch, String.valueOf(score), 100, 200);
		batch.end();

        birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[0].getHeight() / 2, birds[0].getWidth() / 2);

        /* shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);*/

        for (int i = 0; i < numberOfTubes; i++) {
           /* shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
            shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());*/

            if (gameState == 1 && (Intersector.overlaps(birdCircle, topRectangles[i]) || Intersector.overlaps(birdCircle, bottomRectangles[i]))) {
                Gdx.app.log("Collision", "Bird collided with tube!");
                gameState = 2;
            }
        }
        //shapeRenderer.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
