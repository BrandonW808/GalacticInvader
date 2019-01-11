package com.example.brandonward.galacticinvader;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Alien {
    private Bitmap image;
    private int hp = 0;
    private int speed = 1;
    private float xSpeed = 1;
    private float ySpeed = 1;
    private int cooldown = 10;
    private int strength = 1;
    private float x;
    private float y;
    private int width;
    private int height;

    public Alien(int health, int spd, int c, int strngth, Bitmap bmp, int w, int h){
        hp = health;
        xSpeed = spd;
        ySpeed = spd;
        cooldown = c;
        strength = strngth;
        image = bmp;
        width = w;
        height = h;
    }


    public int getHP(){ return hp; }
    public float getXSpeed(){ return xSpeed; }
    public float getYSpeed(){ return ySpeed; }
    public int getSpeed(){ return speed; }
    public int getCooldown(){ return cooldown; }
    public int getStrength(){ return strength; }
    public int getWidth(){ return width; }
    public int getHeight(){ return height; }
    public float getX(){ return x; }
    public float getY(){ return y; }
    public Bitmap getBitmap(){ return image; }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x - image.getWidth()/2, y - image.getHeight()/2, null);
    }

    public synchronized void setX(float newX){ x = newX; }
    public synchronized void setY(float newY){ y = newY; }
    public synchronized void setXSpeed(float newSpeed){ xSpeed = newSpeed; }
    public synchronized void setYSpeed(float newSpeed){ ySpeed = newSpeed; }

    public void setBitmap(Bitmap b){
        image = b;
    }
}
