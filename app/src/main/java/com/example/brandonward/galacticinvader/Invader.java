package com.example.brandonward.galacticinvader;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Invader {
    private Bitmap image;
    private int hp = 0;
    private float xSpeed = 0;
    private float ySpeed = 0;
    private int speed = 1;
    private int cooldown = 10;
    private int strength = 1;
    private int x;
    private int y;
    private int width;
    private int height;


    public Invader(int health, int spd, int c, int strngth, Bitmap bmp, int w, int h){
        hp = health;
        speed = spd;
        cooldown = c;
        strength = strngth;
        image = bmp;
        width = w;
        height = h;
    }


    public int getHP(){ return hp; }
    public int getSpeed(){ return speed; }
    public int getCooldown(){ return cooldown; }
    public int getStrength(){ return strength; }
    public int getWidth(){ return width; }
    public int getHeight(){ return height; }
    public int getX(){ return x; }
    public int getY(){ return y; }
    public float getXSpeed(){ return xSpeed; }
    public float getYSpeed(){ return ySpeed; }
    public Bitmap getBitmap(){ return image; }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x - image.getWidth()/2, y - image.getHeight()/2, null);
    }

    public void setX(int newX){
        x = newX;
    }

    public void setY(int newY){
        y = newY;
    }

    public void setBitmap(Bitmap b){
        image = b;
    }

    public void setXSpeed(float f){ xSpeed = f; }
    public void setYSpeed(float f){ ySpeed = f; }

}
