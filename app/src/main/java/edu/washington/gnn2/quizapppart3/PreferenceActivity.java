package edu.washington.gnn2.quizapppart3;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import
        android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.prefs.PreferenceChangeEvent;


public class PreferenceActivity extends ActionBarActivity {
 private static Intent intent;
    private static PendingIntent pi;
    private int PERIOD;
    private String TAG = ".PreferenceActivity";
    private static String d = "1";
    private static String m = "http://tednewardsandbox.site44.com/questions.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        Button b = (Button) findViewById(R.id.submit);
        Button c = (Button) findViewById(R.id.cancel);

        if(m != null && d!= null){
            EditText dur = (EditText) findViewById(R.id.theTime);
            dur.setText(d);
            EditText url = (EditText) findViewById(R.id.theURL);
            url.setText(m);
        }

        c.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //stop service
                System.out.println("toasts should stop");
                Button b = (Button) findViewById(R.id.submit);
                b.setTag("off");

                PendingIntent contentIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.cancel(contentIntent);
                contentIntent.cancel();


            }

        });


        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText dur = (EditText) findViewById(R.id.theTime);
                d = dur.getText().toString();
                EditText url = (EditText) findViewById(R.id.theURL);
                 m = url.getText().toString();
                int duration = Integer.parseInt(d);
                if( duration > 0 && m != null && !m.isEmpty()){

                    if(pi != null){ //cancel previous pending intent when url or time is updated
                        PendingIntent contentIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        alarmManager.cancel(contentIntent);
                        contentIntent.cancel();
                    }

                    System.out.println("Should make toast alarms");
                    Button b = (Button) findViewById(R.id.submit);
                    b.setTag("on");
                    PERIOD = duration;
                    //make service
                    AlarmManager mgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                    intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                    intent.putExtra("message", m);

                     pi = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
                    PERIOD = PERIOD * 60000;
                    mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 30000, PERIOD, pi);
                    Button c = (Button) findViewById(R.id.cancel);

                    Intent i = new Intent(PreferenceActivity.this, MainActivity.class);
                    if (i.resolveActivity(getPackageManager()) != null) {
                        startActivity(i); // opens a new activity
                    }
                   }

                else {
                    Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_LONG).show();
                }

            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_preference, menu);
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

    public void onDestroy(){
        super.onDestroy();
        Log.i(TAG,"Activity is destroyed, toasts should be canceled?");
       // PendingIntent contentIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
       // AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
       // alarmManager.cancel(contentIntent);
       // contentIntent.cancel();
    }
}
