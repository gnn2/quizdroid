package edu.washington.gnn2.quiz1;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity {
    public static HashMap<String, String[]> questions;
    public static HashMap<String, String[]> answers;
    public static ArrayList<String> quizes;
    public static String[] topicQ = new String[5];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topicQ[0] = "Question 1?";
        topicQ[1] = "Question 2?";
        topicQ[2] = "Question 3?";
        topicQ[3] = "Question 4?";
        topicQ[4] = "Question 5?";
        quizes = addTopics();
        questions = initQuestions(quizes);
        answers = initAnswers(quizes);

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
                    Intent nextActivity = new Intent(MainActivity.this, Overview.class); // cannot use just this cuz this refers to the listener, not the outer this

                    // add data to be passed to next activity
                    nextActivity.putExtra("Overview_name", name);
                    String [] q = questions.get(name);
                    String [] a = answers.get(name);
                    nextActivity.putExtra("Topic_Questions",q);
                    nextActivity.putExtra("num", q.length);
                    nextActivity.putExtra("Topic_Answers",a);
                    nextActivity.putExtra("Correct", 0);
                    nextActivity.putExtra("curr",1);
                    if (nextActivity.resolveActivity(getPackageManager()) != null) {
                        startActivity(nextActivity); // opens a new activity
                    }
                    // code still runs asynchronously

                   // finish(); // kill this instance self (this activity)
                }
            });

     }

    private HashMap<String,String[]> initQuestions(ArrayList<String> topics){
        questions= new HashMap<String,String[]>();
        for(String topic: topics){
            if(!questions.containsKey(topic)){
                questions.put(topic, topicQ);
            }
        }
        return questions;
    }

    private HashMap<String,String[]> initAnswers(ArrayList<String> topics){
        answers= new HashMap<String,String[]>();

        for(String topic: topics){
            if(!answers.containsKey(topic)){
                String[] a = getAnswer(topic);
                answers.put(topic, a);
            }
        }
        return answers;
    }

    private String[] getAnswer(String topic){
        String[] answers = new String[questions.get(topic).length];
        answers[0] = "Answer 2";
        answers[1] = "Answer 3";
        answers[2] = "Answer 3";
        answers[3] = "Answer 4";
        answers[4] = "Answer 1";
        return answers;
    }
    private ArrayList<String> addTopics(){
        quizes = new ArrayList<String>();
        quizes.add("Math");
        quizes.add("Computer Science");
        quizes.add("Music");
        quizes.add("Physics");
        quizes.add("Marvel Super Heroes");
        return quizes;
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
}
