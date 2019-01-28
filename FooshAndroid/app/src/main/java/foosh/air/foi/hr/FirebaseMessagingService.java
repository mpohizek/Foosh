package foosh.air.foi.hr;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * Klasa za komunikaciju sa Firebase Cloud Messaging-om. Služi za slanje i primanje notifikacija.
*/
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "FirebaseMessagingService";

    /**
     * Obvezan prazan konstruktor
     */
    public FirebaseMessagingService() {
    }

    @Override
    public void onCreate() {
    }

    /**
     * Događaj koji se poziva nakon što FirebaseMessagingService dobije poruku
     * @param remoteMessage Poruka koja se dobije od Firebase-a, sadrži naziv i tijelo poruke
     */
    @SuppressLint("LongLogTag")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        Log.d(TAG, "onMessageReceived: Message Received: \n" + "Title: " + title + "\n" + "Message: " + message);
        sendNotification(title, message);

    }

    @Override
    public void onDeletedMessages() {
    }

    /**
     * Kreira notifiakciju i šalje je na mobitel.
     * @param title Naslov notifikacije
     * @param messageBody Poruka unutar notifikacije
     */
    private void sendNotification(String title,String messageBody) {
        Intent intent = new Intent(this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "Default";
        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
    }

    /**
     * Statična metoda za slanje notifikacije useru
     * @param user Korsnik koji dobiva notifikaciju
     * @param title Naslov notifikacije
     * @param message Poruka unutar notifikacije
     */
    public static void sendNotificationToUser(String user, final String title, final String message) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference notifications = ref.child("notificationRequests");

        Map notification = new HashMap<>();
        notification.put("username", user);
        notification.put("title", title);
        notification.put("message", message);

        notifications.push().setValue(notification);
    }
}