package cbs.engelsiz_mugla;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;

    private GoogleMap mMap;
    public Button tButton;
    private Button vButton;
    public TextView konumText;
    private Tracker gps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        tButton = (Button) findViewById(R.id.btnT);
        tButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                konumText = (TextView) findViewById(R.id.text_konum);
                gps = new Tracker(MapsActivity.this);
                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    konumText.setText("Lat: " + latitude + "\nLong: " + longitude);

                    //                Toast.makeText(
                    //                        getApplicationContext(),
                    //                        "Your Location is -\nLat: " + latitude + "\nLong: "
                    //                                + longitude, Toast.LENGTH_LONG).show();

                } else {
                    gps.showSettingsAlert();
                }


                Intent iTmm = new Intent(MapsActivity.this, MainActivity.class);
                startActivity(iTmm);
            }
        });
        vButton = (Button) findViewById(R.id.bntV);
        vButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iVzgc = new Intent(MapsActivity.this, MainActivity.class);
                startActivity(iVzgc);
            }
        });
    }





    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        LatLng cbskonum = new LatLng(37.175781, 28.373504);
        mMap.addMarker(new MarkerOptions().position(cbskonum).title("Coğrafi Bilgi Sistemleri"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cbskonum, 18));
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setMyLocationEnabled(true);
     /**
        // Add a marker in Sydney and move the camera
        LatLng myLatLng = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
        mMap.addMarker(new MarkerOptions().position(myLatLng).title("Your are here now!!"));

*/
        mMap.setOnMyLocationChangeListener(myLocationChangeListener);

    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(loc));
            if(mMap != null){
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            }
        }
    };




}
