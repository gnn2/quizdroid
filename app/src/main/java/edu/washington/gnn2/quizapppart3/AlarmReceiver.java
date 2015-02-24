package edu.washington.gnn2.quizapppart3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    private String TAG = ".AlarmReciever";
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String m = intent.getStringExtra("message");

        Toast.makeText(context, m , Toast.LENGTH_LONG).show();
        Log.i(TAG, "entered intent and should pop toast");
       // throw new UnsupportedOperationException("Not yet implemented");
    }
}
