package com.example.flappyahin;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class CharacterSprite {
    private Bitmap image;
    public int x, y;
    private int xVelocity = 10;
    public int yVelocity = 10;


    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;


    public CharacterSprite(Bitmap bmp) {
        image = bmp;
        x = 120;
        y = 250;

    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);


    }

    public void update() throws InterruptedException {

        for (int i = 0; i <= 4; i++) {
            y += i;
            Thread.sleep(2);

        }
    }
}




