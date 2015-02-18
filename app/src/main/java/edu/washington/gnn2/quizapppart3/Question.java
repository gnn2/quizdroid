package edu.washington.gnn2.quizapppart3;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Gabby on 2/16/2015.
 */
public class Question implements Serializable{
    private int correctAnswer;
    private String question;
    private ArrayList<String> possibleAnswers;

    public Question(String question){
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    /**
    public void setQuestion(String question) {
        this.question = question;
    }
    */

    public ArrayList<String> getPossibleAnswers() {
        return possibleAnswers;
    }

    public void setPossibleAnswers(ArrayList<String> possibleAnswers) {
        this.possibleAnswers = possibleAnswers;
    }


    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }


}
