package com.example.flappyahin;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private CharacterSprite characterSprite;
    public PipeSprite pipe1, pipe2, pipe3;
    public static int gapHeight = 500;
    public static int velocity = 15;
    private Background bg;
    public static int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    public static int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;





    public GameView(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        setFocusable(true);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        for(int i=0;i<characterSprite.yVelocity*10;i++){
            try { Thread.sleep(0,25); } catch (InterruptedException e) { e.printStackTrace(); }
            characterSprite.y = characterSprite.y - 1;
        }
        return super.onTouchEvent(event);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        makeLevel();

        bg = new Background(getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.backgroundhava), screenWidth, screenHeight));

        thread.setRunning(true);
        thread.start();


    }

    private void makeLevel() {

        characterSprite = new CharacterSprite
                (getResizedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bird), 250, 200));
        Bitmap bmp;
        Bitmap bmp2;
        int y;
        int x;
        bmp = getResizedBitmap(BitmapFactory.decodeResource
                (getResources(), R.drawable.pipe_down), 500, Resources.getSystem().getDisplayMetrics().heightPixels / 2);
        bmp2 = getResizedBitmap
                (BitmapFactory.decodeResource(getResources(), R.drawable.pipe_up), 500, Resources.getSystem().getDisplayMetrics().heightPixels / 2);

        Random r = new Random();
        int a = r.nextInt(290);
        int b = r.nextInt(290);
        int c = r.nextInt(290);

        pipe1 = new PipeSprite(bmp, bmp2, 2000, a);
        pipe2 = new PipeSprite(bmp, bmp2, 3000, b);
        pipe3 = new PipeSprite(bmp, bmp2, 4000, c);

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

    public void update() throws InterruptedException {
        logic();
        characterSprite.update();
        bg.update();
        pipe1.update();
        pipe2.update();
        pipe3.update();


    }

    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);
        if (canvas != null) {
            bg.draw(canvas);
            characterSprite.draw(canvas);
            pipe1.draw(canvas);
            pipe2.draw(canvas);
            pipe3.draw(canvas);
        }
    }

        public void logic () throws InterruptedException {

            List<PipeSprite> pipes = new ArrayList<>();
            pipes.add(pipe1);
            pipes.add(pipe2);
            pipes.add(pipe3);


            for (int i = 0; i < pipes.size(); i++) {
                if (characterSprite.y < pipes.get(i).yY + (screenHeight / 2) - (gapHeight / 2) && characterSprite.x + 300 > pipes.get(i).xX && characterSprite.x < pipes.get(i).xX + 500) {
                    makeLevel();
                } else if (characterSprite.y + 240 > (screenHeight / 2) + (gapHeight / 2) + pipes.get(i).yY && characterSprite.x + 300 > pipes.get(i).xX && characterSprite.x < pipes.get(i).xX + 500) {
                    makeLevel();
                }

                if (pipes.get(i).xX + 500 < 0) {
                    Random r = new Random();
                    int value1 = r.nextInt(500);
                    int value2 = r.nextInt(500);
                    pipes.get(i).yY = value2 - 250;
                }
            }

            if (characterSprite.y + 240 < 0) {
                makeLevel();
            }
            if (characterSprite.y > screenHeight) {
                makeLevel();
            }

            while (characterSprite.x > pipe3.xX) {
                addpipe();
            }

        }


    private void addpipe () {

            Bitmap bmp;
            Bitmap bmp2;
            bmp = getResizedBitmap(BitmapFactory.decodeResource
                    (getResources(), R.drawable.pipe_down), 500, Resources.getSystem().getDisplayMetrics().heightPixels / 2);
            bmp2 = getResizedBitmap
                    (BitmapFactory.decodeResource(getResources(), R.drawable.pipe_up), 500, Resources.getSystem().getDisplayMetrics().heightPixels / 2);

            Random r = new Random();

            int b = r.nextInt(290);
            int c = r.nextInt(290);

            pipe1 = new PipeSprite(bmp, bmp2, pipe3.xX, pipe3.yY);
            pipe2 = new PipeSprite(bmp, bmp2, pipe3.xX + 1000, b);
            pipe3 = new PipeSprite(bmp, bmp2, pipe3.xX + 2000, c);

    }
}

