package com.example.obslugaklienta.ui.warehouse;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.obslugaklienta.MainActivity;

import com.example.obslugaklienta.ObjectsAndAdapters.CaptionedImagesAdapterWarenhouse;

import com.example.obslugaklienta.ObjectsAndAdapters.Products;
import com.example.obslugaklienta.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.StringUtils;


import java.util.ArrayList;


public class WarehouseFragment extends Fragment implements ValueEventListener {



    FirebaseDatabase firebaseDatabase;
    boolean[] availabilityTable;
    int[] idTable;
    String[] nameTable;
    String[] nameENGTable;
    String[] descriptioENGTable;
    Double[] priceTable;
    String[] descriptionTable;
    String[] typeTable;
    int[] availabilityTableString;
    private boolean availabilityBolean;
    private String name;
    private double price;
    private int idProduct;
    private String description;
    private String type;
    private String descitpionENG;
    private String nameENG;


    @SuppressLint("StaticFieldLeak")
    public static ProgressBar progressBar;



    private RecyclerView mainActivityRecyclerView;


    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseDatabase = FirebaseDatabase.getInstance();

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
        View layout = inflater.inflate(R.layout.fragment_warehouse, container, false);

        mainActivityRecyclerView = layout.findViewById(R.id.Fragment_warenhouse_recycler);


        firebaseDatabase = FirebaseDatabase.getInstance();

        progressBar = layout.findViewById(R.id.ProgressBar);


        return layout;
    }



    @Override
    public void onPause() {
        super.onPause();
        firebaseDatabase.getReference().child("Products").orderByChild("numberOfList").removeEventListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        firebaseDatabase.getReference().child("Products").orderByChild("numberOfList").addValueEventListener(this);

    }
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        int size = (int) dataSnapshot.getChildrenCount();

        ArrayList<Boolean> cityList = new ArrayList<>();
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
            Products products = postSnapshot.getValue(Products.class);
            assert products != null;
            cityList.add(products.availability);
        }

        ArrayList<Integer> cityList2 = new ArrayList<>();
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
            Products products = postSnapshot.getValue(Products.class);
            assert products != null;
            cityList2.add(products.id);
        }
        ArrayList<String> cityList3 = new ArrayList<>();
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
            Products products = postSnapshot.getValue(Products.class);
            assert products != null;
            cityList3.add(products.name);
        }

        ArrayList<Double> cityList4 = new ArrayList<>();
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
            Products products = postSnapshot.getValue(Products.class);
            assert products != null;
            cityList4.add(products.price);
        }

        ArrayList<String> cityList5 = new ArrayList<>();
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
            Products products = postSnapshot.getValue(Products.class);
            assert products != null;
            cityList5.add(products.description);
        }

        ArrayList<String> cityList6 = new ArrayList<>();
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
            Products products = postSnapshot.getValue(Products.class);
            assert products != null;
            cityList6.add(products.type);
        }

        ArrayList<String> cityList7 = new ArrayList<>();
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
            Products products = postSnapshot.getValue(Products.class);
            assert products != null;
            cityList7.add(products.nameENG);
        }

        ArrayList<String> cityList8 = new ArrayList<>();
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
            Products products = postSnapshot.getValue(Products.class);
            assert products != null;
            cityList8.add(products.descriptionENG);
        }

        availabilityTable = new boolean[size];
        for (int i = 0; i < size; i++)
        {
            availabilityTable[i] = cityList.get(i);
        }

        idTable = new int[size];
        for (int i = 0; i < size; i++)
        {
            idTable[i] = cityList2.get(i);
        }

        nameTable = new String[size];
        for (int i = 0; i < size; i++)
        {
            nameTable[i] = cityList3.get(i);
        }

        nameENGTable = new String[size];
        for (int i = 0; i < size; i++)
        {
            nameENGTable[i] = cityList7.get(i);
        }

        descriptioENGTable = new String[size];
        for (int i = 0; i < size; i++)
        {
            descriptioENGTable[i] = cityList8.get(i);
        }

        priceTable = new Double[size];
        for (int i = 0; i < size; i++)
        {
            priceTable[i] = cityList4.get(i);
        }

        descriptionTable = new String[size];
        for (int i = 0; i < size; i++)
        {
            descriptionTable[i] = cityList5.get(i);
        }

        typeTable = new String[size];
        for (int i = 0; i < size; i++)
        {
            typeTable[i] = cityList6.get(i);
        }
        availabilityTableString = new int[size];

        for (int i = 0; i < size; i++)
        {
            boolean check;
            check = availabilityTable[i];
            if (check)
            {
                availabilityTableString[i] = R.drawable.avainaible;
            }
            else
            {
                availabilityTableString[i] = R.drawable.unavainaible;

            }
        }

        CaptionedImagesAdapterWarenhouse adapter = new CaptionedImagesAdapterWarenhouse(nameTable, availabilityTableString);
        mainActivityRecyclerView.setAdapter(adapter);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mainActivityRecyclerView.setLayoutManager(layoutManager);
        adapter.setListener(position -> {


            availabilityBolean =availabilityTable[position];
            price = priceTable[position];
            name = nameTable[position];
            idProduct = idTable[position];
            description = descriptionTable[position];
            type = typeTable[position];
            descitpionENG = descriptioENGTable[position];
            nameENG = nameENGTable[position];

            boolean fail = false;
            try {


                availabilityBolean = !availabilityBolean;
                SaveInFirebase(idProduct, name, availabilityBolean, price, description);
            }
            catch (Exception e)
            {
                fail = true;
                Toast.makeText(getContext(), "Błąd z bazą danych", Toast.LENGTH_SHORT).show();

            }
            finally {
                if (!fail)
                {
                    Toast.makeText(getContext(), "Zmieniono wartość", Toast.LENGTH_SHORT).show();
                }

            }


        });


    progressBar.setVisibility(View.GONE);


    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }


    private void Backpresses() {
        requireActivity().finish();
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }

    private void SaveInFirebase(int idProduct, String nameString, boolean availabilityBoleanFirebase, double priceFirebase, String description)
    {


            String idString = String.valueOf(idProduct);
            Products products = new Products(idProduct, nameString, availabilityBoleanFirebase, priceFirebase, description, type, nameENG, descitpionENG);
            firebaseDatabase.getReference().child("Products").child(idString).setValue(products);



    }

}