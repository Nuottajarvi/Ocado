package com.Nuotta;

import android.app.Activity;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.SystemClock;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends Activity {

    public static String score = "score";
    public static String skips = "skips";
    static SharedPreferences sharedPreferences;

    private int soundID;
    static boolean loaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        DrawGame drawGame = new DrawGame(this);
        setContentView(drawGame);

        sharedPreferences = getSharedPreferences(score, MODE_PRIVATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_skip) {
            DrawGame.skipPuzzle = true;
        }
        if (id == R.id.action_reset){
            int a;

            for(a = 0; a < 15; a++){
                DrawGame.squareValues[a] = DrawGame.resetState[a];
            }

            if (id == R.id.action_muteSound) {
                //mute music/sounds
            }

            Achievements.movesUsed = 0;
            Achievements.systemStartTime = SystemClock.elapsedRealtime();
        }

        if (id == R.id.action_exit){
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }

    public static void setScore(Context context, int scoreToAdd){

        int newScore = getScore(context) + scoreToAdd;

        SharedPreferences.Editor editor = context.getSharedPreferences(score, 0).edit();
        editor.putInt(score, newScore);
        editor.commit();

    }

    public static void resetScore(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(score, 0).edit();
        editor.putInt(score, 0);
        editor.commit();
    }

    public static int getScore(Context context){

        sharedPreferences = context.getSharedPreferences(score, 0);
        int returnedInt = context.getSharedPreferences(score, 0).getInt(score, 0);
        return returnedInt;
    }

    public static void skip(Context context){

        int skipsLeft = getSkips(context) - 1;

        SharedPreferences.Editor editor = context.getSharedPreferences(skips, 0).edit();
        editor.putInt(skips, skipsLeft);
        editor.commit();
        Toast.makeText(context, (skipsLeft + " skips left, you get more by playing."), Toast.LENGTH_LONG).show();
    }

    public static int getSkips(Context context){
        sharedPreferences = context.getSharedPreferences(skips, 0);
        return context.getSharedPreferences(skips, 0).getInt(skips, 0);
    }
}