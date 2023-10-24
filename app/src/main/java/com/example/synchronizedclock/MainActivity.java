package com.example.synchronizedclock;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
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
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Snackbar.make(view, "Här kommer tiden synas", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
                 */
                //Fältet som vi vill skriva till
                final TextView text1 = (TextView) findViewById(R.id.textview_first);
                text1.setTextSize(60);

                LocalTime localTime = LocalTime.now(); // Get the current system time
                int hours = localTime.getHour();
                int minutes = localTime.getMinute();
                int seconds = localTime.getSecond();

                // Format the time as "HH:MM:SS"
                String time = String.format("%02d:%02d:%02d", hours, minutes, seconds); // Format the time as "HH:MM"
                text1.setText(time);
            }
        });
    }

<<<<<<< Updated upstream
=======
    public void SystemTime() {
        TextView t = findViewById(R.id.textview_first);
        t.setTextSize(60);

        LocalTime localTime = LocalTime.now(); // Get the current system time
        int hours = localTime.getHour();
        int minutes = localTime.getMinute();
        int seconds = localTime.getSecond();

        // Format the time as "HH:MM:SS"
        String time = String.format("%02d:%02d:%02d", hours, minutes, seconds); // Format the time as "HH:MM:SS"
        t.setText(time);
    }

    //Method for getting the NTP time.
    public String NtpTime() {
        TextView t = findViewById(R.id.textView4);
        t.setTextSize(60);

        NTPUDPClient client = new NTPUDPClient();
        client.setDefaultTimeout(5000); // Set a timeout in milliseconds.

        try {
            TimeInfo timeInfo = client.getTime(InetAddress.getByName("pool.ntp.org")); // You can change the NTP server address.

            // Get the NTP time in milliseconds
            long ntpTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();

            return String.valueOf(ntpTime);
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Handle errors or exceptions as needed.
        } finally {
            client.close();
        }
    }


>>>>>>> Stashed changes
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
}