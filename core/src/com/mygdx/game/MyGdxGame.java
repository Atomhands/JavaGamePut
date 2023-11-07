package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.awt.*;
import java.util.Iterator;

public class MyGdxGame extends ApplicationAdapter implements InputProcessor {
    Texture img;
    Texture rainImg;
    Texture bucketImg;
    Texture back;
    Texture fireImg;
    private int score;
    private BitmapFont font;
    // 相机和 SpriteBatch
    private OrthographicCamera camera;
    SpriteBatch batch;
    //图形
    private Rectangle bucket;
    private Array<Rectangle> rain;
    private Array<Rectangle> fire;
    private long lastDropRainTime;
    private long lastFireTime;
    private boolean isStart;

    @Override
    public void create() {
        Gdx.input.setInputProcessor(this);
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        rainImg = new Texture("photo/Rain.png");
        bucketImg = new Texture("photo/bucket.jpg");
        back = new Texture("photo/back.png");
        fireImg = new Texture("photo/fire.jpg");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 960);
        score = 0;
        //bucket
        bucket = new Rectangle();
        bucket.x = 600;
        bucket.y = 20;
        bucket.width = 64;
        bucket.height = 64;

        isStart = false;
        font = new BitmapFont();
        font.setColor(0.25f, 0.8f, 0.16f, 0.9f);
        font.getData().setScale(3);

        rain = new Array<>();
        fire = new Array<>();
        rainDrop();
        fireShot();
    }

    @Override
    public void render() {
        //判断是否开始
        if (!isStart) {
            //渲染
            ScreenUtils.clear(1, 1, 1, 1);
            //Gdx.gl.glClearColor(1, 1, 1, 1);
            //Gdx.gl.glClear(GL20.GL_STENCIL_VALUE_MASK);
            camera.update();
            batch.setProjectionMatrix(camera.combined);

            batch.begin();

            batch.draw(back, 0, 0, 1200, 960);
            batch.draw(bucketImg, bucket.x, bucket.y);

            for (Rectangle rainD :
                    rain) {
                batch.draw(rainImg, rainD.x, rainD.y);
            }
            for (Rectangle fireS :
                    fire) {
                batch.draw(rainImg, fireS.x, fireS.y);
            }
            //雨滴下落
            //发射fire
            if (TimeUtils.nanoTime() - lastDropRainTime > 500000000) rainDrop();
            for (Iterator<Rectangle> iter = rain.iterator(); iter.hasNext(); ) {
                Rectangle nextDrop = iter.next();
                nextDrop.y -= 200 * Gdx.graphics.getDeltaTime();
                if (nextDrop.y < 0) iter.remove();
                //接住雨滴
                if (nextDrop.overlaps(bucket)) {
                    score += 10;
                    iter.remove();
                }

                font.draw(batch, "SCORE: " + score, 160, 766);
            }
            //碰撞
            if(TimeUtils.nanoTime()-lastFireTime>500000000) fireShot();
            for (Iterator<Rectangle> iterF = fire.iterator(); iterF.hasNext(); ) {
                Rectangle nextShot = iterF.next();
                nextShot.y += 500 * Gdx.graphics.getDeltaTime();
                if (nextShot.y > 960) iterF.remove();
                for (Rectangle rainDrop :
                        rain) {
                    if(rainDrop.overlaps(nextShot)){
                        score += 30;
                        iterF.remove();
                        rain.removeValue(rainDrop,true);
                    }
                }
            }
            batch.end();
            //鼠标控制bucket
            if (Gdx.input.isTouched()) {
                final Vector3 touch = new Vector3();
                touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touch);
                bucket.x = (int) (touch.x - 64 / 2);
            }
            //键盘控制
            if (Gdx.input.isKeyPressed(Input.Keys.A)) bucket.x -= 500 * Gdx.graphics.getDeltaTime();
            if (Gdx.input.isKeyPressed(Input.Keys.D)) bucket.x += 500 * Gdx.graphics.getDeltaTime();
            //限制范围
            if (bucket.x < 0) bucket.x = 0;
            if (bucket.x > 1200) bucket.x = 1200 - 64 * 2;
            //if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) fire.y += 500 * Gdx.graphics.getDeltaTime();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        bucketImg.dispose();
        rainImg.dispose();
        back.dispose();
        fireImg.dispose();
    }

    private void rainDrop() {
        Rectangle rainDrop = new Rectangle();
        rainDrop.x = MathUtils.random(0, 1200 - 64);
        rainDrop.y = 800;
        rainDrop.height = 64;
        rainDrop.width = 64;
        rain.add(rainDrop);
        lastDropRainTime = TimeUtils.nanoTime();
    }

    private void fireShot() {
        Rectangle fireShot = new Rectangle();
        fireShot.x = bucket.x+32;
        fireShot.y = 20;
        fireShot.width = 64;
        fireShot.height = 64;
        fire.add(fireShot);
        lastFireTime = TimeUtils.nanoTime();
    }
    private void fireShotIterator(){

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.P) {
            isStart = !isStart;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

}
