package edu.washington.gnn2.quizapppart3;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DownloadReceiver extends BroadcastReceiver {
    private String location = "";
    private String rightLocation;
    private String status;
    private Uri uri;

    public DownloadReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        // an Intent broadcast.
        location = intent.getStringExtra("downloadLocation");
        rightLocation = intent.getStringExtra("shouldDownload");
        long reference = (long) intent.getLongExtra("downloadReference", 0);

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        uri = downloadManager.getUriForDownloadedFile(reference);
//        System.out.println(uri.toString());
        DownloadManager.Query myDownloadQuery = new DownloadManager.Query();
        //set the query filter to our previously Enqueued download
        myDownloadQuery.setFilterById(reference);
        Cursor cursor = downloadManager.query(myDownloadQuery);

        if (cursor.moveToFirst()) {
            //  cursor.getColumnIndex(DownloadManager.COLUMN_T)
           status = checkStatus(cursor);
        }
        if(location.equalsIgnoreCase(rightLocation) && status.equalsIgnoreCase("STATUS_SUCCESSFUL")){

            try{

                ParcelFileDescriptor file;
                //parse the JSON data and display on the screen
                try {
                    file = downloadManager.openDownloadedFile(reference);
                    FileInputStream fis = new FileInputStream(file.getFileDescriptor());
                    String f = readJSONFile(fis);
                    writeToFile(f, context);
                } catch (Exception e) {
                    System.out.println(e.getStackTrace().toString());
                }

            } catch (Exception e) {
                System.out.println(e.getStackTrace().toString());

            }

        } else {
            Toast toast = Toast.makeText(context, "Download was not successful. Please try again later." , Toast.LENGTH_LONG);
            toast.show();
        }
        //downloadManager.remove(reference);
       // throw new UnsupportedOperationException("Not yet implemented");
    }

    private String checkStatus(Cursor cursor) {
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
        System.out.println(reasonText + ":" + statusText);
       // toast.show();
        return statusText;
    }

    private void writeToFile(String data, Context context){
        try{


            File file = new File(context.getFilesDir().getAbsolutePath(),"quizdata.json");

            System.out.println(context.getFilesDir().getAbsolutePath());
            FileOutputStream  fos = new FileOutputStream(file);
            fos.write(data.getBytes());

        } catch(Exception e){

        }


    }

    private String readJSONFile (FileInputStream in) {
        String json = null;
        try {

            int size = in.available();

            byte[] buffer = new byte[size];

            in.read(buffer);

            in.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}

