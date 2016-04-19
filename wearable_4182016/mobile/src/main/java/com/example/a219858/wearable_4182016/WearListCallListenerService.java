package com.example.a219858.wearable_4182016;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class WearListCallListenerService extends WearableListenerService {

    private static final String VOICE_TRANSCRIPTION = "voice_transcription";
    private SendPiCommand sendPiCommand;

    public static String HOST = "10.0.0.42", PORT = "8080";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);

        String event = messageEvent.getPath();

        Log.d("Listclicked", event);

        String[] message = event.split("\\s+");

        if (message[0].equals(VOICE_TRANSCRIPTION)) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Light Control")
                    .setContentText(message[1]);
            // Sets an ID for the notification
            int mNotificationId = 001;
            // Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            sendPiCommand = new SendPiCommand();
            if (HOST == null || PORT == null) {
                HOST = "10.0.0.42";
                PORT = "8080";
            }
            sendPiCommand.execute(HOST, PORT, message[1]);

            // Builds the notification and issues it.
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }
    }

    /**
     * Async task to send commands to the Raspberry Pi.
     */
    private static class SendPiCommand extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String host = params[0];
            int port = Integer.parseInt(params[1]);
            String spokenText = params[2];

            Socket commandSocket = null;
            String command = spokenText;

            try {
                commandSocket = new Socket(host, port);
                PrintWriter out = new PrintWriter(commandSocket.getOutputStream(), true);
                out.write(command);
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}