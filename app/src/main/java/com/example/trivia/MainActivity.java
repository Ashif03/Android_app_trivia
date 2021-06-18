package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.trivia.data.Repository;
import com.example.trivia.databinding.ActivityMainBinding;
import com.example.trivia.model.Question;
import com.example.trivia.model.Score;
import com.example.trivia.util.Prefs;
import com.google.android.material.snackbar.Snackbar;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private int currentQuestionIndex=0;
    List<Question> questionList;
    private int scoreCounter=0;
    private Score score;
    private Prefs prefs;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        score=new Score();
        prefs=new Prefs(MainActivity.this);

        currentQuestionIndex=prefs.getState();

        binding.highScoreView.setText(MessageFormat.format("Highest Score:{0}", String.valueOf(prefs.getHighScore())));



       questionList=  new Repository().getQuestion(QuestionArrayList -> {
           binding.QuestionTextview.setText((CharSequence) QuestionArrayList.get(currentQuestionIndex).getAnswer());

                   Question_counter(QuestionArrayList);

               }



       );

                 binding.nextButton.setOnClickListener(v -> {
                     getNextQuestion();


                 });
                 binding.trueButton.setOnClickListener(v -> {
                     chackanswer(true);
                     updateQuestion();

                 });
                 binding.falseButton.setOnClickListener(v -> {
                     chackanswer(false);
                     updateQuestion();

                 });
    }

    private void getNextQuestion() {
        currentQuestionIndex=(currentQuestionIndex+1)%questionList.size();
        updateQuestion();
    }

    private void chackanswer(boolean userChosecorrectAnswer) {
        boolean answer=questionList.get(currentQuestionIndex).isAnswerTrue();
        int snackMsgId=0;
        if (userChosecorrectAnswer==answer) {
            snackMsgId= R.string.correct_answer;
            fadeAnimation();
            addpoint();

        }
        else {
            snackMsgId=R.string.incorrect;
            shakeAnimation();
            deductPoint();
        }
        Snackbar.make(binding.cardView,snackMsgId,Snackbar.LENGTH_SHORT)
                .show();
    }

    private void Question_counter(ArrayList<Question> QuestionArrayList) {
        binding.textViewOutOff.setText(String.format(getString(R.string.text_formated), currentQuestionIndex, QuestionArrayList.size()));
    }

    private void updateQuestion() {
        String question=questionList.get(currentQuestionIndex).getAnswer();
        binding.QuestionTextview.setText(question);
        Question_counter((ArrayList<Question>) questionList);
    }
    private void shakeAnimation(){
        Animation shake= AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake_animation);
        binding.cardView.setAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.QuestionTextview.setTextColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.QuestionTextview.setTextColor(Color.WHITE);
                getNextQuestion();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void fadeAnimation(){
        AlphaAnimation alphaAnimation=new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);


        binding.cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.QuestionTextview.setTextColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.QuestionTextview.setTextColor(Color.WHITE);
                getNextQuestion();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void addpoint(){

        scoreCounter+=2;
        score.setScore(scoreCounter);
        binding.scoreView.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));

    }

    @Override
    protected void onPause() {
        prefs.saveHighScore(score.getScore());
        prefs.setState(currentQuestionIndex);
        super.onPause();

    }

    private void deductPoint(){
        if (scoreCounter>0){
            scoreCounter-=2;
            score.setScore(scoreCounter);
            binding.scoreView.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));

        }
        else {
            scoreCounter=0;
            score.setScore(scoreCounter);

        }



    }

}