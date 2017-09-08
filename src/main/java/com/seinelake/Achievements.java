package com.seinelake;


import android.content.Context;
import android.util.Log;

public class Achievements {

    //score statics

    static int movesUsed = 0;
    static long timeUsed = 0;
    static long systemStartTime = 0;
    static int difficultyBonusIntersections = 0;
    static int difficultyBonusLength = 0;
    static int difficultyBonusOverEdge = 0;


    public Achievements(){
    }

    public static int[] getLevelAndExp(int score){

        int[] values = getLevel(score, 0);

        return values;

    }

    public static int[] getLevel(int score, int recursionCount){

        recursionCount++;

        score = score - (int) (Math.pow(recursionCount, 0.666666) * 100);

        if( score > 0){

            return getLevel(score, recursionCount);

        }else{

            int[] returnValues = new int[3];
            returnValues[0] = recursionCount - 1;
            returnValues[1] = score + (int)(Math.pow(recursionCount, 0.666666) * 100);
            returnValues[2] = (int) (Math.pow(recursionCount, 0.666666) * 100);


            return returnValues;

        }


    }

    public static int getBonuses(Context context, boolean update){

        Log.w("aa", "getBonuses");

        /*
        So how the score works

        If the amount of intersections is 1, give points depending on moves used and time used. More moves and time used less points. Difficulty bonus 1

        If the amount of intersections is 2 and they are right next to each other difficulty bonus 2

        If the amount of intersections is 2 and they are not next to each other difficulty bonus 3

        If the amount of intersections is 3 and they are next to each other difficulty bonus 4

        If the amount of intersections is 3 and they are not next to each other or the other chain is 4 long, difficulty bonus 5

        If player completes the puzzle faster, less points will be gained if difficulty bonus is 1 or more. The score in puzzles over difficulty 1 is
        Math.min(Moves * 3, Time used)

         */

        if(movesUsed == 0) movesUsed = 1;
        if(timeUsed == 0) timeUsed = 1;

        //calculate difficulty score

        float score = 0f;

        //higher score the more moves and time went to the puzzle

        score = (3 * (float)(Math.pow(2, difficultyBonusIntersections))) + (difficultyBonusLength + difficultyBonusOverEdge * 2) / 3;
        Log.w("score before time and move bonuses : ", "" + score + "intersections" + (difficultyBonusIntersections * 100) + "length" + difficultyBonusLength + "over edge" + (difficultyBonusOverEdge) * 5);

        score = score * (0.1f * Math.min(movesUsed * 3, (timeUsed / 1000)));
        Log.w("score after time and move bonuses : ", "" + score);

        if(update == true){
            updateScore(context, (int)score);
        }

        return (int)score;



    }

    public static void updateScore(Context context,int score){
        com.Nuotta.MainActivity.setScore(context, score);
    }
}
