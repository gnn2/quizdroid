package edu.washington.gnn2.quizapppart3;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class QuizActivity extends ActionBarActivity {
    //public static questionMaster questions;
    public static Intent i;
    public static int selection;
    public static Topic t;
    public String name;
    private static int curr;
    private static int correct;
    private static String answer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Intent launchedMe = getIntent();
       name = launchedMe.getStringExtra("Overview_name");
        t = (Topic) launchedMe.getSerializableExtra("topic");
        i = getIntent();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
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
     * A placeholder fragment containing a simple view.
     *
     * TopicOverview fragment
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
          View rootView = inflater.inflate(R.layout.activity_overview, container, false);
            curr = 0;
            correct =0;
            QuizApp quiz = QuizApp.getInstance();
            TextView tv = (TextView) rootView.findViewById(R.id.overviewText);
            String quizName = t.getTitle();
            tv.setText(quizName+ " Overview");
            tv.setTextSize(20);

            TextView longText = (TextView)rootView.findViewById(R.id.overviewExt);
            longText.setText(t.getDescriptionLong());
            longText.setTextSize(15);

            TextView tvv = (TextView) rootView.findViewById(R.id.numOfQuestions);
            tvv.setText("Number of questions: " + t.getQuizQuestions().size());
            tvv.setTextSize(15);

            Button b = (Button) rootView.findViewById(R.id.takeQuiz);

            b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v){
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, new QuestionFragment())
                            .commit();
                }
            });
            return rootView;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     *
     * TopicOverview fragment
     */
    public static class QuestionFragment extends Fragment {

        public QuestionFragment() {
        }

       private View rootView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
           rootView = inflater.inflate(R.layout.activity_quiz_question, container, false);

            QuizApp quiz = QuizApp.getInstance();
            // add the extra data into the text view of the 2nd activity (this layout)
            TextView tv = (TextView) rootView.findViewById(R.id.question);
            String question = t.getQuizQuestions().get(curr).getQuestion();
            tv.setText("Question " + (curr+1) + ": " + question);
            tv.setTextSize(20);

            RadioButton a1 = (RadioButton) rootView.findViewById(R.id.rb1);
            a1.setText(t.getQuizQuestions().get(curr).getPossibleAnswers().get(0));
            a1.setTextSize(20);

            RadioButton a2 = (RadioButton) rootView.findViewById(R.id.rb2);
            a2.setText(t.getQuizQuestions().get(curr).getPossibleAnswers().get(1));
            a2.setTextSize(20);

            RadioButton a3 = (RadioButton) rootView.findViewById(R.id.rb3);
            a3.setText(t.getQuizQuestions().get(curr).getPossibleAnswers().get(2));
            a3.setTextSize(20);

            RadioButton a4 = (RadioButton) rootView.findViewById(R.id.rb4);
            a4.setText(t.getQuizQuestions().get(curr).getPossibleAnswers().get(3));
            a4.setTextSize(20);

            RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.radioGroup);
            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    System.out.println("Radio button chosen");
                    RadioButton b = (RadioButton) rootView.findViewById(checkedId);
                    String text = b.getText().toString();
                    answer = text;
                    System.out.println("Button " + text);
                    Button sub = (Button) rootView.findViewById(R.id.submit);
                    if(sub.getVisibility() != rootView.VISIBLE) {
                        sub.setVisibility(rootView.VISIBLE);
                    }
                }
            });
            Button b = (Button) rootView.findViewById(R.id.submit);

            b.setOnClickListener(new View.OnClickListener() {
               public void onClick(View v){

                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, new SummaryFragment())
                            .commit();

                }

            });
            return rootView;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     *
     * TopicOverview fragment
     */
    public static class SummaryFragment extends Fragment {

        public SummaryFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_summary, container, false);

            QuizApp quiz = QuizApp.getInstance();
            TextView s = (TextView) rootView.findViewById(R.id.answers);
            int correctIndex = t.getQuizQuestions().get(curr).getCorrectAnswer();
            String correctAnswer =  t.getQuizQuestions().get(curr).getPossibleAnswers().get(correctIndex);
            s.setText("Your Answer: " + answer + "\n" + "Correct Answer: " + correctAnswer);
            s.setTextSize(20);
            if(answer.equalsIgnoreCase(correctAnswer)){
                correct++;
            }
            TextView score = (TextView) rootView.findViewById(R.id.score);
            score.setText("You have " + correct + " correct out of " + (curr+1));
            Button b;
            if((curr + 1) < t.getQuizQuestions().size()){
                b = (Button) rootView.findViewById(R.id.next);
                b.setVisibility(View.VISIBLE);
                b.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v){
                        curr++;
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, new QuestionFragment())
                                .commit();
                    }
                });
            } else {
                b = (Button) rootView.findViewById(R.id.finish);
                b.setVisibility(View.VISIBLE);
                b.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v){
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        ((QuizActivity) getActivity()).startActivity(intent);
                    }
                });
            }


            return rootView;
        }
    }
}
