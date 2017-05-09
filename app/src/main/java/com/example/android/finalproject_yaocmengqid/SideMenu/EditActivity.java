package com.example.android.finalproject_yaocmengqid.SideMenu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.finalproject_yaocmengqid.R;
import com.example.android.finalproject_yaocmengqid.Utils.CircularTransform;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {

    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_PICK_PHOTO = 2;

    private ImageView imageView;
    private File photoFile;
    private StorageReference mStorageRef;
    private FirebaseUser userAuth;
    private DatabaseReference mUserRef;
    private DatabaseReference mPeopleRef;
    //private People user;
    private Uri fileToUpload;
    private String TAG = "EditActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        imageView = (ImageView) findViewById(R.id.preview);

        FirebaseAuth mAuth;
        FirebaseAuth.AuthStateListener mAuthListener;
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser userAuth = firebaseAuth.getCurrentUser();
                if (userAuth != null) {
                    mStorageRef = FirebaseStorage.getInstance().getReference(userAuth.getUid());
                    mUserRef = FirebaseDatabase.getInstance().getReference("users").child(userAuth.getUid());
                    FirebaseDatabase.getInstance().getReference("people").child(userAuth.getUid())
                            .orderByKey().limitToFirst(1).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            mPeopleRef = dataSnapshot.getRef();
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    // Display Picture
                    mUserRef.child("profilePhoto").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String url = dataSnapshot.getValue(String.class);
                            if (url != null) {
                                Picasso.with(EditActivity.this).load(Uri.parse(url))
                                        .resize(imageView.getWidth(), imageView.getHeight())
                                        .centerInside()
                                        .into(imageView);
                                Log.d(TAG, "Profile picture URL is: " + url);
                            }
                            mUserRef.child("profilePhoto").removeEventListener(this);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });
                    // Get display name
                    mUserRef.child("name").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String value = dataSnapshot.getValue(String.class);
                            ((EditText)findViewById(R.id.editText_username)).setText(value);
                            Log.d(TAG, "User name is: " + value);
                            mUserRef.child("name").removeEventListener(this);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });
                } else {
                    finish();
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void takePhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.finalproject_yaocmengqid",
                        photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    public void pickPhoto(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_PICK_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK)
            return;

        if (requestCode == REQUEST_TAKE_PHOTO) {
            fileToUpload = Uri.parse(photoFile.toURI().toString());
        }
        else if (requestCode == REQUEST_PICK_PHOTO) {
            fileToUpload = data.getData();
        }

        final Target target = new Target() {
            @Override
            public void onPrepareLoad(Drawable arg0) {
                return;
            }
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
                try {
                    File file = File.createTempFile("cropped", "png");
                    FileOutputStream ostream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, ostream);
                    ostream.close();
                    fileToUpload = Uri.fromFile(file);
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                }
            }
            @Override
            public void onBitmapFailed(Drawable arg0) {
                return;
            }
        };

        Picasso.with(this)
                .load(fileToUpload)
                .resize(imageView.getWidth(),imageView.getHeight())
                .transform(new CircularTransform())
                .centerInside().into(target);
        imageView.setTag(target);
        Picasso.with(this)
                .load(fileToUpload)
                .resize(imageView.getWidth(),imageView.getHeight())
                .transform(new CircularTransform())
                .centerInside()
                .into(imageView);
        //Log.d(TAG, fileToUpload.toString());
    }

    public void updateProfile(View view){
        String newName = ((EditText)findViewById(R.id.editText_username)).getText().toString();
        Log.d(TAG, "new name: " + newName);
/*        user.setName(newName);
        if (fileToUpload != null) {
            user.setProfilePicture(fileToUpload);
        }*/

        mUserRef.child("name").setValue(newName);
        mPeopleRef.child("name").setValue(newName);

        if (fileToUpload != null) {
            StorageReference riversRef = mStorageRef.child("images/upload.jpg");
            riversRef.putFile(fileToUpload)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(EditActivity.this, "Profile picture upload unsuccessful!", Toast.LENGTH_SHORT).show();
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(EditActivity.this, "Update profile successfully!", Toast.LENGTH_SHORT).show();
                            @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            mUserRef.child("profilePhoto").setValue(downloadUrl.toString());
                        }
                    });
        }

        finish();
    }
}
