package com.example.mmiazi.uabv21_admin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Review2 extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage storage;
    DatabaseReference databaseReference;
    //RatingBar ratingBar;
    TextView textUserName, textComment;
    ImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review2);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storage = FirebaseStorage.getInstance();

        DatabaseReference nameRef = databaseReference.child("fromUser/review2/isNameShared");
        DatabaseReference photoRef = databaseReference.child("fromUser/review2/isPhotoShared");

        DatabaseReference databaseReferenceComment = databaseReference.child("fromUser/review2/comment");
        final DatabaseReference databaseReferenceName = databaseReference.child("message/userName");

        textUserName = findViewById(R.id.tv_userName2);
        textComment = findViewById(R.id.tv_userReview2);

        profilePic = findViewById(R.id.iv_cad2_photo);

        databaseReferenceComment.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textComment.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final String[] isNameShared = new String[1];
        final String[] isPhotoShared = new String[1];

        nameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                isNameShared[0] = dataSnapshot.getValue(String.class);
                databaseReferenceName.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(isNameShared[0].equals("true"))textUserName.setText(dataSnapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        photoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                isPhotoShared[0] = dataSnapshot.getValue(String.class);
                if(isPhotoShared[0].equals("true")){
                    StorageReference profilePicStorage = storage.getReference().child("photos/photo.jpg");

                    final long One_MB = 1024*1024;
                    profilePicStorage.getBytes(One_MB).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                            profilePic.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
                }else profilePic.setImageResource(R.drawable.user_photo_not_selected);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
