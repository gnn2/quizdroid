package edu.washington.gnn2.quiz1;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Summary extends ActionBarActivity {
     private String[] a;
    private String[] q;
    private int correct;
    private int currQuestion;
    private int numQuestions;
    private String answer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        Intent launchedMe = getIntent();


         // get data that was passed from first activity
          numQuestions = launchedMe.getIntExtra("num", 20);
         currQuestion = launchedMe.getIntExtra("curr", 1);
         correct = launchedMe.getIntExtra("Correct", 0);
         q =  launchedMe.getStringArrayExtra("Topic_Questions");
         a = launchedMe.getStringArrayExtra("Topic_Answer");
         answer = launchedMe.getStringExtra("User_Answer").toString();

        // add the extra data into the text view of the 2nd activity (this layout)
        String realAnswer= a[(currQuestion -1)];
        TextView s = (TextView) findViewById(R.id.answers);
        s.setText("Your Answer: " + answer + "\n" + "Correct Answer: " + realAnswer);
        s.setTextSize(20);
        TextView score = (TextView) findViewById(R.id.score);
        score.setText("You have " + correct + " correct out of " + currQuestion);
        Button b;
        if(currQuestion < numQuestions){
            b = (Button) findViewById(R.id.next);
            b.setVisibility(View.VISIBLE);
        } else {
             b = (Button) findViewById(R.id.finish);
            b.setVisibility(View.VISIBLE);
        }

        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Button b;
                if(currQuestion < numQuestions){
                   //currQuestion = currQuestion + 1;
                    b = (Button) findViewById(R.id.next);
                    b.setVisibility(View.VISIBLE);
                } else {
                    b = (Button) findViewById(R.id.finish);
                    b.setVisibility(View.VISIBLE);
                }
                 Intent nextActivity;
                if(b.getText().toString().equalsIgnoreCase("Next Question")){
                   nextActivity = new Intent(Summary.this, QuizQuestion.class);
                    nextActivity.putExtra("curr", (currQuestion+1));
                    nextActivity.putExtra("Topic_Questions", q);
                    nextActivity.putExtra("Topic_Answers", a);
                    nextActivity.putExtra("Correct", correct);
                    nextActivity.putExtra("num", numQuestions);

                } else {
                    nextActivity = new Intent(Summary.this, MainActivity.class);
                }
               // cannot use just this cuz this refers to the listener, not the outer this

                if(nextActivity.resolveActivity(getPackageManager())!=null) {
                    startActivity(nextActivity); // opens a new activity
                }
                // code still runs asynchronously

                finish(); // kill this instance self (this activity)
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_summary, menu);
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

    private void setButton(int currQuestion, int totalQuestion){

    }
}
