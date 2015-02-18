package edu.washington.gnn2.quizapppart3;

import android.app.Application;
import android.view.Menu;
import android.util.Log;


public class QuizApp extends Application {
    private static final String TAG = "QuizAppActivity";
    private static QuizApp singleton;

    public static QuizApp getInstance(){
        return singleton;
    }
    @Override
    public void onCreate() {
        super.onCreate();
       // setContentView(R.layout.activity_quiz_app);
        Log.i(TAG,"ON CREATE: application started");
        singleton = this;

    }

  //  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.menu_quiz_app, menu);
        return true;
    }

    /**
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
    */
}
