package com.example.trivia.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.trivia.MainActivity;

public class Prefs {
    public static final String HIGH_SCORE = "high_score";
    public static final String STATE = "current_State";
    private SharedPreferences preferences;

    public Prefs(MainActivity context) {
        this.preferences = context.getPreferences(Context.MODE_PRIVATE);
    }

   public void saveHighScore(int score){
        int currentScore=score;
        int lastScore =preferences.getInt(HIGH_SCORE,0);
        if (currentScore>lastScore){
            preferences.edit().putInt(HIGH_SCORE,currentScore).apply();
        }

   }

    public int getHighScore(){
        return preferences.getInt(HIGH_SCORE,0);
    }

    public void setState(int index){
        preferences.edit().putInt(STATE,index).apply();

    }
    public int getState(){
        return preferences.getInt(STATE,0);
    }

}
