package com.seinelake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class DrawGame extends View implements OnTouchListener {

    static ArrayList<LineCreator> allLines = new ArrayList<LineCreator>();
    static int draggedSquare;
    static int[] squareValues = new int[15];
    static boolean victoryScreen = false;
    static int phase = 0;
    static int score = 0;
    static int scoreThisRound = 0;
    static boolean doNotGetScoreAgain = false;
    static int mixAmount = 10;
    static boolean goToNextPuzzle = false;
    static boolean chooseNextPuzzle = true;
    static boolean chooseNextPuzzle2 = true;
    static boolean skipPuzzle = false;
    static int[][] ghostCircle = new int[8][2];
    static int[] ghostCircleValues = new int[8];
    static int gc = 0;
    static double[][] ballAnimationOffset = new double[31][2];
    static int counter = 0;
    static boolean dropScoreCalled = false;

    static boolean notDoneYet = true;

    static int[] resetState = new int[15];
    static boolean mixed = false;

    static ArrayList<LineCreator> lineAnimate = new ArrayList<LineCreator>();
    static ArrayList<Boolean> forwardAnimate = new ArrayList<Boolean>();
    static boolean queueOn = false;
    static boolean[] correctCircleNotMoving = new boolean[15];
    static int amountOfQueuedAnimations = 0;

    static boolean finished = false;
    static boolean loaded = false;

    static ChooseDifficulties[] puzzle3 = new ChooseDifficulties[3];

    static int level = 0;

    final Bitmap kugelbugel = BitmapFactory.decodeResource(getResources(), R.drawable.kugelbugel);
    final Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
    final Bitmap nextlvbg = BitmapFactory.decodeResource(getResources(), R.drawable.nextlvbg);
    final Bitmap greenFlare = BitmapFactory.decodeResource(getResources(), R.drawable.greenflare);
    final Bitmap lines[][] = new Bitmap[2][2];


    public DrawGame(Context context){

        super(context);

        lines[0][0]= BitmapFactory.decodeResource(getResources(), R.drawable.redlinevert);
        lines[0][1] = BitmapFactory.decodeResource(getResources(), R.drawable.redlinehoriz);
        lines[1][0] = BitmapFactory.decodeResource(getResources(), R.drawable.whitelinevert);
        lines[1][1] = BitmapFactory.decodeResource(getResources(), R.drawable.whitelinehoriz);

        com.Nuotta.MainActivity.resetScore(context);
        int score = com.Nuotta.MainActivity.getScore(context);
        Achievements.getLevelAndExp(score);

        chooseNextPuzzle = true;

        setOnTouchListener(this);
        setBackgroundColor(Color.BLACK);

        int a;
        for(a = 0; a < 15; a++){
            squareValues[a] = a;
            correctCircleNotMoving[a] = true;
        }
        for(a = 0; a < 8; a++){
            ghostCircle[a][0] = -100;
            ghostCircle[a][1] = -100;
        }

        Achievements.systemStartTime = SystemClock.elapsedRealtime();

    }

    @Override
    public void onDraw(Canvas canvas){

        Paint paint = new Paint();

        int a,b;

        canvas.drawBitmap(background, 0, 0, paint);

        //LINES


        paint.setColor(Color.RED);

        for(a = 0; a < allLines.size() && a < 2; a++){

            for(b = 0; b < allLines.get(a).getSize(); b++){

                int m = allLines.get(a).getSquare(b);
                int n = 0;

                if( b == allLines.get(a).getSize() - 1){

                    n = allLines.get(a).getSquare(0);

                }else{

                    n = allLines.get(a).getSquare(b + 1);
                }

                //goes over right or left side
                if( m - n == 4 || n - m == 4){

                    canvas.drawBitmap(lines[a][1], 0 , 40 + (m / 5) * 80, paint);
                    canvas.drawBitmap(lines[a][1], 400 , 40 + (m / 5) * 80, paint);

                }
                //goes over top or bottom side
                if( m - n == 10 || n - m == 10){

                    int modulo = m % 5;
                    canvas.drawBitmap(lines[a][0], modulo * 80 + 40, 0, paint );
                    canvas.drawBitmap(lines[a][0], modulo * 80 + 40, 240, paint);

                }

                //verticals
                if( n - m == 5){

                    if(m < 5){

                        canvas.drawBitmap(lines[a][0], (m + 1) * 80 - 40, 80, paint );

                    }else if(m > 9){

                        canvas.drawBitmap(lines[a][0], (m - 9) * 80 - 40, 240, paint );


                    }else{

                        canvas.drawBitmap(lines[a][0], (m - 4) * 80 - 40, 160, paint );


                    }

                }
                if( m - n == 5){

                    if(n < 5){

                        canvas.drawBitmap(lines[a][0], (n + 1) * 80 - 40, 80, paint );

                    }else if(n > 9){

                        canvas.drawBitmap(lines[a][0], (n - 9) * 80 - 40, 240, paint );


                    }else{

                        canvas.drawBitmap(lines[a][0], (n - 4) * 80 - 40 , 160, paint );


                    }

                }


                //horizontals
                if( n - m == 1){

                    if(m < 5){

                        canvas.drawBitmap(lines[a][1], (m + 1) * 80, 40, paint );

                    }else if(m > 9){

                        canvas.drawBitmap(lines[a][1], (m - 9) * 80, 200, paint );


                    }else{

                        canvas.drawBitmap(lines[a][1], (m - 4) * 80, 120, paint );


                    }
                }


                if( m - n == 1){

                    if(m < 5){

                        canvas.drawBitmap(lines[a][1], (n + 1) * 80, 40, paint );

                    }else if(m > 9){

                        canvas.drawBitmap(lines[a][1], (n - 9) * 80, 200, paint );


                    }else{

                        canvas.drawBitmap(lines[a][1], (n - 4) * 80, 120, paint );


                    }
                }
            }
        }

        //Correct circles

        int correctCircles = 0;

        for(a = 0; a < 15; a++){

            if(squareValues[a] == a && correctCircleNotMoving[a] == true){

            correctCircles++;

            paint.setColor(Color.GREEN);

            if(a < 5){

                canvas.drawBitmap(greenFlare, (a+1) * 80 - 37, 43, paint );

            }else if(a > 9){

                canvas.drawBitmap(greenFlare, (a-9) * 80 - 37, 203, paint );

            }else{

                canvas.drawBitmap(greenFlare, (a-4) * 80 - 37, 123, paint );

            }
            }
        }

        if(correctCircles == 15 && mixed == true){
            finish();
            mixed = false;
        }

        //CIRCLES

        for(a = 0; a < 15; a++){


            if(a < 5){

                canvas.drawBitmap(kugelbugel, (a + 1) * 80 - 32 + (int)(ballAnimationOffset[a][0]), 48 + (int)(ballAnimationOffset[a][1]), paint);
                paint.setColor(Color.BLACK);
                paint.setTextSize(20);
                canvas.drawText("" + (squareValues[a] + 1), (a + 1) * 80 + (int)(ballAnimationOffset[a][0]), 80 + (int)(ballAnimationOffset[a][1]), paint);



            }else if(a > 9){

                canvas.drawBitmap(kugelbugel, (a - 9) * 80 - 32 + (int)(ballAnimationOffset[a][0]), 208 + (int)(ballAnimationOffset[a][1]), paint);
                paint.setColor(Color.BLACK);
                paint.setTextSize(20);
                canvas.drawText("" + (squareValues[a] + 1), (a - 9) * 80 + (int)(ballAnimationOffset[a][0]), 240 + (int)(ballAnimationOffset[a][1]), paint);

            }else{

                canvas.drawBitmap(kugelbugel, (a - 4) * 80 - 32 + (int)(ballAnimationOffset[a][0]), 128 + (int)(ballAnimationOffset[a][1]), paint);
                paint.setColor(Color.BLACK);
                paint.setTextSize(20);
                canvas.drawText("" + (squareValues[a] + 1), (a - 4) * 80 + (int)(ballAnimationOffset[a][0]), 160 + (int)(ballAnimationOffset[a][1]), paint);


            }
        }

        //"ghost circles"

        for( a = 0; a < 8; a++){
            canvas.drawBitmap(kugelbugel, ghostCircle[a][0] + (int)(ballAnimationOffset[15 + a][0]), ghostCircle[a][1] + (int)(ballAnimationOffset[15 + a][1]), paint);
            paint.setColor(Color.BLACK);
            paint.setTextSize(20);
            canvas.drawText("" + (ghostCircleValues[a] + 1), ghostCircle[a][0] + 32 + (int)(ballAnimationOffset[15 + a][0]), ghostCircle[a][1] + (int)(ballAnimationOffset[15 + a][1]) + 32, paint);
        }


        //Draw Victory screen

        if(victoryScreen == true){

            paint.setARGB(200, 0, 0, 0);
            canvas.drawRect(0,0,480,320, paint);
            paint.setARGB(255,0,0,0 );

            //draw polygon

            Path polygon = new Path();
            polygon.moveTo(240, 30);
            polygon.lineTo(350, 85);
            polygon.lineTo(350, 190);
            polygon.lineTo(240, 250);
            polygon.lineTo(135, 190);
            polygon.lineTo(135, 85);
            polygon.close();

            Path polygonOuter = new Path();
            polygonOuter.moveTo(240, 29);
            polygonOuter.lineTo(351, 84);
            polygonOuter.lineTo(351, 191);
            polygonOuter.lineTo(240, 251);
            polygonOuter.lineTo(134, 191);
            polygonOuter.lineTo(134, 84);
            polygonOuter.close();

            Path polygonLowerPart = new Path();
            polygonLowerPart.moveTo(135, 160);
            polygonLowerPart.lineTo(350, 160);
            polygonLowerPart.lineTo(350, 190);
            polygonLowerPart.lineTo(240, 250);
            polygonLowerPart.lineTo(135, 190);
            polygonLowerPart.close();


            paint.setColor(Color.rgb(127, 127, 127));
            canvas.drawPath(polygonOuter, paint);

            paint.setColor(Color.rgb(136, 0, 21));
            canvas.drawPath(polygon, paint);

            paint.setColor(Color.rgb(94, 0, 14));
            canvas.drawPath(polygonLowerPart, paint);

            paint.setColor(Color.rgb(127, 127, 127));
            canvas.drawLine(135, 160, 350, 160, paint);
            canvas.drawRect(148, 146, 332, 184, paint);

            //Circle

            canvas.drawCircle(185, 105, 25, paint);

            paint.setColor(Color.rgb(221,221,221));
            canvas.drawCircle(185, 105, 23, paint);

            paint.setColor(Color.rgb(235,235,235));
            canvas.drawCircle(194, 96, 7, paint);

            //bonus texts

            paint.setColor(Color.WHITE);


            if( doNotGetScoreAgain == false){
            scoreThisRound = Achievements.getBonuses(getContext(), false);
            doNotGetScoreAgain = true;

            }


            canvas.drawText("" + scoreThisRound, 210, 90, paint);

            if(dropScoreCalled == false){
                dropScore();
                dropScoreCalled = true;
            }


            //Bar

            paint.setColor(Color.BLACK);
            canvas.drawRect(151, 147, 331, 178, paint);

            paint.setColor(Color.WHITE);

            int[] levelAndExp = Achievements.getLevelAndExp(score);

            canvas.drawText("" + levelAndExp[0], 175, 105, paint);

            int experiencePercentage = (int)(100 * (double)( levelAndExp[1]) / (double)( levelAndExp[2] ));


            paint.setColor(Color.rgb(255, 127, 39));

            int x = (int)(experiencePercentage * 1.8);
            canvas.drawRect(151, 147, 151 + x, 181, paint);

            paint.setColor(Color.rgb(204, 82, 0));

            Path the3DEffect = new Path();
            the3DEffect.moveTo(151 + x, 147);
            the3DEffect.lineTo(151 + x + 4, 147);
            the3DEffect.lineTo(151 + x + 4, 178);
            the3DEffect.lineTo(151 + x, 181);
            the3DEffect.close();

            canvas.drawPath(the3DEffect, paint);

            //so the bar doesn't go over

            paint.setColor(Color.rgb(127, 127, 127));
            canvas.drawLine(332, 146, 332, 184, paint);
            canvas.drawLine(332, 160, 348, 160, paint);

            paint.setColor(Color.rgb(136, 0, 21));
            canvas.drawRect(333, 146, 348, 160, paint );

            paint.setColor(Color.rgb(94, 0, 14));
            canvas.drawRect(333, 161, 348, 181, paint );

        }

            //Draw "choose next puzzle"

            if(chooseNextPuzzle == true){

            canvas.drawBitmap(nextlvbg, 0, 0, paint);

            paint.setColor(Color.WHITE);
            canvas.drawText("Choose next level", 10, 20, paint);

            CreateLines();


                //Draw lines for small screens

                for(a = 0; a < allLines.size(); a++){

                    for(b = 0; b < allLines.get(a).getSize(); b++){

                        //setColor

                        switch(a){
                            case 0: paint.setColor(Color.rgb(162, 44, 21)); break;
                            case 1: paint.setColor(Color.rgb(150,150,150)); break;
                            case 2: paint.setColor(Color.rgb(162, 44, 21)); break;
                            case 3: paint.setColor(Color.rgb(150,150,150)); break;
                            case 4: paint.setColor(Color.rgb(162, 44, 21)); break;
                            case 5: paint.setColor(Color.rgb(150,150,150)); break;


                            default:;break;
                        }

                        int m = allLines.get(a).getSquare(b);
                        int n;
                        int mx, my, nx, ny;

                        if(b == allLines.get(a).getSize() - 1){

                            n = allLines.get(a).getSquare(0);

                        }else{

                            n = allLines.get(a).getSquare(b + 1);

                        }

                        if(m < 5){

                            mx = (m + 1) * 24 + 15 + (155 * (int)(a / 2));
                            my = 140;

                        }else if(m > 9){

                            mx = (m - 9) * 24 + 15 + (155 * (int)(a / 2));
                            my = 193;

                        }else{

                            mx = (m - 4) * 24 + 15 + (155 * (int)(a / 2));;
                            my = 167;

                        }

                        if(n < 5){

                            nx = (n + 1) * 24 + 15 + (155 * (int)(a / 2));;
                            ny = 140;

                        }else if(n > 9){

                            nx = (n - 9) * 24 + 15 + (155 * (int)(a / 2));;
                            ny = 193;

                        }else{

                            nx = (n - 4) * 24 + 15 + (155 * (int)(a / 2));;
                            ny = 167;

                        }

                        paint.setStrokeWidth(3);

                        //Check if line should be going over screen

                        //if line goes from up or down side of the screen

                        if(Math.abs(n - m) == 10){

                            //if M is down, N is up
                            if(Math.max(n, m) == m){

                                canvas.drawLine(mx + ((a % 2) * 3), my + ((a % 2) * 3), mx + ((a % 2) * 3), 218, paint);
                                canvas.drawLine(nx + ((a % 2) * 3), ny + ((a % 2) * 3), nx + ((a % 2) * 3), 115, paint);

                            }else{

                                canvas.drawLine(mx + ((a % 2) * 3), my + ((a % 2) * 3), mx + ((a % 2) * 3), 115, paint);
                                canvas.drawLine(nx + ((a % 2) * 3), ny + ((a % 2) * 3), nx + ((a % 2) * 3), 218, paint);

                            }

                        }

                        //if line goes from right or left side of the screen

                        else if(Math.abs(n - m) == 4){

                            //if M is right, N is left
                            if(Math.max(n, m) == m){

                                canvas.drawLine(mx + ((a % 2) * 3), my + ((a % 2) * 3), 155 + ((int) a / 2) * 155 , my + ((a % 2) * 3), paint);
                                canvas.drawLine(nx + ((a % 2) * 3), ny + ((a % 2) * 3), 15 + ((int) a / 2) * 155, ny + ((a % 2) * 3), paint);

                            }else{

                                canvas.drawLine(mx + ((a % 2) * 3), my + ((a % 2) * 3), 15 + ((int) a / 2) * 155, my + ((a % 2) * 3), paint);
                                canvas.drawLine(nx + ((a % 2) * 3), ny + ((a % 2) * 3), 155 + ((int) a / 2) * 155, ny + ((a % 2) * 3), paint);

                            }

                        }else{

                            canvas.drawLine(mx + ((a % 2) * 3), my + ((a % 2) * 3), nx + ((a % 2) * 3), ny + ((a % 2) * 3), paint);

                        }
                    }
                }

                chooseNextPuzzle2 = true;

                chooseNextPuzzle = false;
        }

    }

    public boolean onTouch(View v, MotionEvent e){

        int x = (int)e.getX();
        int y = (int)e.getY();

        if(e.getAction() == MotionEvent.ACTION_DOWN){


            if(chooseNextPuzzle2 == true){

                if( x < 162){

                    allLines.remove(5);
                    allLines.remove(4);
                    allLines.remove(3);
                    allLines.remove(2);

                    Achievements.difficultyBonusIntersections = puzzle3[0].getIntersections();
                    Achievements.difficultyBonusOverEdge = puzzle3[0].getOvers();
                    Achievements.difficultyBonusLength = puzzle3[0].getLength();;


                }else if(x > 317){

                    allLines.remove(3);
                    allLines.remove(2);
                    allLines.remove(1);
                    allLines.remove(0);

                    Achievements.difficultyBonusIntersections = puzzle3[1].getIntersections();
                    Achievements.difficultyBonusOverEdge = puzzle3[1].getOvers();
                    Achievements.difficultyBonusLength = puzzle3[1].getLength();;


                }else{

                    allLines.remove(5);
                    allLines.remove(4);
                    allLines.remove(1);
                    allLines.remove(0);

                    Achievements.difficultyBonusIntersections = puzzle3[2].getIntersections();
                    Achievements.difficultyBonusOverEdge = puzzle3[2].getOvers();
                    Achievements.difficultyBonusLength = puzzle3[2].getLength();
                }


                chooseNextPuzzle2 = false;
                newPuzzle();

                invalidate();

            }

            if(skipPuzzle == true){
                chooseNextPuzzle = true;
                com.Nuotta.MainActivity.skip(getContext());
                invalidate();
                skipPuzzle = false;
            }


            if(finished == true && notDoneYet == true){

              //notDoneYet estää SystemClockin päivittymisen

                notDoneYet = false;
                victoryScreen = true;
                Achievements.timeUsed = SystemClock.elapsedRealtime() - Achievements.systemStartTime;

                invalidate();

            }else{

            if(y < 120){

                draggedSquare = (x - 40) / 80;

            }else if(y > 200){

                draggedSquare = 10 + (x - 40) / 80;

            }else{

                draggedSquare = 5 + (x - 40) / 80;

            }

            }

        }
        if(e.getAction() == MotionEvent.ACTION_UP){

            int endingSquare = 0;


            if((y < 120 && y > 40) || y >= 280){

                if(x <= 40){
                endingSquare = 4;
                }else if(x >= 440){
                endingSquare = 0;
                }else{
                endingSquare = (x - 40) / 80;
                }

            }else if((y > 200 && y < 280 ) || y <= 40){

                if(x <= 40){
                    endingSquare = 14;
                }else if(x >= 440){
                    endingSquare = 10;
                }else{
                endingSquare = 10 + (x - 40) / 80;
                }

            }else{


                if(x <= 40){
                    endingSquare = 9;
                }else if(x >= 440){
                    endingSquare = 5;
                }else{
                endingSquare = 5 + (x - 40) / 80;
                }

            }

            if(amountOfQueuedAnimations <= 5){
            //testing this
                if(findRightLineToMove(draggedSquare, endingSquare) == false){

                    boolean[] roadChecked = new boolean[4];
                    int a;

                    for (a = 0; a<roadChecked.length; a++){

                        roadChecked[a] = false;

                    }
                    searchUntil(endingSquare, roadChecked);



            }
            }
        }


        return true;
    }

    public void searchUntil(int endingSquare, boolean[] checked){

        //pulled one too much ahead right and left
        if( endingSquare - draggedSquare == 2 && endingSquare % 5 > 1){

            if( findRightLineToMove(draggedSquare, draggedSquare + 1 ) == false){

                if(checked[0] == false){
                    checked[0] = true;
                    searchUntil(endingSquare, checked);
                }
            }
        }

        if( draggedSquare - endingSquare == 2 && endingSquare % 5 < 3){

           if( findRightLineToMove(draggedSquare, draggedSquare - 1) == false){

               if(checked[1] == false){
                   checked[1] = true;
                   searchUntil(endingSquare, checked);
               }


           }
        }



        //pulled one too much ahead up or down

        if( endingSquare - draggedSquare == 10){

           if( findRightLineToMove(draggedSquare, draggedSquare + 5) == false){

               if(checked[2] == false){
                   checked[2] = true;
                   searchUntil(endingSquare, checked);
               }
           }

        }
        if( draggedSquare - endingSquare == 10){

           if( findRightLineToMove(draggedSquare, draggedSquare - 5) == false){

               if(checked[3] == false){
                   checked[3] = true;
                   searchUntil(endingSquare, checked);
               }

           }

        }
    }

    public static void CreateLines(){

        allLines.clear();
        ArrayList<ChooseDifficulties> puzzle = new ArrayList<ChooseDifficulties>();

        Random rnd = new Random();

        int a, b, c;
        int startingSquare;
        LineCreator[] line = new LineCreator[40];

        for(a = 0; a < 40; a+=2){

            startingSquare = rnd.nextInt(15);
            line[a] = new LineCreator(startingSquare);
            line[a + 1] = new LineCreator(startingSquare);
            puzzle.add(new ChooseDifficulties(line[a], line[a + 1]));

        }

        int collision = 0;

        //check that lines aren't the same and check collision points

        for( a = 0; a < 20; a++){
           for ( b = 0; b < puzzle.get(a).getLine1().getSize(); b++ ){
               for(c = 0; c < puzzle.get(a).getLine2().getSize(); c++){
                   if(puzzle.get(a).getLine1().getSquare(b) == puzzle.get(a).getLine2().getSquare(c)){
                       collision++;
                   }


               }
           }




           while(collision == line[a].getSize() && collision == line[a + 1].getSize()){

                   puzzle.remove(a);
                   collision = 0;
                   startingSquare = rnd.nextInt(15);
                   line[a] = new LineCreator(startingSquare);
                   line[a + 1] = new LineCreator(startingSquare);
                   puzzle.add(new ChooseDifficulties(line[a], line[a + 1]));

                   for ( b = 0; b < puzzle.get(a).getLine1().getSize(); b++ ){
                       for(c = 0; c < puzzle.get(a).getLine2().getSize(); c++){
                           if(puzzle.get(a).getLine1().getSquare(b) == puzzle.get(a).getLine2().getSquare(c)){
                               collision++;
                           }


                       }
                   }
               }

               puzzle.get(a).setIntersections(collision);
               collision = 0;

           }




        List<ChooseDifficulties> difficultyOrder = puzzle;
        Collections.sort(difficultyOrder, new SortDifficulties());

        //at high levels get one from first 5, one from 2nd 5 and one from last 10
        //medium unlocks at lv5, hard unlocks at lv10
        //before that give puzzles only from unlocked classes.

        Random rng = new Random();
        int random1 = rng.nextInt(5);
        int random2 = rng.nextInt(5);
        int random3 = rng.nextInt(10);
        int random4 = rng.nextInt(5);

        if(level < 5){

            allLines.add(difficultyOrder.get(0).getLine1());
            allLines.add(difficultyOrder.get(0).getLine2());
            allLines.add(difficultyOrder.get(1 + random1).getLine1());
            allLines.add(difficultyOrder.get(1 + random1).getLine2());
            allLines.add(difficultyOrder.get(2 + random1 + random2).getLine1());
            allLines.add(difficultyOrder.get(2 + random1 + random2).getLine2());

            puzzle3[0] = difficultyOrder.get(0);
            puzzle3[1] = difficultyOrder.get(1 + random1);
            puzzle3[2] = difficultyOrder.get(2 + random1 + random2);

        }else if(level < 10){

            allLines.add(difficultyOrder.get(random1).getLine1());
            allLines.add(difficultyOrder.get(random1).getLine2());
            allLines.add(difficultyOrder.get(1 + random1 + random2).getLine1());
            allLines.add(difficultyOrder.get(1 + random1 + random2).getLine2());
            allLines.add(difficultyOrder.get(2 + random1 + random2 + random4).getLine1());
            allLines.add(difficultyOrder.get(2 + random1 + random2 + random4).getLine2());

            puzzle3[0] = difficultyOrder.get(random1);
            puzzle3[1] = difficultyOrder.get(1 + random1 + random2);
            puzzle3[2] = difficultyOrder.get(2 + random1 + random2 + random4);

        }else{

            allLines.add(difficultyOrder.get(random1).getLine1());
            allLines.add(difficultyOrder.get(random1).getLine2());
            allLines.add(difficultyOrder.get(5 + random2).getLine1());
            allLines.add(difficultyOrder.get(5 + random2).getLine2());
            allLines.add(difficultyOrder.get(10 + random3).getLine1());
            allLines.add(difficultyOrder.get(10 + random3).getLine2());

            puzzle3[0] = difficultyOrder.get(random1);
            puzzle3[1] = difficultyOrder.get(5 + random2);
            puzzle3[2] = difficultyOrder.get(10 + random3);
        }

    }

    public boolean findRightLineToMove(int start, int end){

        //check if even adjacent

        int f = Math.abs(start - end);
          if(f == 1 || f==5 || f==10 || f==4){

            int a,b;

            for(a = 0; a < allLines.size(); a++){
                for(b = 0; b < allLines.get(a).getSize() - 1; b++){

                    int firstSquare = allLines.get(a).getSquare(b);
                    int secondSquare = allLines.get(a).getSquare(b+1);

                    if( start == firstSquare && end == secondSquare){
                        animationTimer(allLines.get(a), true);
                        return true;
                    }

                    if( start == secondSquare && end == firstSquare){
                        animationTimer(allLines.get(a), false);
                        return true;
                    }

                    if( start == allLines.get(a).getSquare(0) && end == allLines.get(a).getSquare(allLines.get(a).getSize()-1)){
                        animationTimer(allLines.get(a), false);
                        return true;
                    }

                    if( start == allLines.get(a).getSquare(allLines.get(a).getSize()-1) && end == allLines.get(a).getSquare(0)){
                        animationTimer(allLines.get(a), true);
                        return true;
                    }

                }
            }

            return false;

        }


        return false;

    }

    public void animationTimer(LineCreator line, boolean forward){


        Achievements.movesUsed++;

        lineAnimate.add(line);
        forwardAnimate.add(forward);
        amountOfQueuedAnimations++;

        final int animationHz = 20;

        if(queueOn == false){
        final Timer animation = new Timer();
        animation.schedule(new TimerTask(){

            private int counter = 0;
            public void run(){

                if(finished == false){

                    queueOn = true;

                    lineAnimation(animationHz);

                    int a;
                    for(a = 0; a < lineAnimate.get(0).getSize(); a++ ){
                        correctCircleNotMoving[lineAnimate.get(0).getSquare(a)] = false;
                    }

                    counter++;

                    if(counter == 80){

                        for(a = 0; a < 15; a++ ){
                            correctCircleNotMoving[a] = true;
                        }

                        counter = 0;

                        queueOn = false;

                        moveLine(lineAnimate.get(0), forwardAnimate.get(0));

                        lineAnimate.remove(0);
                        forwardAnimate.remove(0);
                        amountOfQueuedAnimations--;

                        if(lineAnimate.isEmpty() == true){
                        animation.cancel();

                        }

                        for(a = 0; a < 31; a++){
                            ballAnimationOffset[a][0] = 0;
                            ballAnimationOffset[a][1] = 0;
                        }

                    }
                }

            }

        },0, 20);
        }
    }

    public void moveLine(LineCreator line, boolean forward){

        int a;
        int savedValue;


        if( forward == true){

            savedValue = squareValues[line.getSquare(line.getSize() - 1)];


            for(a = line.getSize() - 1; a > 0; a--){

                squareValues[line.getSquare(a)] = squareValues[line.getSquare(a - 1)];


            }

            squareValues[line.getSquare(0)] = savedValue;


        }else{

            savedValue = squareValues[line.getSquare(0)];


            for(a = 0; a < line.getSize() - 1; a++){

                squareValues[line.getSquare(a)] = squareValues[line.getSquare(a + 1)];

            }

            squareValues[line.getSquare(line.getSize() - 1)] = savedValue;



        }
    }


    public void lineAnimation(int animationHz){

        int a;

        LineCreator line = lineAnimate.get(0);

        //animation speed
        int s = 1;

        if ( forwardAnimate.get(0) == true){

        for( a = 0; a < line.getSize(); a++){


            lineAnimation2(a, line, s, forwardAnimate.get(0));

        }

            gc = 0;

        }else{


        for ( a = line.getSize() - 1; a >= 0; a--){

            lineAnimation2(a, line, s, forwardAnimate.get(0));

        }

            gc = 0;

        }
        postInvalidate();


    }

    public void lineAnimation2(int a, LineCreator line, int s, boolean forward){

        int a1 = line.getSquare(a);
        int a2 = 0;
        int a3 = 0;

        if(a == line.getSize() - 1 && forward == true){

            a2 = line.getSquare(0);

        }else if(a == 0 && forward == false){

            a2 = line.getSquare(line.getSize() - 1);

        }else if(forward == true){

            a2 = line.getSquare(a + 1);

        }else{

            a2 = line.getSquare(a - 1);

        }

        //Left
        if( a1 - a2 == 1){
            ballAnimationOffset[a1][0] -= s;
        }
        //Right
        if( a2 - a1 == 1){
            ballAnimationOffset[a1][0] += s;
        }
        //up
        if( a1 - a2 == 5){
            ballAnimationOffset[a1][1] -= s;
        }
        //down
        if( a2 - a1 == 5){
            ballAnimationOffset[a1][1] += s;
        }


        //over right side
        if( a1 - a2 == 4){

            ghostCircle[gc][0] = -64;
            ghostCircle[gc][1] = (a1 / 5) * 80 + 48;
            ghostCircleValues[gc] = squareValues[a1];

            ballAnimationOffset[a1][0] += s * 1.4;
            ballAnimationOffset[15 + gc][0] += s * 1.4;

            gc++;

        }
        //over left side
        if( a2 - a1 == 4){

            ghostCircle[gc][0] = 480;
            ghostCircle[gc][1] = ( a1 / 5 ) * 80 + 48;
            ghostCircleValues[gc] = squareValues[a1];

            ballAnimationOffset[a1][0] -= s * 1.4;
            ballAnimationOffset[15 + gc][0] -= s * 1.4;

            gc++;

        }
        //over top side
        if( a2 - a1 == 10){

            ghostCircle[gc][0] = ((a1 % 5) + 1) * 80 - 32;
            ghostCircle[gc][1] = 320;
            ghostCircleValues[gc] = squareValues[a1];

            ballAnimationOffset[a1][1] -= s * 1.4;
            ballAnimationOffset[15 + gc][1] -= s * 1.4;

            gc++;

        }
        //over bottom side
        if( a1 - a2 == 10){

            ghostCircle[gc][0] = ((a1 % 5) + 1) * 80 - 32;
            ghostCircle[gc][1] = -64;
            ghostCircleValues[gc] = squareValues[a1];

            ballAnimationOffset[a1][1] += s * 1.4;
            ballAnimationOffset[15 + gc][1] += s * 1.4;

            gc++;
        }


    }

    public void mixMap(){

        int a;
        Random r = new Random();
        for(a = 0; a < mixAmount; a++){

            moveLine(allLines.get(r.nextInt(allLines.size())), r.nextBoolean());
        }

        int checkIfNotMixed = 0;
        for(a  = 0; a < 15; a++){
            if(squareValues[a] == a){
                checkIfNotMixed++;
            }
        }
        if(checkIfNotMixed == 15){
            moveLine(allLines.get(r.nextInt(allLines.size())), r.nextBoolean());
        }

        mixed = true;

    }

    public void dropScore(){

        counter = scoreThisRound;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                final Timer dropper = new Timer();
                dropper.schedule(new TimerTask() {
                    public void run() {

                        counter--;

                        if (scoreThisRound > 0) {

                            score += 1;
                            scoreThisRound -= 1;

                            postInvalidate();
                        }

                        if(counter < -10){

                            com.Nuotta.MainActivity.setScore(getContext(), score);

                            notDoneYet = true;

                            dropper.cancel();

                            chooseNextPuzzle = true;

                            postInvalidate();


                        }




                    }

                }, 0, 200);

            }
        }, 1000);





    }

    public void finish(){

        Achievements.timeUsed = SystemClock.elapsedRealtime() - Achievements.systemStartTime;
        finished = true;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                victoryScreen = true;

                postInvalidate();
            }
        }, 1000);

    }


    public void newPuzzle(){

        finished = false;
        victoryScreen = false;
        dropScoreCalled = false;
        goToNextPuzzle = false;
        chooseNextPuzzle = false;
        doNotGetScoreAgain = false;

        Achievements.movesUsed = 0;
        Achievements.timeUsed = 0;
        Achievements.systemStartTime = 0;
        Achievements.systemStartTime = SystemClock.elapsedRealtime();

        int a;
        for(a = 0; a < 15; a++){
            squareValues[a] = a;
        }

        mixMap();

        for(a = 0; a < 8; a++){
            ghostCircle[a][0] = -100;
            ghostCircle[a][1] = -100;
        }

        for(a = 0; a < 15; a++){
            resetState[a] = squareValues[a];
        }
    }
}
