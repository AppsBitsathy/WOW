package in.bittechpro.apps.wow;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
//import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    Location lastLocation;
    public Marker currentUserLocationMarker;
    private static final int  Request_User_Location_code=99;

    private static final Object TAG ="tag" ;
    Context context;
    MapView mapView;
    GoogleMap googleMap;
    String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    View view;
    int col;
    boolean onetime = true;

    MarkerOptions markerOptions = new MarkerOptions();

    public MapFragment(){
        //require empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = view.findViewById(R.id.mapView);

        checkPermissions();

        mapView.onCreate(savedInstanceState);
        MapsInitializer.initialize(this.getActivity());
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);

        if(checkPermission()) {
            buildGoogleApiClient();
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        }



        /*LatLng coordinate1 = new LatLng(11.495916, 77.276485);
        googleMap.addMarker(new MarkerOptions().position(coordinate1).title("IT")).showInfoWindow();

        LatLng coordinate2 = new LatLng( 11.494961, 77.277013 );
        googleMap.addMarker(new MarkerOptions().position(coordinate2).title("pandal")).showInfoWindow();

        LatLng coordinate3 = new LatLng(11.495104, 77.276474);
        googleMap.addMarker(new MarkerOptions().position(coordinate3).title("LABS")).showInfoWindow();*/
    }

    private void getMachine() {

        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());

        final HashMap<String, String> params = new HashMap<>();
        params.put("u_id","ALL");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlManager.GET_COR_MACHINE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("VOLLEY1", response);
                        try {
                            JSONObject result = new JSONObject(response);
                            if(result.getInt("status")==0) {

                                int total = result.getInt("total");

                                if (total > 0) {

                                    String[] m_name = new String[total];
                                    String[] m_id = new String[total];
                                    float[] lati = new float[total];
                                    float[] longi = new float[total];
                                    int[] full_cap= new int[total];
                                    int[] cur_cap = new int[total];
                                    double[] capacity = new double[total];

                                    JSONArray res_m_name = result.getJSONArray("m_name");
                                    JSONArray res_m_id = result.getJSONArray("m_id");
                                    JSONArray res_lati = result.getJSONArray("lat");
                                    JSONArray res_longi = result.getJSONArray("long");
                                    JSONArray res_full_cap = result.getJSONArray("full_cap");
                                    JSONArray res_cur_cap = result.getJSONArray("cur_cap");


                                    for (int r = 0; r < total; r++) {

                                        m_name[r] = res_m_name.get(r).toString();
                                        m_id[r] = res_m_id.get(r).toString();
                                        lati[r] = Float.parseFloat(res_lati.get(r).toString());
                                        longi[r] = Float.parseFloat(res_longi.get(r).toString());
                                        full_cap[r] = (int)res_full_cap.get(r);
                                        cur_cap[r] = (int)res_cur_cap.get(r);

                                        capacity[r]  = ((double) cur_cap[r] / (double)full_cap[r]) * 100;



                                        Float color = BitmapDescriptorFactory.HUE_GREEN;
                                        col = Color.GREEN;

                                        if(capacity[r]>74.9){
                                            color = BitmapDescriptorFactory.HUE_RED;
                                            col = Color.RED;

                                        }
                                        else if (capacity[r]>49.9){
                                            color = BitmapDescriptorFactory.HUE_BLUE;
                                            col = Color.BLUE;
                                        }

                                        col = Color.GRAY;

                                        lati[r] = (float) lastLocation.getLatitude();
                                        longi[r] = (float) lastLocation.getLongitude();

                                        //LatLng coordinate = new LatLng(lati[r],longi[r]);
                                        LatLng coordinate = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());

                                        markerOptions.position(coordinate).title(m_name[r]).snippet(String.valueOf(100-capacity[r])+"% FREE").icon(BitmapDescriptorFactory.defaultMarker(color));
                                        googleMap.addMarker(markerOptions);

                                        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                            @Override
                                            public View getInfoWindow(Marker arg0) {
                                                return null;
                                            }

                                            @Override
                                            public View getInfoContents(Marker marker) {

                                                LinearLayout info = new LinearLayout(context);
                                                info.setOrientation(LinearLayout.VERTICAL);

                                                TextView title = new TextView(context);
                                                title.setTextColor(Color.BLACK);
                                                title.setGravity(Gravity.CENTER);
                                                title.setTypeface(null, Typeface.BOLD);
                                                title.setText(marker.getTitle());

                                                TextView snippet = new TextView(context);
                                                snippet.setTextColor(col);
                                                snippet.setGravity(Gravity.CENTER);
                                                snippet.setText(marker.getSnippet());

                                                info.addView(title);
                                                info.addView(snippet);

                                                return info;
                                            }
                                        });

                                    }


                                    LatLng coordinate1 = new LatLng(lati[0]-0.01, longi[0]);
                                    googleMap.addMarker(new MarkerOptions().position(coordinate1).title("DEMO 1").snippet("20% FREE").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                                    LatLng coordinate2 = new LatLng( lati[0], longi[0]-0.01 );
                                    googleMap.addMarker(new MarkerOptions().position(coordinate2).title("DEMO 2").snippet("80% FREE").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                                    LatLng coordinate3 = new LatLng(lati[0]-0.01, longi[0]-0.01);
                                    googleMap.addMarker(new MarkerOptions().position(coordinate3).title("DEMO 3").snippet("70% FREE").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                                    LatLng coordinate4 = new LatLng(lati[0]+0.01, longi[0]);
                                    googleMap.addMarker(new MarkerOptions().position(coordinate4).title("DEMO 4").snippet("10% FREE").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                                    LatLng coordinate5 = new LatLng( lati[0], longi[0]+0.01 );
                                    googleMap.addMarker(new MarkerOptions().position(coordinate5).title("DEMO 5").snippet("80% FREE").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                                    LatLng coordinate6 = new LatLng(lati[0]+0.01, longi[0]+0.01);
                                    googleMap.addMarker(new MarkerOptions().position(coordinate6).title("DEMO 6").snippet("2% FREE").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Snackbar.make(view, "Unknown error occured, Please try again", Snackbar.LENGTH_LONG);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY2", error.toString());
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    Log.d("VOLLEY3", "getBody: ");
                    RequestHandler requestHandler = new RequestHandler();
                    return requestHandler.sendPostRequest(params).getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    Log.d("VOLLEY4", "getwef: ");
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",params, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getActivity(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions( getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }

        return true;
    }

    private boolean checkPermission(){

        return (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case Request_User_Location_code :
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        googleMap.setMyLocationEnabled(true);
                        googleMap.getUiSettings().setMyLocationButtonEnabled(true);


                    }
                }
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest=new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION )== PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);

            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        }

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

            @Override
            public void onResult(@NonNull LocationSettingsResult result) {

                final Status status = result.getStatus();

                switch (status.getStatusCode()) {

                    case LocationSettingsStatusCodes.SUCCESS:

                        Log.i(String.valueOf(TAG), "All location settings are satisfied.");

                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        Log.i((String) TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {

                            status.startResolutionForResult(getActivity(), 0x1);
                        }
                        catch (IntentSender.SendIntentException e) {

                            Log.i((String) TAG, "PendingIntent unable to execute request.");
                        }

                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i((String) TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });

    }

    @Override
    public void onConnectionSuspended(int i) {
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
    }


    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        googleMap.clear();

        getMachine();
        if(currentUserLocationMarker != null){
            currentUserLocationMarker.remove();
        }
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());


        //MarkerOptions markerOptions = new MarkerOptions();
        /*markerOptions.position(latLng);
        markerOptions.title("TEST_MACHINE");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        currentUserLocationMarker = googleMap.addMarker(markerOptions);
        currentUserLocationMarker.showInfoWindow();*/

        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng, 11);
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

        googleMap.moveCamera(yourLocation);
        // googleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
        googleMap.animateCamera(zoom);

        // googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //  googleMap.animateCamera(CameraUpdateFactory.zoomBy(11));
        if(googleApiClient!= null) {

            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
        }





    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}


