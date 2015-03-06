package edu.washington.gnn2.quizapppart3;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Gabby on 2/16/2015.
 */
public class Topic implements Serializable {
    private String title;
    private String descriptionShort;
    private String descriptionLong;
    private ArrayList<Question> quizQuestions;

    public Topic(String title, String descriptionShort, String descriptionLong ){
        this.title = title;
        this.descriptionShort = descriptionShort;
        this.descriptionLong = descriptionLong;
        quizQuestions = new ArrayList<Question>();
    }

    public void setQuizQuestion(Question q) {
        quizQuestions.add(q);
    }


    public ArrayList<Question> getQuizQuestions() {
        return quizQuestions;
    }

    public void setQuizQuestions(ArrayList<Question> quizQuestions) {
        this.quizQuestions = quizQuestions;
    }


    public String getDescriptionShort() {
        return descriptionShort;
    }

    public void setDescriptionShort(String descriptionShort) {
        this.descriptionShort = descriptionShort;
    }

    public String getDescriptionLong() {
        return descriptionLong;
    }

    public void setDescriptionLong(String descriptionLong) {
        this.descriptionLong = descriptionLong;
    }

    public String getTitle() {
        return title;
    }

    /**
    public void setTitle(String title) {
        this.title = title;
    }
    */


}
