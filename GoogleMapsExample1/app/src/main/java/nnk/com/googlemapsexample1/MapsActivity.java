package nnk.com.googlemapsexample1;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback
{

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        // Add a marker in Ameerpet and move the camera
        LatLng sathyatech = new LatLng(12.9106730,77.6235410);
        mMap.addMarker(new MarkerOptions().position(sathyatech).title("Sri Sai Dhama"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sathyatech));
    }

    public void showHybrid(View v)
    {
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    public void showSatellite(View v)
    {
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    public void showTerrain(View v)
    {
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }

    public void shownext(View v)
    {
        startActivity(new Intent(MapsActivity.this,NextActivity.class));
    }
}
