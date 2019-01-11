package com.example.brandonward.galacticinvader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.MainThread;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "Game View: ";
    private GameThread thread;
    private Invader player;
    private ArrayList<Alien> aliens;
    volatile float touched_x, touched_y;
    private float mCurrAngle = 0;
    private double mPrevAngle = 0;
    private int playerX = 0;
    private int playerY = 0;
    private ImageView invader;

    public ArrayList<Alien> getAliens() { return aliens; }
    public Invader getPlayer(){ return player; }
    public int getPlayerX(){ return playerX; }
    public int getPlayerY(){ return playerY; }

    public synchronized void setAliens(ArrayList<Alien> a){
        aliens = a;
    }

    public GameView(Context context) {
        super(context);
        thread = new GameThread(getHolder(), this);
        setFocusable(true);
        getHolder().addCallback(this);
        aliens = new ArrayList<>();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        player = new Invader(0,5,0,0, BitmapFactory.decodeResource(getResources(),R.drawable.invader_initial), 25, 25);

        Random rand = new Random();
        int count = (rand.nextInt(3) + 2);
        for (int i=0; i<count; i++) {
            aliens.add(new Alien(0, 3, 0, 0, BitmapFactory.decodeResource(getResources(), R.drawable.alien_fighter_1), BitmapFactory.decodeResource(getResources(), R.drawable.alien_fighter_1).getWidth(), BitmapFactory.decodeResource(getResources(), R.drawable.alien_fighter_1).getHeight()));
        }
        for (Alien a: aliens){
            rand = new Random();
            int i = (rand.nextInt(1000) + 25);
            int j = (rand.nextInt(1000) + 25);
            a.setX(i);
            a.setY(j);
        }

        thread.setRunning(true);
        thread.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void update(){

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawColor(Color.BLACK);
            playerX = ((canvas.getWidth()/2)-(player.getWidth()/2));
            playerY = ((canvas.getHeight()/2)-(player.getHeight()/2));
            player.setX(playerX);
            player.setY(playerY);
            player.draw(canvas);
            for (Alien a : aliens){
                if (a.getX() <= canvas.getWidth() && a.getX() >= 0 && a.getY() >= 0 && a.getY() <= canvas.getHeight()) {
                    a.draw(canvas);
                }else{
                    aliens.remove(a);
                }
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                touched_x = event.getX();
                touched_y = event.getY();
                mCurrAngle = (float) Math.toDegrees(Math.atan2(touched_x - player.getX(), player.getY() - touched_y));
                calculatePlayerSpeed();
                mPrevAngle = mCurrAngle;
            }case MotionEvent.ACTION_MOVE: {
                touched_x = event.getX();
                touched_y = event.getY();
                calculatePlayerSpeed();
                mCurrAngle = (float) Math.toDegrees(Math.atan2(touched_x - player.getX(), player.getY() - touched_y));
                mPrevAngle = mCurrAngle;

            }

        }

        player.setX(playerX);
        player.setY(playerY);

        return true;
    }

    private void calculatePlayerSpeed(){
        float newXSpeed = (Math.abs(touched_x-playerX));
        float newYSpeed = (Math.abs(touched_y-playerY));
        float total = player.getSpeed()/(newXSpeed+newYSpeed);
        if(playerX > touched_x) {
            player.setXSpeed(total * newXSpeed * -1);
        }else{
            player.setXSpeed(total * newXSpeed);
        }
        if (playerY > touched_y) {
            player.setYSpeed(total * newYSpeed *-1);
        }else {
            player.setYSpeed(total * newYSpeed);
        }
        for (int i=0; i<aliens.size(); i++) {
            Log.i(TAG, "X SPEED: " + player.getXSpeed() + " Y SPEED: " + player.getYSpeed() + " VS \n" + aliens.get(i).getXSpeed() + " and " + aliens.get(i).getYSpeed());
        }
    }

}