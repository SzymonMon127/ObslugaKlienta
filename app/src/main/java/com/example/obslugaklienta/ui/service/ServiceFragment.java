package com.example.obslugaklienta.ui.service;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.obslugaklienta.R;


public class ServiceFragment extends Fragment {




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View layout = inflater.inflate(R.layout.fragment_services, container, false);

        TextView phoneNumber = layout.findViewById(R.id.phone_number);
        TextView textView = layout.findViewById(R.id.email_adres);
        ImageButton buttonPhone  = layout.findViewById(R.id.phone_button);
        ImageButton buttonEmail = layout.findViewById(R.id.email_button);

        buttonEmail.setOnClickListener(v -> {

            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{String.valueOf(textView.getText())});
            email.putExtra(Intent.EXTRA_SUBJECT, "OrderManagmentApplicationContact");
            email.setType("message/rfc822");
            startActivity(Intent.createChooser(email, "Wybierz aplikacje pocztową :)"));
        });

        buttonPhone.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Pomyślnie usunięto", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            String parseString = "tel:"+phoneNumber.getText();
            intent.setData(Uri.parse(parseString));
            startActivity(intent);

        });

        return layout;
    }


}