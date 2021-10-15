package com.example.obslugaklienta.ui.finished;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.obslugaklienta.MainActivity;
import com.example.obslugaklienta.ObjectsAndAdapters.CaptionedImagesAdapter;
import com.example.obslugaklienta.ObjectsAndAdapters.Order;
import com.example.obslugaklienta.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class FinishedFragment extends Fragment implements ValueEventListener {


    private RecyclerView mainActivityRecyclerView;
    private FirebaseDatabase firebaseDatabase;
    private String hour1;
    private String Adress1;
    private String Order1;
    private String id1;
    private String PhoneNumber1;
    private int cost1;
    private String payment1;
    private String complaint;
    private String userNameClient;
    private  boolean fail;
    private int rewardPoints;

    @SuppressLint("StaticFieldLeak")
    public static ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseDatabase = FirebaseDatabase.getInstance();
        fail = false;

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {

            String firebaseEmail = firebaseAuth.getCurrentUser().getEmail();
            String afterEmail = StringUtils.substringAfter(firebaseEmail, "@");
            String  name = StringUtils.substringBefore(afterEmail, ".");

            if (name.equals("kierowca"))
            {
                Backpresses();
                Toast.makeText(getContext(), "Brak uprawnień", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_finished, container, false);

        mainActivityRecyclerView = layout.findViewById(R.id.Fragment_finished_recycler);


        firebaseDatabase = FirebaseDatabase.getInstance();

        progressBar = layout.findViewById(R.id.ProgressBar);






        return layout;
    }



    @Override
    public void onPause() {
        super.onPause();
        firebaseDatabase.getReference().child("Realized").orderByChild("hour").removeEventListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        fail = false;
        firebaseDatabase.getReference().child("Realized").orderByChild("hour").addValueEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        int size = (int) dataSnapshot.getChildrenCount();


        ArrayList<String> cityList = new ArrayList<>();

        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
            Order newOrder = postSnapshot.getValue(Order.class);
            assert newOrder != null;
            cityList.add(newOrder.hour);
        }

        ArrayList<String> cityList2 = new ArrayList<>();
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
            Order newOrder = postSnapshot.getValue(Order.class);
            assert newOrder != null;
            cityList2.add(newOrder.order);

        }

        ArrayList<String> cityList3 = new ArrayList<>();
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
            Order newOrder = postSnapshot.getValue(Order.class);
            assert newOrder != null;
            cityList3.add(newOrder.Adress);
        }

        ArrayList<String> cityList4 = new ArrayList<>();
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
            Order newOrder = postSnapshot.getValue(Order.class);
            assert newOrder != null;
            cityList4.add(newOrder.PhoneNumber);
        }

        ArrayList<String> cityList5 = new ArrayList<>();
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            Order newOrder = postSnapshot.getValue(Order.class);
            assert newOrder != null;
            cityList5.add(newOrder.id);
        }

        ArrayList<Integer> cityList6 = new ArrayList<>();
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            Order newOrder = postSnapshot.getValue(Order.class);
            assert newOrder != null;
            cityList6.add((int) newOrder.cost);
        }

        ArrayList<String> cityList7 = new ArrayList<>();
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            Order newOrder = postSnapshot.getValue(Order.class);
            assert newOrder != null;
            cityList7.add(newOrder.payment);
        }

        ArrayList<String> cityList8 = new ArrayList<>();
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            Order newOrder = postSnapshot.getValue(Order.class);
            assert newOrder != null;
            cityList8.add(newOrder.userNameClient);
        }

        ArrayList<Integer> cityList10 = new ArrayList<>();
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
            Order newOrder = postSnapshot.getValue(Order.class);
            assert newOrder != null;
            cityList10.add(newOrder.points);
        }

        final int[] pointsTable = new int[size];
        for (int i = 0; i < size; i++)
        {
            pointsTable[i] = cityList10.get(i);
        }

        final String[] hour = new String[size];
        for (int i = 0; i < size; i++)
        {
            hour[i] = cityList.get(i);
        }

        final String[] order = new String[size];

        for (int i = 0; i < size; i++)
        {
            order[i] = cityList2.get(i);
        }

        final String[] adress = new String[size];
        for (int i = 0; i < size; i++)
        {
            adress[i] = cityList3.get(i);
        }
        final String[] phoneNumber = new String[size];
        for (int i = 0; i < size; i++)
        {
            phoneNumber[i] = cityList4.get(i);
        }

        final String[] Id = new String[size];
        for (int i = 0; i < size; i++) {
            Id[i] = cityList5.get(i);
        }

        final String[] price = new String[size];
        for (int i = 0; i < size; i++) {
            price[i] = cityList6.get(i) + " PLN";
        }

        final int[] price1 = new int[size];
        for (int i = 0; i < size; i++) {
            price1[i] = cityList6.get(i);
        }

        final String[] payment = new String[size];
        for (int i = 0; i < size; i++) {
            payment[i] = cityList7.get(i);
        }

        final String[] userNameClientTable = new String[size];
        for (int i = 0; i < size; i++) {
            userNameClientTable[i] = cityList8.get(i);
        }

        final String[] pointsStringTAble = new String[size];
        {
            for (int i= 0; i < size; i++)
            {
                pointsStringTAble[i] = pointsTable[i] + " punktów";
            }
        }
        CaptionedImagesAdapter adapter = new CaptionedImagesAdapter(hour, order, adress, phoneNumber, price, payment, pointsStringTAble);
        mainActivityRecyclerView.setAdapter(adapter);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mainActivityRecyclerView.setLayoutManager(layoutManager);
        adapter.setListener(position -> {


            id1 = Id[position];
            hour1 = hour[position];
            Order1 = order[position];
            Adress1 = adress[position];
            PhoneNumber1 = phoneNumber[position];
            cost1 = price1[position];
            payment1 = payment[position];
            userNameClient = userNameClientTable[position];
            rewardPoints = pointsTable[position];

            showAlertDialogButtonClicked();


        });

        progressBar.setVisibility(View.GONE);


    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    public void showAlertDialogButtonClicked() {

        final EditText edittext = new EditText(getContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Reklamacja");
        builder.setMessage("Na pewno chcesz przenieść zamówienie z godziny:"+" " +hour1 +" do reklamacji? \n \n Powód reklamacji:");
        builder.setView(edittext);

        builder.setPositiveButton("TAK, CHCĘ", (dialog, which) -> {
            try {
                fail = false;
                complaint = String.valueOf(edittext.getText());
                if (complaint.equals(""))
                {
                    complaint = "brak";
                }
                Order newOrder1 = new Order(id1, hour1, Order1, Adress1, PhoneNumber1, cost1, payment1, complaint, userNameClient, rewardPoints);
                firebaseDatabase.getReference("NotFinishedComplaint").child(id1).setValue(newOrder1);

            }
            catch (Exception e)
            {
                fail = true;
                Toast.makeText(getContext(), "Błąd przenoszenia", Toast.LENGTH_SHORT).show();

            }
            finally {
                if (!fail)
                {
                    firebaseDatabase.getReference("Realized").child(id1).removeValue();
                    Toast.makeText(getContext(), "Pomyślnie przeniesiono", Toast.LENGTH_SHORT).show();
                }
            }


        });

        builder.setNeutralButton("USUWAM CAŁKOWICIE", (dialog, which) -> {
            try {
                fail = false;
                firebaseDatabase.getReference("Realized").child(id1).removeValue();
            }
            catch (Exception e)
            {
                fail = true;
                Toast.makeText(getContext(), "Błąd usuwania", Toast.LENGTH_SHORT).show();

            }
            finally {
                if (!fail)
                {
                    Toast.makeText(getContext(), "Pomyślnie usunięto", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("ANULUJ", (dialog, which) -> Toast.makeText(getContext(), "Anulowano", Toast.LENGTH_SHORT).show());





        AlertDialog dialog = builder.create();
        dialog.setIcon(R.drawable.icon);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.password_background);
        dialog.show();
    }

    private void Backpresses() {
        requireActivity().finish();
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }



}