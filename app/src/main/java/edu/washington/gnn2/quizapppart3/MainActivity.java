package edu.washington.gnn2.quizapppart3;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class MainActivity extends ActionBarActivity  {

    private static final String TAG = "MainActivity";
    public static Repository repo;
   // private static ArrayList<String> topics;
    private static HashMap<String, ArrayList<String>> questions; //
    private static HashMap<String, ArrayList<String>> answers;
    private static HashMap<String, HashMap<Integer, ArrayList<String>>> possibleAnswers;
    private static ArrayList<String> quizes;
    private static ArrayList<String> topicQ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Activity onCreate");
        setContentView(R.layout.activity_main);

       QuizApp app = new QuizApp();
        quizes = addTopics();
        topicQ = addQuestions();
        questions = initQuestions(quizes);
        answers = initAnswers(quizes);
        possibleAnswers = setPossibleAnswers();

      final Repository repo = new Repository();

       HashMap<String, Topic> dir = repo.createRepo(quizes, questions, answers, possibleAnswers);

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, quizes);
        ListView listView = (ListView) findViewById(R.id.listItem);
        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                Toast.makeText(getApplicationContext(),
                        ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
                String name = ((TextView) view).getText().toString();
                Intent i = new Intent(MainActivity.this, QuizActivity.class);
               // QuizApp quiz = QuizApp.getInstance();
                //Intent nextActivity = new Intent(MainActivity.this, QuizActivity.class); // cannot use just this cuz this refers to the listener, not the outer this

                // add data to be passed to next activity
                i.putExtra("Overview_name", name);
                Topic t = repo.getTopic(name);
                i.putExtra("topic", t);

               if (i.resolveActivity(getPackageManager()) != null) {
                   startActivity(i); // opens a new activity
                }


                // finish(); // kill this instance self (this activity)
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * add all quiz topics and return arraylist of topics
     * @return
     */
    private ArrayList<String> addTopics(){
        quizes = new ArrayList<String>();
        quizes.add("Math");
        quizes.add("Computer Science");
        quizes.add("Music");
        quizes.add("Physics");
        quizes.add("Marvel Super Heroes");
        return quizes;
    }

    /**
     * define questions for the topics
     * @return
     */
    private ArrayList<String> addQuestions(){
        ArrayList<String> question = new ArrayList<String>();
        question.add("Question 1?");
        question.add("Question 2?");
        question.add("Question 3?");
        question.add("Question 4?");
        question.add("Question 5?");
        return question;
    }

    /**
     * link topics to their questions
     * @param topics
     * @return
     */
    private HashMap<String,ArrayList<String>> initQuestions(ArrayList<String> topics){
        questions = new HashMap<String, ArrayList<String>>();
        for(String topic: topics){
            if(!questions.containsKey(topic)){
                questions.put(topic, topicQ);
            }
        }
        return questions;
    }

    /**
     * link topics to the answers for their questions
     * @param topics
     * @return
     */
    private HashMap<String,ArrayList<String>> initAnswers(ArrayList<String> topics){
        answers= new HashMap<String,ArrayList<String>>();

        for(String topic: topics){
            if(!answers.containsKey(topic)){
                ArrayList<String> a = getAnswer(topic);
                answers.put(topic, a);
            }
        }
        return answers;
    }

    /**
     * link topics and their questions to answers
     * @param topic
     * @return
     */
    private ArrayList<String> getAnswer(String topic){
        ArrayList<String> answers = new ArrayList<String>();
        answers.add("Answer 2");
        answers.add("Answer 3");
        answers.add("Answer 3");
        answers.add("Answer 4");
        answers.add("Answer 1");
        return answers;
    }/**
     *
     * @return
     */
    private static HashMap<String, HashMap<Integer, ArrayList<String>>> setPossibleAnswers(){
        HashMap<String, HashMap<Integer, ArrayList<String>>> possible = new  HashMap<String, HashMap<Integer, ArrayList<String>>>();
        for(String topic:  quizes){
            if(!possible.containsKey(topic)){
                HashMap<Integer, ArrayList<String>> map = new  HashMap<Integer, ArrayList<String>>();
                for(int i = 0 ; i < questions.get(topic).size(); i++){
                    ArrayList<String> possibleA = new ArrayList<String>();
                    possibleA.add("Answer 1");
                    possibleA.add("Answer 2");
                    possibleA.add("Answer 3");
                    possibleA.add("Answer 4");
                    map.put(i,possibleA);
                }
                possible.put(topic, map);
            }
        }
        return possible;
    }


    public void onClick(View v){

    }

}
