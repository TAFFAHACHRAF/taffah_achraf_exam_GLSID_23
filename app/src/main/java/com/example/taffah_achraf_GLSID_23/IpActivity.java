package com.example.taffah_achraf_GLSID_23;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ajincodew.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IpActivity extends AppCompatActivity {

    private EditText ipEditText;
    private Button searchButton;
    private MapView mapView;
    private GoogleMap googleMap;
    private RecyclerView infoRecyclerView;
    private InfoAdapter infoAdapter;
    private List<InfoItem> infoItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);

        ipEditText = findViewById(R.id.ipEditText);
        searchButton = findViewById(R.id.searchButton);
        mapView = findViewById(R.id.mapView);
        infoRecyclerView = findViewById(R.id.infoRecyclerView);

        // Initialize RecyclerView
        infoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        infoItems = new ArrayList<>();
        infoAdapter = new InfoAdapter(infoItems);
        infoRecyclerView.setAdapter(infoAdapter);

        // Initialize MapView
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;
            }
        });

        // Search button click listener
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipAddress = ipEditText.getText().toString().trim();
                if (!ipAddress.isEmpty()) {
                    fetchLocation(ipAddress);
                } else {
                    Toast.makeText(IpActivity.this, "Please enter an IP address", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Retrieve login from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        String password = sharedPreferences.getString("password", "");
        String welcomeMessage = "Welcome, " + email;

        // Set welcome message on TextView
        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        welcomeTextView.setText(welcomeMessage);
    }

    private void fetchLocation(String ipAddress) {
        String url = "https://ipinfo.io/" + ipAddress + "/geo";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String ip = response.getString("ip");
                            String city = response.getString("city");
                            String region = response.getString("region");
                            String country = response.getString("country");


                            infoItems.clear();
                            infoItems.add(new InfoItem("Ip", ip));
                            infoItems.add(new InfoItem("City", city));
                            infoItems.add(new InfoItem("Region", region));
                            infoItems.add(new InfoItem("Country", country));
                            infoAdapter.notifyDataSetChanged();

                            String location = response.getString("loc");
                            String[] latLng = location.split(",");
                            double latitude = Double.parseDouble(latLng[0]);
                            double longitude = Double.parseDouble(latLng[1]);
                            displayLocation(latitude, longitude);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(IpActivity.this, "Error retrieving location", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(IpActivity.this, "Error retrieving location", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(this).add(request);
    }

    private void displayLocation(double latitude, double longitude) {
        googleMap.clear();
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    static class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.InfoViewHolder> {

        private List<InfoItem> infoItems;

        public InfoAdapter(List<InfoItem> infoItems) {
            this.infoItems = infoItems;
        }

        @NonNull
        @Override
        public InfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info, parent, false);
            return new InfoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull InfoViewHolder holder, int position) {
            InfoItem infoItem = infoItems.get(position);
            holder.tvTitle.setText(infoItem.getTitle());
            holder.tvValue.setText(infoItem.getValue());
        }

        @Override
        public int getItemCount() {
            return infoItems.size();
        }

        static class InfoViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle;
            TextView tvValue;

            public InfoViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvTitle);
                tvValue = itemView.findViewById(R.id.tvValue);
            }
        }
    }

    static class InfoItem {
        private String title;
        private String value;

        public InfoItem(String title, String value) {
            this.title = title;
            this.value = value;
        }

        public String getTitle() {
            return title;
        }

        public String getValue() {
            return value;
        }

        public void setTitle(String title){
            this.title=title;
        }

        public void setValue(String value){
            this.value=value;
        }
    }
}
