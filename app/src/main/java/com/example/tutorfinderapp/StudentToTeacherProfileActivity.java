package com.example.tutorfinderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentToTeacherProfileActivity extends AppCompatActivity {
    TextView teacherName,teacherEmail,teacherLoc,teacherSub,teacherPhn;
    Button sTBtn;
    FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_to_teacher_profile);
        teacherName = findViewById(R.id.stProname);
        teacherEmail = findViewById(R.id.stProEmail);
        teacherPhn = findViewById(R.id.stProPhn);
        teacherLoc = findViewById(R.id.stProLoc);
        teacherSub = findViewById(R.id.stProSub);
        sTBtn =findViewById(R.id.sTBtn);

        final String id = getIntent().getStringExtra("ID");
        teacherName.setText(id);

        mAuth = FirebaseAuth.getInstance();
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Teacher").child(id);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("Name").getValue().toString();
                String email = dataSnapshot.child("Email").getValue().toString();
                String phone = dataSnapshot.child("Phone").getValue().toString();
                String sub = dataSnapshot.child("Subject").getValue().toString();
                String loc = dataSnapshot.child("Location").getValue().toString();
                String uid = dataSnapshot.child("User_ID").getValue().toString();
                teacherName.setText(name);
                teacherEmail.setText(email);
                teacherPhn.setText(phone);
                teacherLoc.setText(loc);
                teacherSub.setText(sub);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        DatabaseReference tDatabase = FirebaseDatabase.getInstance().getReference().child("Connect").child(mAuth.getCurrentUser().getUid());
        tDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(id)) {

                    sTBtn.setText("Unfriend");
                }else{
                    DatabaseReference tDatabase = FirebaseDatabase.getInstance().getReference().child("Request").child(mAuth.getCurrentUser().getUid());
                    tDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(id)) {

                                sTBtn.setText("Cancel Request");
                            }else{
                                sTBtn.setText("Sent Request");
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    sTBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
                         DatabaseReference tDatabase = FirebaseDatabase.getInstance().getReference().child("Request").child(mAuth.getCurrentUser().getUid());
                        tDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                /*if (dataSnapshot.hasChild(id)) {

                                    sTBtn.setText("Sent Request");
                                    FirebaseDatabase.getInstance().getReference().child("Request").child(mAuth.getCurrentUser().getUid()).child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(StudentToTeacherProfileActivity.this, "Cancel Friend Requst", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });

                                }else{*/
                                    FirebaseDatabase.getInstance().getReference().child("Request").child(mAuth.getCurrentUser().getUid()).child(id).child("Status").setValue("Sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                                Toast.makeText(StudentToTeacherProfileActivity.this, "Sent Friend Requst", Toast.LENGTH_SHORT).show();
                                                FirebaseDatabase.getInstance().getReference().child("Request").child(id).child(mAuth.getCurrentUser().getUid()).child("Status").setValue("Receive").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                sTBtn.setText("Cancel Request");
                                                        }
                                                    }
                                                });
                                            }//
                                        }
                                    });
                                /*}*/

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }

                        });
/*
                        DatabaseReference lDatabase = FirebaseDatabase.getInstance().getReference().child("Request").child(mAuth.getCurrentUser().getUid());
            lDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(id)) {

                                    sTBtn.setText("Sent Request");
                                    FirebaseDatabase.getInstance().getReference().child("Request").child(mAuth.getCurrentUser().getUid()).child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(StudentToTeacherProfileActivity.this, "Cancel Friend Requst", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });

                                }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });*/

                    }


    });

    }
}