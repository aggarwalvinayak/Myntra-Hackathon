package c.hackathon.my_bong.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import c.hackathon.my_bong.Fragments.CartFragment;
import c.hackathon.my_bong.Fragments.CategoriesFragment;
import c.hackathon.my_bong.Fragments.ShopFragment;
import c.hackathon.my_bong.Fragments.TrendingFragment;
import c.hackathon.my_bong.R;
import c.hackathon.my_bong.adapters.Trending_grid_view_adapter;
import c.hackathon.my_bong.recyclerModels.Trending_grid_model;

import android.view.Menu;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private RecyclerView itemsRecyclerView;
    private List<Trending_grid_model> itemsList = new ArrayList<>();
    private Trending_grid_view_adapter itemsRecyclerAdapter;
    private int REQUEST_IMAGE_CAPTURE = 111;
    private Bitmap imageBitmap;
    private ByteArrayOutputStream baos;
    private byte[] compressedData;
    private StorageReference mountainsRef;
    private ProgressDialog pd;
    private GoogleApiClient googleApiClient;
    String id = "936643491249-368qr2lds62ads7v0hh5a16t9sc9me3s.apps.googleusercontent.com";
    private GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference mdatabase;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        itemsRecyclerView = findViewById(R.id.itemsRecyclerView);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://my-bong.appspot.com");
        mountainsRef = storageRef.child("user_uploaded").child("img");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Objects.requireNonNull(getSupportActionBar()).setTitle("My-Bong");
        mdatabase = FirebaseDatabase.getInstance().getReference();


        pd = new ProgressDialog(this);
        pd.setMessage("loading");
        pd.setCancelable(false);

        toolbar.setTitle("My-Bong");
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPictureDialog();
//                LaunchCamera();
            }
        });
        toolbar.setTitleTextColor(R.color.colorPrimaryDark);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorAccent));


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();



        transaction.replace(R.id.content, new TrendingFragment()).addToBackStack("tag").commit();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Trending_grid_model items = new Trending_grid_model();
        items.setName("Brand xyz");
        items.setPrice("Rs 599");
        itemsList.add(items);
        itemsList.add(items);
        itemsList.add(items);
        itemsList.add(items);
        itemsList.add(items);
        itemsList.add(items);
        itemsList.add(items);


        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)//you can also use R.string.default_web_client_id
                .requestIdToken(id)
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();




        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_men) {

            transaction.replace(R.id.content, new ShopFragment()).addToBackStack("tag").commit();

            // Handle the camera action
        }else if (id == R.id.nav_share) {

            String shareBody = "Hi, I am inviting you to vote for the election using VotZure which makes the voting extremely secure using 3FA and blockchain services";
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "VotZure");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

        }else if(id == R.id.nav_trending){
            transaction.replace(R.id.content, new TrendingFragment()).addToBackStack("tag").commit();
        }else if(id == R.id.nav_logout){
            signOut();
        }else if(id == R.id.nav_cart){
            transaction.replace(R.id.content, new CartFragment()).addToBackStack("tag").commit();
        }else if(id == R.id.nav_kids){
            transaction.replace(R.id.content, new CategoriesFragment()).addToBackStack("tag").commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            signOut();
        }

        return super.onOptionsItemSelected(item);
    }


    private void showPictureDialog(){

//        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }


    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent,0);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }




    private void UploadToFirebase() {
        pd.show();


        mountainsRef.putBytes(compressedData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mountainsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        pd.dismiss();
                        final Uri downloadUrl = uri;
                        mdatabase.child("user_uploaded").setValue(downloadUrl.toString());
                        Toast.makeText(HomeActivity.this, "Pic Successfully uploaded,Please wait some time to get your styles generated", Toast.LENGTH_SHORT).show();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.content, new CategoriesFragment()).addToBackStack("tag").commit();
                    }
                });
            }
        });


//        UploadTask uploadTask = mountainsRef.putBytes(compressedData);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                pd.dismiss();
//                Toast.makeText(HomeActivity.this, "Failed to Upload Picture. Try Again Later", Toast.LENGTH_SHORT).show();// Handle unsuccessful uploads
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
//                Toast.makeText(HomeActivity.this, downloadUrl.toString(), Toast.LENGTH_SHORT).show();
////                mdatabase.child("user_uploaded").setValue(downloadUrl);
//                Toast.makeText(HomeActivity.this, "Pic Successfully uploaded,Please wait some time to get your styles generated", Toast.LENGTH_SHORT).show();
//
//            }
//        });



    }


    private void LaunchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }



    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                });
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == this.RESULT_OK) {
//            Bundle extras = data.getExtras();
//            imageBitmap = (Bitmap) extras.get("data");
//            baos = new ByteArrayOutputStream();
//            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            compressedData = baos.toByteArray();
//            UploadToFirebase();
//        }
//    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);



//        Bundle extras = imageReturnedIntent.getExtras();
//        imageBitmap = (Bitmap) extras.get("data");




        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){

                    Uri selectedImage = imageReturnedIntent.getData();

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        compressedData = baos.toByteArray();
                        UploadToFirebase();
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Bundle extras = imageReturnedIntent.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                    baos = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    compressedData = baos.toByteArray();
                    UploadToFirebase();
                }
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
