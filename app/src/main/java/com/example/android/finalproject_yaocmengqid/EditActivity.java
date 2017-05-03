package com.example.android.finalproject_yaocmengqid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.android.finalproject_yaocmengqid.Utils.CircularTransform;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
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
    private DatabaseReference mDatabaseRef;
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
                    /*user = new People(userAuth.getUid());
                    ((EditText)findViewById(R.id.editText_username)).setText(user.getName());
                    Picasso.with(EditActivity.this).load(user.getProfilePicture())
                            .resize(imageView.getWidth(),imageView.getHeight())
                            .centerInside()
                            .into(imageView);*/

                    mStorageRef = FirebaseStorage.getInstance().getReference(userAuth.getUid());
                    //((EditText)findViewById(R.id.editText_username)).setText(user.getDisplayName());
                    // Fetch profile picture
                    try {
                        final File localFile = File.createTempFile("images", "jpg");
                        mStorageRef.child("images/upload.jpg").getFile(localFile)
                                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        // Successfully downloaded data to local file
                                        Picasso.with(EditActivity.this).load(localFile)
                                                .resize(imageView.getWidth(),imageView.getHeight())
                                                .centerInside()
                                                .into(imageView);
                                        // ...
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Get display name
                    mDatabaseRef = FirebaseDatabase.getInstance().getReference("users").child(userAuth.getUid());
                    mDatabaseRef.child("name").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String value = dataSnapshot.getValue(String.class);
                            ((EditText)findViewById(R.id.editText_username)).setText(value);
                            Log.d(TAG, "User name is: " + value);
                            mDatabaseRef.child("name").removeEventListener(this);
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

        mDatabaseRef.child("name").setValue(newName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EditActivity.this, "Update profile successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditActivity.this, "Update profile failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                    Toast.makeText(EditActivity.this, "Profile picture upload successful!", Toast.LENGTH_SHORT).show();
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                }
            });
        }

        finish();
    }
}
