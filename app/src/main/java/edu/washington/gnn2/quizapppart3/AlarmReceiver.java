package edu.washington.gnn2.quizapppart3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.File;

public class AlarmReceiver extends BroadcastReceiver {
    private String TAG = ".AlarmReciever";
   private String filePath = "quizdata.json";
   private String tempFile = "data.json";
    private String downloadName;
    private long downloadReference;
    private File direct = new File(Environment.DIRECTORY_DOWNLOADS);


    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // an Intent broadcast.
        Log.i(TAG, "entered intent and start download");
        String m = intent.getStringExtra("message");
        Toast.makeText(context, m , Toast.LENGTH_LONG).show();
        download(context, m);

    }
    private void download(Context context, String url){

        try {

            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            boolean isDownloading = false;
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterByStatus(
                    DownloadManager.STATUS_PAUSED|
                            DownloadManager.STATUS_PENDING|
                            DownloadManager.STATUS_RUNNING|
                            DownloadManager.STATUS_SUCCESSFUL);
            Cursor cur = downloadManager.query(query);
            int col = cur.getColumnIndex(
                    DownloadManager.COLUMN_LOCAL_FILENAME);
            for(cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                isDownloading = isDownloading || (direct.toString().equalsIgnoreCase(cur.getString(col)));
            }


            if (!isDownloading && !direct.exists()) {
                downloadName = filePath;

            } else {
                downloadName = tempFile;
            }
            File end = new File(Environment.DIRECTORY_DOWNLOADS + "/" + downloadName);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalFilesDir(context,Environment.DIRECTORY_DOWNLOADS , downloadName)
                    .setMimeType("application/json")

                    .allowScanningByMediaScanner();


            //Enqueue a new download and same the referenceId
            downloadReference = downloadManager.enqueue(request);

            cur.close();

            Intent intent = new Intent(context, DownloadReceiver.class);
            intent.setAction(downloadManager.ACTION_DOWNLOAD_COMPLETE);
            intent.putExtra("downloadLocation", end.getAbsolutePath().toString());
            intent.putExtra("shouldDownload", (direct.getAbsolutePath().toString()+ "/" + filePath));
            intent.putExtra("downloadReference", downloadReference);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
            context.sendBroadcast(intent);

            DownloadManager.Query myDownloadQuery = new DownloadManager.Query();

            //set the query filter to our previously Enqueued download
            myDownloadQuery.setFilterById(downloadReference);

            //Query the download manager about downloads that have been requested.
            Cursor cursor = downloadManager.query(myDownloadQuery);
            if (cursor.moveToFirst()) {
                //  cursor.getColumnIndex(DownloadManager.COLUMN_T)
               checkStatus(cursor, context);


            }
            cursor.close();

        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }

    }


    private void checkStatus(Cursor cursor, Context context) {
        //column for status
        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
        int status = cursor.getInt(columnIndex);
        //column for reason code if the download failed or paused
        int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
        int reason = cursor.getInt(columnReason);
        //get the download filename
        int filenameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
        String filename = cursor.getString(filenameIndex);

        String statusText = "";
        String reasonText = "";

        switch (status) {
            case DownloadManager.STATUS_FAILED:
                statusText = "STATUS_FAILED";
                switch (reason) {
                    case DownloadManager.ERROR_CANNOT_RESUME:
                        reasonText = "ERROR_CANNOT_RESUME";
                        break;
                    case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                        reasonText = "ERROR_DEVICE_NOT_FOUND";
                        break;
                    case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                        reasonText = "ERROR_FILE_ALREADY_EXISTS";
                        break;
                    case DownloadManager.ERROR_FILE_ERROR:
                        reasonText = "ERROR_FILE_ERROR";
                        break;
                    case DownloadManager.ERROR_HTTP_DATA_ERROR:
                        reasonText = "ERROR_HTTP_DATA_ERROR";
                        break;
                    case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                        reasonText = "ERROR_INSUFFICIENT_SPACE";
                        break;
                    case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                        reasonText = "ERROR_TOO_MANY_REDIRECTS";
                        break;
                    case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                        reasonText = "ERROR_UNHANDLED_HTTP_CODE";
                        break;
                    case DownloadManager.ERROR_UNKNOWN:
                        reasonText = "ERROR_UNKNOWN";
                        break;
                }
                break;
            case DownloadManager.STATUS_PAUSED:
                statusText = "STATUS_PAUSED";
                switch (reason) {
                    case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                        reasonText = "PAUSED_QUEUED_FOR_WIFI";
                        break;
                    case DownloadManager.PAUSED_UNKNOWN:
                        reasonText = "PAUSED_UNKNOWN";
                        break;
                    case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                        reasonText = "PAUSED_WAITING_FOR_NETWORK";
                        break;
                    case DownloadManager.PAUSED_WAITING_TO_RETRY:
                        reasonText = "PAUSED_WAITING_TO_RETRY";
                        break;
                }
                break;
            case DownloadManager.STATUS_PENDING:
                statusText = "STATUS_PENDING";
                break;
            case DownloadManager.STATUS_RUNNING:
                statusText = "STATUS_RUNNING";
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                statusText = "STATUS_SUCCESSFUL";
                reasonText = "Filename:\n" + filename;
                break;
        }


        Toast toast = Toast.makeText(context, statusText + "\n" + reasonText ,Toast.LENGTH_LONG);
        //   toast.setGravity(Gravity.TOP, 25, 400);
        System.out.println(reasonText + ":" + statusText);
        toast.show();
        //return statusText;
    }


}



