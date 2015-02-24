package edu.washington.gnn2.quizapppart3;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import
        android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.prefs.PreferenceChangeEvent;


public class PreferenceActivity extends ActionBarActivity {
 private static Intent intent;
    private int PERIOD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        Button b = (Button) findViewById(R.id.submit);
        Button c = (Button) findViewById(R.id.cancel);

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
                String d = dur.getText().toString();
                EditText url = (EditText) findViewById(R.id.theURL);
                String m = url.getText().toString();
                int duration = Integer.parseInt(d);
                if( duration > 0 && m != null && !m.isEmpty()){
                    System.out.println("Should make taost alarms");
                    Button b = (Button) findViewById(R.id.submit);
                    b.setTag("on");
                    PERIOD = duration;
                    //make service
                    AlarmManager mgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                    intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                    intent.putExtra("message", m);

                    PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
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
}
