package com.example.brandonward.galacticinvader;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.ArrayList;

public class GameThread extends Thread {
    private static final String TAG = "Game Thread: ";

    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private boolean running = true;
    public static Canvas canvas;
    private int targetFPS = 60;
    private long averageFPS;

    public GameThread(SurfaceHolder surfaceHolder, GameView gameView) {

        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;

    }

    @Override
    public void run(){
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        long targetTime = 1000 / targetFPS;

        while(running){
            startTime = System.nanoTime();
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized(surfaceHolder) {
                    this.gameView.update();
                    this.gameView.draw(canvas);
                }
            } catch (Exception e) {} finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            synchronized(gameView.getAliens()) {
                gameView.setAliens(updateAliens(gameView.getAliens()));
            }


            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;

            try {
                this.sleep(waitTime);
            } catch (Exception e) {}

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == targetFPS)        {
                averageFPS = 1000 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
            }

        }
    }

    public ArrayList<Alien> updateAliens(ArrayList<Alien> aliens){
        synchronized(aliens) {
            for (Alien alien : aliens) {

                float newXSpeed = (Math.abs(gameView.getPlayerX() - alien.getX()));
                float newYSpeed = (Math.abs(gameView.getPlayerY() - alien.getY()));
                float total = alien.getSpeed() / (newXSpeed + newYSpeed);

                if (gameView.getPlayerX() > alien.getX()) {
                    alien.setXSpeed((total * newXSpeed) - gameView.getPlayer().getXSpeed());
                } else {
                    alien.setXSpeed((total * newXSpeed) - gameView.getPlayer().getXSpeed() * -1);
                }

                if (gameView.getPlayerY() > alien.getY()) {
                    alien.setYSpeed(total * newYSpeed - gameView.getPlayer().getYSpeed());
                } else {
                    alien.setYSpeed(total * newYSpeed - gameView.getPlayer().getYSpeed() * -1);
                }


                checkCollisions();
                alien.setX(alien.getX() + alien.getXSpeed());
                alien.setY(alien.getY() + alien.getYSpeed());
            }
        }

        return aliens;
    }

    public void checkCollisions(){
        synchronized(gameView.getAliens()) {
            for (Alien alien : gameView.getAliens()) {
                if (alien.getX() + alien.getWidth() >= gameView.getPlayerX() && alien.getX() <= gameView.getPlayerX() + gameView.getPlayer().getWidth() &&
                        (alien.getY() + alien.getHeight() >= gameView.getPlayerY() || gameView.getPlayerY() <= alien.getY())) {
                    Log.i(TAG, "HIT");
                    ArrayList<Alien> a = gameView.getAliens();
                    explode(alien);
                }
            }
        }
    }

    public void explode(Alien alien){
        alien.setBitmap(BitmapFactory.decodeResource(gameView.getResources(),R.drawable.explode1));
        alien.draw(canvas);
        Log.i(TAG, "HiT");
        try {
            alien.setBitmap(BitmapFactory.decodeResource(gameView.getResources(),R.drawable.explode2));
            alien.draw(canvas);
            alien.setBitmap(BitmapFactory.decodeResource(gameView.getResources(),R.drawable.explode3));
            alien.draw(canvas);
            alien.setBitmap(BitmapFactory.decodeResource(gameView.getResources(),R.drawable.explode4));
            alien.draw(canvas);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setRunning(boolean isRunning) {
        running = isRunning;
    }

}

