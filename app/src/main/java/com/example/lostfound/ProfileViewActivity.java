package com.example.lostfound;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileViewActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    private ImageView imageView;
    private TextView textViewUser, textViewSchool;
    private Button buttonBack, buttonCall, buttonMessage;

    private String imageUrl;
    private String imageName;
    private String userId;
    private String phoneNum;

    public static final String LOSTPost_USERID = "com.example.lostfound.lostPostuserid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        Intent intent = getIntent();

        imageView = (ImageView) findViewById(R.id.imageView);
        textViewUser = (TextView) findViewById(R.id.textViewUser);
        textViewSchool = (TextView) findViewById(R.id.textViewSchool);
        buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonCall = (Button) findViewById(R.id.buttonCall);
        buttonMessage = (Button) findViewById(R.id.buttonMessage);

        userId = intent.getStringExtra(PostViewActivity.Post_PROFILE);

        buttonBack.setOnClickListener(this);

        buttonCall.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNum, null));
                startActivity(intent);
            }
        });

        buttonMessage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                intent.putExtra(LOSTPost_USERID,userId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference = FirebaseDatabase.getInstance().getReference("/USERS/" + userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User User = dataSnapshot.child("INFO").getValue(User.class);
                if (User != null){
                    textViewUser.setText(User.getName());
                    phoneNum = User.getPhoneNum();
                    textViewSchool.setText(User.getSchool());
                    imageUrl = dataSnapshot.child("IMAGE").child("imageUrl").getValue(String.class);
                    imageName = dataSnapshot.child("IMAGE").child("name").getValue(String.class);
                    Picasso.get().load(imageUrl).fit().into(imageView);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view == buttonBack){
            finish();
            startActivity(new Intent(this, LostActivity.class));
        }
    }
}
