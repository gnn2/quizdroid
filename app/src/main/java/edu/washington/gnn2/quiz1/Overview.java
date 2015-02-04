package edu.washington.gnn2.quiz1;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;


public class Overview extends ActionBarActivity {
    private String name;
    private int numQuestions;
    private String[] q;
    private String[] a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        // Get the Intent that opened this activity
        Intent launchedMe = getIntent();
        name = launchedMe.getStringExtra("Overview_name");  // get data that was passed from first activity
        numQuestions = launchedMe.getIntExtra("num", 20);
        q = launchedMe.getStringArrayExtra("Topic_Questions");
        a = launchedMe.getStringArrayExtra("Topic_Answers");

        // add the extra data into the text view of the 2nd activity (this layout)
        TextView tv = (TextView) findViewById(R.id.overviewText);
        tv.setText(name + " Overview");
        tv.setTextSize(20);

        TextView tvv = (TextView) findViewById(R.id.numOfQuestions);
        tvv.setText("Number of questions: " + numQuestions);
        tvv.setTextSize(15);

        Button b = (Button) findViewById(R.id.takeQuiz);

        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent launchedMe = getIntent();
                int correct = launchedMe.getIntExtra("Correct",0);
            Intent nextActivity = new Intent(Overview.this, QuizQuestion.class); // cannot use just this cuz this refers to the listener, not the outer this

            // add data to be passed to next activity
            nextActivity.putExtra("Overview_name",name);

                nextActivity.putExtra("Topic_Questions",q);
                nextActivity.putExtra("Topic_Answers",a);
                nextActivity.putExtra("num", q.length);
                nextActivity.putExtra("Correct", correct);
                nextActivity.putExtra("curr",1);


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
        getMenuInflater().inflate(R.menu.menu_overview, menu);
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
