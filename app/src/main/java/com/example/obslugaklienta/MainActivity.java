package com.example.obslugaklienta;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;



import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;


import com.example.obslugaklienta.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity implements ValueEventListener {

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth firebaseAuth;
    private MediaPlayer orderAlert;

    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    public boolean firstRunningNotification;
    private int size1;
    private NavigationView navigationView;
    private String name;
    private FirebaseDatabase firebaseDatabase;
    private String type;


    public boolean isFirstRunningNotification() {
        return firstRunningNotification;
    }

    private static final String CHANNEL_ID = "1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        firstRunningNotification = true;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference().child("Orders").addValueEventListener(this);




         builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_notification_small)
                .setContentTitle("Masz nowe zamówienie!!!")
                .setContentText("Odbierz mnie :)")
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.icon_notification_big))

                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setPriority(NotificationCompat.PRIORITY_HIGH);



        Intent notifyIntent = new Intent(this, MainActivity.class);
// Set the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
// Create the PendingIntent
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                this, 0, notifyIntent, PendingIntent.FLAG_IMMUTABLE
        );

         builder.setContentIntent(notifyPendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.CHANNEL_NEWS);
            String description = getString(R.string.CHANNEL_DESCRIPTION);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }














        com.example.obslugaklienta.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
         navigationView = binding.navView;










        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_started, R.id.nav_finished, R.id.nav_reports, R.id.nav_warehouse, R.id.nav_complaint_not_finished, R.id.nav_complaint, R.id.nav_driver,R.id.nav_services)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.userTextInfo);
        TextView employeeText = headerView.findViewById(R.id.employee);
    
                if (firebaseAuth.getCurrentUser() != null) {

                    String firebaseEmail = firebaseAuth.getCurrentUser().getEmail();
                    String beforeEmail = StringUtils.substringBefore(firebaseEmail, "@");
                    String afterEmail = StringUtils.substringAfter(firebaseEmail, "@");
                    name = StringUtils.substringBefore(firebaseEmail, ".");
                    type = StringUtils.substringBefore(afterEmail, ".");
                    String lastName = StringUtils.substringAfter(beforeEmail, ".");
                    String userName;
                    try {
                        userName = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase() + " "
                                + lastName.substring(0,1).toUpperCase() + lastName.substring(1).toLowerCase();
                    } catch (Exception e)
                    {
                        userName = String.valueOf(R.string.placeholder);

                    }
                    if (type.equals("kierowca"))
                    {
                        String nameDriver = lastName.substring(0,1).toUpperCase() + lastName.substring(1).toLowerCase();
                        navUsername.setText(nameDriver);
                        employeeText.setText(R.string.driver);
                    }
                    else {
                        navUsername.setText(userName);
                    }


                }
                else
                {
                    GoToLogin();
                }











    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull String name, @NonNull @NotNull Context context, @NonNull @NotNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    private void GoToLogin() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public void onResume( ) {
        super.onResume();

        firstRunningNotification = true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_logout) {

            Toast toast = Toast.makeText(MainActivity.this, "Wylogowano", Toast.LENGTH_SHORT);
            toast.show();


            firebaseAuth.signOut();
            GoToLogin();


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {


        if(firebaseAuth.getCurrentUser()!=null)
        {
            String firebaseEmail = firebaseAuth.getCurrentUser().getEmail();
            String afterEmail = StringUtils.substringAfter(firebaseEmail, "@");
            type = StringUtils.substringBefore(afterEmail, ".");
            Menu menu = navigationView.getMenu();
            if (type.equals("kierowca"))
            {
                menu.performIdentifierAction(R.id.nav_driver, 0);
                firebaseDatabase.getReference().child("Orders").removeEventListener(this);
            }
        }



        int size = (int) snapshot.getChildrenCount();

        if (!firstRunningNotification)
        {
            if (size1< size)
            {
                if (orderAlert==null)
                {
                    orderAlert = MediaPlayer.create(this, R.raw.new_order_alert);
                }

                notificationManager.notify(1, builder.build());
                orderAlert.start();
            }
            size1 = size;

        }
        else
        {
            size1 = size;
          firstRunningNotification=false;
        }




    }

    @Override
    public void onCancelled(@NonNull @NotNull DatabaseError error) {

    }

    @Override
    public void onBackPressed() {
        int i =1;
        if (i ==2)
        {
            super.onBackPressed();
        }
    }
}
