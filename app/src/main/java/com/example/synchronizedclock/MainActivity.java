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
    private TextView textViewLabel; // Declare TextView for time label
    private TextView textViewClock; // Declare TextView for clock
    private Handler handler = new Handler(); // Initialize handler for ticking clock
    private SimpleDateFormat clock = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());  // Initialize clock to format the time values as strings in the "HH:mm:ss" format

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Initialize TimeView components
        textViewLabel = findViewById(R.id.textViewLabel);
        textViewClock = findViewById(R.id.textViewClock);

        // Method to update the displayed time every second. Creates a time-based loop with handler (ticking clock)
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                updateTimeBasedOnNetwork(); // Calls
            }
        }, 1000);
        super.onResume();
    }

    // Method for getting System time
    private String SystemTime() {
        return clock.format(new Date()); // Format the time as "HH:mm:ss"
    }

    // Method for getting the NTP time
    private String NtpTime() {
        NTPUDPClient client = new NTPUDPClient(); // Create an instance of the NTPUDPClient class for a UDP implementation of a client for the Network Time Protocol (NTP)
        client.setDefaultTimeout(3000); // Set a timeout to prevent the client from waiting indefinitely for the server to response

        try {
            InetAddress inetAddress = InetAddress.getByName("3.se.pool.ntp.org"); // Determine the IP-address of NTP server
            TimeInfo timeInfo = client.getTime(inetAddress); // Retrieve time from the NTP server
            long ntpTime = timeInfo.getMessage().getTransmitTimeStamp().getTime(); // Get NTP time in milliseconds

            return clock.format(new Date(ntpTime)); // Format the NTP time into a date-time string

        // Handle exeptions and close the DatagramSocket
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        } finally {
            client.close();
        }
    }

    // Check if internet connection is available. Return true or false
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }

    // If internet is available -> show NTP time, otherwise show System time
    private void updateTimeBasedOnNetwork() {
        if (isNetworkAvailable()) {
            // Create a thread to perform network time retrieval
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String networkTime = NtpTime();
                    updateUI(networkTime, "Network time", Color.BLUE);
                }
            }).start();

        } else { // Call SystemTime() function and show system time on display
            String systemTime = SystemTime();
            updateUI(systemTime, "System time", Color.RED);
        }
    }

    private void updateUI(String time, String label, int labelColor) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewClock.setText(time);
                textViewLabel.setText(label);
                textViewLabel.setTextColor(labelColor);
            }
        });
    }
}