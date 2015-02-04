package edu.washington.gnn2.quiz1;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class QuizQuestion extends ActionBarActivity {
    private String answer;
    private String[] a;
    private String[] q;
    private int correct;
    private int numQuestions;
    private int currQuestion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_question);

        Intent launchedMe = getIntent();
        //String name = launchedMe.getStringExtra("Overview_name");  // get data that was passed from first activity
        numQuestions = launchedMe.getIntExtra("num", 20);
        currQuestion = launchedMe.getIntExtra("curr", 1);
        correct = launchedMe.getIntExtra("Correct", correct);
        q = launchedMe.getStringArrayExtra("Topic_Questions");
        a = launchedMe.getStringArrayExtra("Topic_Answers");


        // add the extra data into the text view of the 2nd activity (this layout)
        TextView tv = (TextView) findViewById(R.id.question);
        tv.setText("Question " + currQuestion + ": " + q[currQuestion -1]);
        tv.setTextSize(20);

        RadioButton a1 = (RadioButton) findViewById(R.id.rb1);
        a1.setText("Answer 1");
        a1.setTextSize(20);

        RadioButton a2 = (RadioButton) findViewById(R.id.rb2);
        a2.setText("Answer 2");
        a2.setTextSize(20);

        RadioButton a3 = (RadioButton) findViewById(R.id.rb3);
        a3.setText("Answer 3");
        a3.setTextSize(20);

        RadioButton a4 = (RadioButton) findViewById(R.id.rb4);
        a4.setText("Answer 4");
        a4.setTextSize(20);

        Button b = (Button) findViewById(R.id.submit);

        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

                Intent nextActivity = new Intent(QuizQuestion.this, Summary.class); // cannot use just this cuz this refers to the listener, not the outer this
                nextActivity.putExtra("curr", currQuestion);
                nextActivity.putExtra("Topic_Questions", q);
                nextActivity.putExtra("Topic_Answer", a);
                nextActivity.putExtra("User_Answer", answer);
                nextActivity.putExtra("num", numQuestions);
                String realAnswer = a[currQuestion-1];
                System.out.println("The answer to this question is: " + realAnswer);
                if(realAnswer.equalsIgnoreCase(answer)){
                    nextActivity.putExtra("Correct", (correct+1));
                } else {
                    nextActivity.putExtra("Correct", correct);
                }

               if(nextActivity.resolveActivity(getPackageManager())!=null) {
                    startActivity(nextActivity); // opens a new activity
                }
                // code still runs asynchronously

               // finish(); // kill this instance self (this activity)
            }
        });
    }

    public void onClick(View v){
        System.out.println("Radio button chosen");
       RadioButton b = (RadioButton) v;
        String text = b.getText().toString();
        answer = text;
        System.out.println("Button " + text);
        RadioGroup group = (RadioGroup) v.getParent();
        LinearLayout lay = (LinearLayout) group.getParent();
        Button sub = (Button) lay.findViewById(R.id.submit);
        if(sub.getVisibility() != v.VISIBLE) {
            sub.setVisibility(v.VISIBLE);
        }
     }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz_question, menu);
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
