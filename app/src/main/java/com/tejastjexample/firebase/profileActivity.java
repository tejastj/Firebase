package com.tejastjexample.firebase;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class profileActivity extends AppCompatActivity implements View.OnClickListener{

    TextView profiletext;
    Button profilelogout,profilesave;
    EditText profilename,profileaddress;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    public String name;
    public String address;
    public static final int MY_PERMISSION_REQUEST_LOCATION=1;
    public final long  period = 20000;
    public Timer timer;
    public TextView myweb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profiletext = (TextView) findViewById(R.id.profiletext);
        profilelogout = (Button) findViewById(R.id.profilelogout);
        profilename = (EditText) findViewById(R.id.profilname);
        profileaddress = (EditText) findViewById(R.id.profileaddress);
        profilesave = (Button) findViewById(R.id.profilesave);
        myweb = (TextView)findViewById(R.id.website);



        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, loginActivity.class));
        }
        profilelogout.setOnClickListener(this);
        profilesave.setOnClickListener(this);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        profiletext.setText("Welcome " + user.getEmail());

        databaseReference = FirebaseDatabase.getInstance().getReference(user.getUid());

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }
        },0,period);
        myweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(profileActivity.this,website.class));
            }
        });

    }

    public void TimerMethod(){
        this.runOnUiThread(Timer_Tick);
    }
    public Runnable Timer_Tick = new Runnable() {
        @Override
        public void run() {
            if (ContextCompat.checkSelfPermission(profileActivity.this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(profileActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    ActivityCompat.requestPermissions(profileActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION);
                } else {
                    ActivityCompat.requestPermissions(profileActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION);
                }
            } else {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                try {
                    String place = hereLocation(location.getLatitude(), location.getLongitude());
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    UserLocation userLocation = new UserLocation(latitude, longitude, place, name, address);
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    databaseReference.child("Chethan").setValue(userLocation);
                    Toast.makeText(getApplicationContext(), "Information  Saved...", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "oops! Not Found", Toast.LENGTH_SHORT).show();
                }
            }

        }
    };
    private void getUserInformation(){
        name = profilename.getText().toString().trim();
        address = profileaddress.getText().toString().trim();
        UserInformation userInformation = new UserInformation(name,address);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference.child("Chethan").setValue(userInformation);
        Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_REQUEST_LOCATION:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(profileActivity.this,android.Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        LocationManager locationManager =(LocationManager)getSystemService(Context.LOCATION_SERVICE);
                        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        try{
                            String place = hereLocation(location.getLatitude(),location.getLongitude());
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            UserLocation userLocation = new UserLocation(latitude,longitude,place,name,address);
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            databaseReference.child(user.getUid()).setValue(userLocation);

                        }catch (Exception e ){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Opps! permission not graunted",Toast.LENGTH_SHORT).show();

                        }
                    }
                }
        }
    }




    public String hereLocation(double lat, double lon){
        String curCity="";

        Geocoder geocoder = new Geocoder(profileActivity.this, Locale.getDefault());
        List<Address> addressList;

        try{
            addressList =geocoder.getFromLocation(lat,lon,1);
            if(addressList.size()>0){
                curCity = addressList.get(0).getLocality();
            }


        }catch(Exception e){
            e.printStackTrace();
        }
        return curCity;
    }


    @Override
    public void onClick(View view) {
        if(view== profilelogout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
        if(view == profilesave){
            getUserInformation();
        }
    }
}
