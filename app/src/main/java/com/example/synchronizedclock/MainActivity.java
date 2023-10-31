package com.example.synchronizedclock;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;

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
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private TextView textViewLabel; // Declare TextView for time label
    private TextView textViewClock; // Declare TextView for clock
    private Handler handler = new Handler(); // Initialize handler for ticking clock
    private SimpleDateFormat clock = new SimpleDateFormat("HH:mm:ss");  // Initialize clock to format the time values as strings in the "HH:mm:ss" format

    // Start the activity (sets up user interface and implements a ticking clock)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Initialize TextView components
        textViewLabel = findViewById(R.id.textViewLabel);
        textViewClock = findViewById(R.id.textViewClock);

        clock.setTimeZone(TimeZone.getTimeZone("Europe/Stockholm"));

        // Update the displayed time by scheduling a repeating task using a Handler
        // Calls the updateTimeBasedOnNetwork() method to update the displayed time every second (ticking clock)
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                updateTimeBasedOnNetwork();
            }
        }, 1000);
        super.onResume();
    }

    // Method for getting the System time
    private String getSystemTime() {
        return clock.format(new Date()); // Format the time as "HH:mm:ss"
    }

    // Method for getting the NTP time
    private String getNtpTime() {
        NTPUDPClient client = new NTPUDPClient(); // Create an instance of the NTPUDPClient class for a UDP (DatagramSocket) connection with NTP server
        client.setDefaultTimeout(3000); // Set a timeout to prevent the client from waiting indefinitely for the server to response

        try {
            InetAddress inetAddress = InetAddress.getByName("3.se.pool.ntp.org"); // Determine the IP-address of the NTP server
            TimeInfo timeInfo = client.getTime(inetAddress); // Request time information from the NTP server
            long ntpTime = timeInfo.getReturnTime(); // Retrieve the NTP time in milliseconds

            return clock.format(new Date(ntpTime)); // Return the NTP time (milliseconds) and format it into "HH:mm:ss"

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
        Network network = connectivityManager.getActiveNetwork();

        if (network != null) {
            return true; // There is an active network
        } else {
            return false; // No active network available
        }
    }

    // If network is available -> show NTP time, otherwise show System time
    private void updateTimeBasedOnNetwork() {
        if (isNetworkAvailable()) {
            // Create a new thread to perform network time retrieval
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String networkTime = getNtpTime();
                    updateUI(networkTime, "Network time", Color.BLUE);
                }
            }).start();

        } else { // Call SystemTime() function and show system time on display
            String systemTime = getSystemTime();
            updateUI(systemTime, "System time", Color.RED);
        }
    }

    // Android user interface (UI) operations must be executed on the main UI thread
    // so when the getNtpTime()-function is called on a separate thread in the updateTimeBasedOnNetwork()-function,
    // we need another thread (runOnUiThread) to switch back to the main UI thread for updating the UI components of network time
    // the updateUI-function implements this runOnUiThread
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