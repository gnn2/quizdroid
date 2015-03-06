package edu.washington.gnn2.quizapppart3;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class MainActivity extends ActionBarActivity  {

    private static final String TAG = "MainActivity";
    public static Repository repo;
    private static final String name = "quizdata.json";
    private static String m = "http://tednewardsandbox.site44.com/questions.json";
    private File data ;
   // private static ArrayList<String> topics;
    private static HashMap<String, ArrayList<String>> questions; //
    private static HashMap<String, ArrayList<String>> answers;
    private static HashMap<String, HashMap<Integer, ArrayList<String>>> possibleAnswers;
    private static ArrayList<String> quizes;
    private static ArrayList<String> topicQ;
    HashMap<String, Topic> dir;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Activity onCreate");
        setContentView(R.layout.activity_main);
        data = new File(getApplicationContext().getFilesDir().getAbsolutePath(), "quizdata.json");
        QuizApp app = new QuizApp();
        quizes  = new ArrayList<String>();
        dir = new HashMap<String, Topic>();
        final Repository repo = new Repository();

        if(data.exists()){
            try{
                FileInputStream fis = new FileInputStream(data);
                String f = readJSONFile(fis);
                JSONArray jsonArray = new JSONArray(f);
              //  JSONArray names = jsonObject.names();
                int topics = jsonArray.length();
                for(int i = 0; i < topics; i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    //JSONArray names = jsonObject.names();
                    String title = jsonObject.getString("title");
                    String desc =jsonObject.getString("desc");
                    JSONArray questions = jsonObject.getJSONArray("questions");
                    int qs = questions.length();
                    for(int j = 0; j < qs; j ++){
                         JSONObject question = questions.getJSONObject(j);
                        JSONArray names = question.names();
                        String q = question.getString("text");
                        String answer = question.getString("answer");
                        JSONArray answers = question.getJSONArray("answers");
                        ArrayList<String> listdata = new ArrayList<String>();
                        if (answers != null) {
                            for (int z=0;z<answers.length();z++){
                                listdata.add(answers.get(z).toString());
                            }
                        }
                        dir = repo.addTopicQuestion(title,desc,q,answer,listdata);
                    }
                    quizes.add(title);
                }
               // System.out.println(names.toString());
                } catch(Exception e){
                    System.out.println(e.toString());
                }
        } else {
            quizes = addTopics();
            topicQ = addQuestions();
            questions = initQuestions(quizes);
            answers = initAnswers(quizes);
            possibleAnswers = setPossibleAnswers();

           dir  = repo.createRepo(quizes, questions, answers, possibleAnswers);
        }
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
            Intent i = new Intent(MainActivity.this, PreferenceActivity.class);
            if (i.resolveActivity(getPackageManager()) != null) {
                startActivity(i); // opens a new activity
            }
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

    public void onDestroy(){
        super.onDestroy();
    }


    private String readJSONFile (FileInputStream in) {
        String json = null;
        try {

            int size = in.available();

            byte[] buffer = new byte[size];

            in.read(buffer);

            in.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}
