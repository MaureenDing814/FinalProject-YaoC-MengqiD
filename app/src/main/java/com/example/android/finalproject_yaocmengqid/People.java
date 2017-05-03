package com.example.android.finalproject_yaocmengqid;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

/**
 * Created by Maureen_Ding on 4/24/17.
 */

public class People {
    private String uid;
    private String name;
    private String email;
    private Uri profilePicture;

    //private DatabaseReference mRef;
    //private StorageReference mStorageRef;

    public People(){
    }

    public People(String uid) {
        this.uid = uid;
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("users").child(uid);
        mRef.child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = dataSnapshot.getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        mRef.child("email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                email = dataSnapshot.getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference(uid);
        // Fetch profile picture
        try {
            final File localFile = File.createTempFile("images"+uid, "jpg");
            mStorageRef.child("images/upload.jpg").getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Successfully downloaded data to local file
                            profilePicture = Uri.fromFile(localFile);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    });
        } catch (IOException e) {
            Log.e("People " + uid, e.toString());
        }
    }


    /*public void setName(String name) {
        this.name = name;
        //mRef.child("name").setValue(name);
    }

    public void setEmail(String email) {
        this.email = email;
        //mRef.child("email").setValue(email);
    }

    public void setProfilePicture(final Uri profilePicture) {
        if (profilePicture != null) {
            mStorageRef.child("images/upload.jpg").putFile(profilePicture)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            People.this.profilePicture = profilePicture;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        }
    }*/

    public Uri getProfilePicture() { return profilePicture; }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() { return uid; }

    @Override
    public String toString() {
        return "People{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
