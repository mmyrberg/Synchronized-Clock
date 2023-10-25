package com.example.synchronizedclock;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.synchronizedclock.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.IOException;
import java.net.InetAddress;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private TextView textViewLabel;
    private TextView textViewTime;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Initialize TextView elements in app layout.
        textViewLabel = findViewById(R.id.textViewLabel); // Initialize the TextViews here
        textViewTime = findViewById(R.id.textViewTime);

        //updateTimeBasedOnNetwork();

        // Method to update the displayed time every 1 second. Uses a Handler to create a time-based loop.
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                updateTimeBasedOnNetwork();
            }
        }, 1000);
        super.onResume();
/*
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTimeBasedOnNetwork();
                //NtpTime();
            }
        });*/
    }

    // Method for getting System time.
    private String SystemTime() {
        SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return date.format(new Date());
    }

    // Method for getting the NTP time.
    private String NtpTime() {
        NTPUDPClient client = new NTPUDPClient();
        client.setDefaultTimeout(3000);

        try {
            InetAddress inetAddress = InetAddress.getByName("3.se.pool.ntp.org");
            TimeInfo timeInfo = client.getTime(inetAddress);
            long ntpTimeMillis = timeInfo.getMessage().getTransmitTimeStamp().getTime();

            SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            String formattedNtpTime = date.format(new Date(ntpTimeMillis));

            return formattedNtpTime;
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        } finally {
            client.close();
        }
    }

    // Check if the internet connection is available. Returns true or false.
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }

    // If internet is available -> show NTP time, otherwise show System time.
    private void updateTimeBasedOnNetwork() {
        if (isNetworkAvailable()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String networkTime = NtpTime();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewTime.setText(networkTime);
                            textViewLabel.setText("Network time");
                            textViewLabel.setTextColor(Color.BLUE);
                        }
                    });
                }
            }).start();
        } else {
            String systemTime = SystemTime();
            textViewTime.setText(systemTime);
            textViewLabel.setText("System time");
            textViewLabel.setTextColor(Color.RED);
        }
    }
}
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}*/