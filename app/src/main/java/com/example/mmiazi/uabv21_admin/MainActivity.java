package com.example.mmiazi.uabv21_admin;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button sendReview, sendRestaurant, clearButton;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceRec;
    private String CHANNEL_ID = "Channel";
    private String notificationCommand = "empty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        setTitle("Users as Beacons Admin");

        //Notifications...
        databaseReferenceRec = firebaseDatabase.getReference().child("signalToAdmin").child("command");
        databaseReferenceRec.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notificationCommand = dataSnapshot.getValue().toString();
                if(notificationCommand.equals("advertised1")) {
                    Log.d("test", notificationCommand+"");
                    createNotification(1);
                    databaseReferenceRec.setValue("empty");
                }else if(notificationCommand.equals("advertised2")) {
                    Log.d("test", notificationCommand+"");
                    createNotification(2);
                    databaseReferenceRec.setValue("empty");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        clearButton = findViewById(R.id.buttonClear);

        sendReview = findViewById(R.id.buttonService);
        sendRestaurant = findViewById(R.id.buttonRestaurant);

        sendReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = firebaseDatabase.getReference();
                databaseReference.child("signalFromAdmin/command").setValue("advertised1");
            }
        });
        sendRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = firebaseDatabase.getReference();
                databaseReference.child("signalFromAdmin/command").setValue("advertised2");
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = firebaseDatabase.getReference();

                databaseReference.child("fromUser/review1/rating").setValue("empty");
                databaseReference.child("fromUser/review1/comment").setValue("empty");
                databaseReference.child("fromUser/review1/isNameShared").setValue("false");
                databaseReference.child("fromUser/review1/isPhotoShared").setValue("false");
                databaseReference.child("fromUser/review2/rating").setValue("empty");
                databaseReference.child("fromUser/review2/comment").setValue("empty");
                databaseReference.child("fromUser/review2/isNameShared").setValue("false");
                databaseReference.child("fromUser/review2/isPhotoShared").setValue("false");

                databaseReference.child("signalFromAdmin/command").setValue("empty");
                databaseReference.child("signalToAdmin/command").setValue("empty");

                databaseReference.child("message/userEmail").setValue("empty");
                databaseReference.child("message/userName").setValue("empty");
                databaseReference.child("message/userPhoto").setValue("empty");
            }
        });

    }
    private void createNotification(int i){
        //        TODO: Notification receive.....
        createNotificationChannel();
        if(i==1){
            Intent intent = new Intent(this, Review1.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(intent);
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notify)
                    .setContentTitle("Review Received!")
                    .setContentText("Someone nearby published a review!")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("Someone nearby published a review!"))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(1, mBuilder.build());

        }else if(i==2){
            Intent intent = new Intent(this, Review2.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(intent);
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notify)
                    .setContentTitle("Review Received!")
                    .setContentText("Someone nearby published a review!")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("Someone nearby published a review!"))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(1, mBuilder.build());
        }

    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
