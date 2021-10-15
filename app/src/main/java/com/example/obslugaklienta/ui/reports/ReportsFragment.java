package com.example.obslugaklienta.ui.reports;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;



import com.example.obslugaklienta.MainActivity;
import com.example.obslugaklienta.ObjectsAndAdapters.Common;
import com.example.obslugaklienta.ObjectsAndAdapters.Order;
import com.example.obslugaklienta.ObjectsAndAdapters.PdfDocuemntsAdapter;
import com.example.obslugaklienta.R;
import com.example.obslugaklienta.SignInActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ReportsFragment extends Fragment  {


    ImageButton create_pdf_button;


    private FirebaseDatabase firebaseDatabase;
    private String time;
    private int size;
    String HourPdf;
    private String OrderPdf;
    private String PhoneNumberPdf;
    private String AdressPdf;
    private double PricePdf;
    private String userNameStringPdf;
     String paymentPdf;
    String[] hour;
    String[] order;
    String[] adress;
    String[] phoneNumber;
    String[] price;
    String[] complaint;
    String[] payment;
    String[] userNameClient;
    int[] points;
    private  boolean fail;
    private String pointsString;
    private int pointsInt;
    private int TotalPoints;


    double[] soloPrice;
    private double TotalPrice;
    Context context;
    Activity activity;
    private FirebaseAuth firebaseAuth;


    private String userName;
    private ImageButton create_pdf_button_complaints;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        fail = false;
        View layout = inflater.inflate(R.layout.fragment_reports, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {

            getNameAndId();

            String firebaseEmail = firebaseAuth.getCurrentUser().getEmail();
            String afterEmail = StringUtils.substringAfter(firebaseEmail, "@");
            String  name = StringUtils.substringBefore(afterEmail, ".");

            if (name.equals("kierowca"))
            {
                Backpresses();
                Toast.makeText(getContext(), "Brak uprawnień", Toast.LENGTH_SHORT).show();

            }
            }

        else
        {
            goToLogin();

        }

        TotalPrice =0;
        TotalPoints=0;

        context = getContext();
        activity = getActivity();


        Date currentTime = Calendar.getInstance().getTime();

        time = String.valueOf(currentTime);

        firebaseDatabase = FirebaseDatabase.getInstance();

        create_pdf_button = layout.findViewById(R.id.create_pdf_button);
        create_pdf_button_complaints = layout.findViewById(R.id.create_pdf_button_comptain);
        ImageButton delete_realized_button = layout.findViewById(R.id.deleteRealizedButton);
        ImageButton delete_complaint_button = layout.findViewById(R.id.deleteComplaintButton);

        delete_realized_button.setOnClickListener(v -> {
            try {
                fail = false;
                firebaseDatabase.getReference("Realized").removeValue();
            }
            catch (Exception e)
            {
                fail = true;
                Toast.makeText(getContext(), "Błąd usuwania", Toast.LENGTH_SHORT).show();

            }
            finally {
                if (!fail)
                {
                    Toast.makeText(getContext(), "Pomyślnie skasowano", Toast.LENGTH_SHORT).show();
                }
            }

        });

        delete_realized_button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    v.setBackgroundResource(R.drawable.delete_realized_button_clicked);
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    v.setBackgroundResource(R.drawable.delete_realized_button);
                    break;
                }
            }
            return false;
        });
        delete_complaint_button.setOnClickListener(v -> {


            try {
                fail = false;
                firebaseDatabase.getReference("Complaint").removeValue();
            }
            catch (Exception e)
            {
                fail = true;
                Toast.makeText(getContext(), "Błąd usuwania", Toast.LENGTH_SHORT).show();

            }
            finally {
                if (!fail)
                {
                    Toast.makeText(getContext(), "Pomyślnie skasowano", Toast.LENGTH_SHORT).show();
                }
            }

        });
        delete_complaint_button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    v.setBackgroundResource(R.drawable.delete_complaint_button_clicked);
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    v.setBackgroundResource(R.drawable.delete_complaint_button);
                    break;
                }
            }
            return false;
        });

        create_pdf_button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    v.setBackgroundResource(R.drawable.pdf_button_clicked);
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    v.setBackgroundResource(R.drawable.pdf_button);
                    break;
                }
            }
            return false;
        });

        create_pdf_button_complaints.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    v.setBackgroundResource(R.drawable.pdf_button_clicked);
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    v.setBackgroundResource(R.drawable.pdf_button);
                    break;
                }
            }
            return false;
        });

        Dexter.withContext(context).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                create_pdf_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        create_pdf_button.setBackgroundResource(R.drawable.pdf_button_clicked);

                        firebaseDatabase.getReference().child("Realized").orderByChild("hour").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                size = (int) snapshot.getChildrenCount();


                                ArrayList<String> cityList = new ArrayList<>();

                                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                                    Order newOrder = postSnapshot.getValue(Order.class);
                                    assert newOrder != null;
                                    cityList.add(newOrder.hour);
                                }

                                ArrayList<String> cityList2 = new ArrayList<>();
                                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                                    Order newOrder = postSnapshot.getValue(Order.class);
                                    assert newOrder != null;
                                    cityList2.add(newOrder.order);

                                }

                                ArrayList<String> cityList3 = new ArrayList<>();
                                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                                    Order newOrder = postSnapshot.getValue(Order.class);
                                    assert newOrder != null;
                                    cityList3.add(newOrder.Adress);
                                }

                                ArrayList<String> cityList4 = new ArrayList<>();
                                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                                    Order newOrder = postSnapshot.getValue(Order.class);
                                    assert newOrder != null;
                                    cityList4.add(newOrder.PhoneNumber);
                                }

                                ArrayList<Integer> cityList5 = new ArrayList<>();
                                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                                    Order newOrder = postSnapshot.getValue(Order.class);
                                    assert newOrder != null;
                                    cityList5.add((int) newOrder.cost);
                                }

                                ArrayList<String> cityList6 = new ArrayList<>();
                                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                                    Order newOrder = postSnapshot.getValue(Order.class);
                                    assert newOrder != null;
                                    cityList6.add(newOrder.payment);
                                }
                                ArrayList<String> cityList7 = new ArrayList<>();
                                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                                    Order newOrder = postSnapshot.getValue(Order.class);
                                    assert newOrder != null;
                                    cityList7.add(newOrder.userNameClient);
                                }

                                ArrayList<Integer> cityList8 = new ArrayList<>();
                                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                                    Order newOrder = postSnapshot.getValue(Order.class);
                                    assert newOrder != null;
                                    cityList8.add(newOrder.points);
                                }


                                hour = new String[size];
                                order = new String[size];
                                adress = new String[size];
                                phoneNumber = new String[size];
                                price = new String[size];
                                soloPrice = new double[size];
                                payment = new String[size];
                                userNameClient = new String[size];
                                points = new int[size];

                                for ( int i=0; i < size; i++)
                                {
                                    points[i] = cityList8.get(i);
                                }

                                for ( int i=0; i < size; i++)
                                {
                                    hour[i] = cityList.get(i);
                                }



                                for ( int i=0; i < size; i++)
                                {
                                    order[i] = cityList2.get(i);
                                }


                                for ( int i=0; i < size; i++)
                                {
                                    adress[i] = cityList3.get(i);
                                }

                                for ( int i=0; i < size; i++)
                                {
                                    phoneNumber[i] = cityList4.get(i);
                                }

                                for ( int i=0; i < size; i++)
                                {
                                    price[i] = cityList5.get(i) + " PLN";
                                }

                                for( int i =0; i < size; i++)
                                {
                                    soloPrice[i] = cityList5.get(i);
                                }

                                for (int i=0; i < size; i++)
                                {
                                    TotalPrice = TotalPrice+soloPrice[i];
                                    TotalPoints = TotalPoints+points[i];
                                }

                                for (int i=0; i < size; i++)
                                {
                                    payment[i] = cityList6.get(i);
                                }

                                for (int i=0; i < size; i++)
                                {
                                    userNameClient[i] = cityList7.get(i);
                                }


                                createPDFFILE(Common.getAppPath(context)+"test_pdf.pdf");
                                firebaseDatabase.getReference("Realized").orderByChild("hour").removeEventListener(this);
                            }


                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });
                        create_pdf_button.setBackgroundResource(R.drawable.pdf_button);
                    }
                });
                create_pdf_button_complaints.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        create_pdf_button_complaints.setBackgroundResource(R.drawable.pdf_button_clicked);
                        firebaseDatabase.getReference().child("Complaint").orderByChild("hour").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                size = (int) snapshot.getChildrenCount();




                                ArrayList<String> cityList = new ArrayList<>();
                                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                                    Order newOrder = postSnapshot.getValue(Order.class);
                                    assert newOrder != null;
                                    cityList.add(newOrder.hour);
                                }

                                ArrayList<String> cityList2 = new ArrayList<>();
                                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                                    Order newOrder = postSnapshot.getValue(Order.class);
                                    assert newOrder != null;
                                    cityList2.add(newOrder.order);
                                }

                                ArrayList<String> cityList3 = new ArrayList<>();
                                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                                    Order newOrder = postSnapshot.getValue(Order.class);
                                    assert newOrder != null;
                                    cityList3.add(newOrder.Adress);
                                }

                                ArrayList<String> cityList4 = new ArrayList<>();
                                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                                    Order newOrder = postSnapshot.getValue(Order.class);
                                    assert newOrder != null;
                                    cityList4.add(newOrder.PhoneNumber);
                                }

                                ArrayList<Integer> cityList5 = new ArrayList<>();
                                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                                    Order newOrder = postSnapshot.getValue(Order.class);
                                    assert newOrder != null;
                                    cityList5.add((int) newOrder.cost);
                                }

                                ArrayList<String> cityList6 = new ArrayList<>();
                                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                                    Order newOrder = postSnapshot.getValue(Order.class);
                                    assert newOrder != null;
                                    cityList6.add(newOrder.complaint);
                                }

                                ArrayList<String> cityList7 = new ArrayList<>();
                                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                                    Order newOrder = postSnapshot.getValue(Order.class);
                                    assert newOrder != null;
                                    cityList7.add(newOrder.payment);
                                }

                                ArrayList<String> cityList8 = new ArrayList<>();
                                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                                    Order newOrder = postSnapshot.getValue(Order.class);
                                    assert newOrder != null;
                                    cityList8.add(newOrder.userNameClient);
                                }

                                ArrayList<Integer> cityList9 = new ArrayList<>();
                                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                                    Order newOrder = postSnapshot.getValue(Order.class);
                                    assert newOrder != null;
                                    cityList9.add(newOrder.points);
                                }

                                points = new int[size];
                                hour = new String[size];
                                order = new String[size];
                                adress = new String[size];
                                phoneNumber = new String[size];
                                price = new String[size];
                                soloPrice = new double[size];
                                complaint = new String[size];
                                payment = new String[size];
                                userNameClient = new String[size];



                                for ( int i=0; i < size; i++)
                                {
                                    points[i] = cityList9.get(i);
                                }

                                for ( int i=0; i < size; i++)
                                {
                                    hour[i] = cityList.get(i);
                                }



                                for ( int i=0; i < size; i++)
                                {
                                    order[i] = cityList2.get(i);
                                }


                                for ( int i=0; i < size; i++)
                                {
                                    adress[i] = cityList3.get(i);
                                }

                                for ( int i=0; i < size; i++)
                                {
                                    phoneNumber[i] = cityList4.get(i);
                                }

                                for ( int i=0; i < size; i++)
                                {
                                    price[i] = cityList5.get(i) + " PLN";
                                }

                                for( int i =0; i < size; i++)
                                {
                                    soloPrice[i] = cityList5.get(i);
                                }

                                for( int i =0; i < size; i++)
                                {
                                    complaint[i] = cityList6.get(i);
                                }


                                for (int i=0; i < size; i++)
                                {
                                    TotalPrice = TotalPrice+soloPrice[i];
                                }

                                for (int i=0; i < size; i++)
                                {
                                    payment[i] = cityList7.get(i);
                                }

                                for (int i=0; i < size; i++)
                                {
                                    userNameClient[i] = cityList8.get(i);
                                }


                                createPDFFILE_Complaints(Common.getAppPath(context)+"test_pdf.pdf");

                                firebaseDatabase.getReference("Complaint").orderByChild("hour").removeEventListener(this);
                            }



                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });
                        create_pdf_button_complaints.setBackgroundResource(R.drawable.pdf_button);
                    }
                });
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

            }
        }).check();




        return layout;
    }

    private void createPDFFILE_Complaints(String path) {
        if(new File(path).exists())
            new File(path).delete();
        try {
            //Tworzenie dokumentu
            Document document = new Document();
            //Zapis dokument
            PdfWriter.getInstance(document, new FileOutputStream(path));
            //Otwórzenie dokument
            document.open();

            //Ustawienia dokumentu
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor(userName);
            document.addCreator(userName);

            //Font Setting
            BaseColor colorAccent = new BaseColor(0, 153, 204, 255);

            float frontSize = 11.0f;
            float valueFrontSize = 14.0f;


            //Custom font
            BaseFont fontName = BaseFont.createFont("assets/fonts/bigjohnpro_regular.otf",
                    "UTF-8", BaseFont.EMBEDDED);

            //Create title of Doc
            Font titleFont = new Font(fontName, 24.0f, Font.NORMAL, BaseColor.BLACK);
            Font orderFont = new Font(fontName, 18.0f, Font.NORMAL, BaseColor.BLACK);
            addNewItem(document, "Reklamacje zrealizowane", Element.ALIGN_CENTER, titleFont);

            //Add more
            Font orderNumberFont = new Font(fontName, frontSize, Font.NORMAL, colorAccent);
            Font fontBlueBig = new Font(fontName, valueFrontSize, Font.NORMAL, colorAccent);
            Font fontRedBig = new Font(fontName, valueFrontSize, Font.NORMAL, BaseColor.RED);
            Font fontGreenBig = new Font(fontName, valueFrontSize, Font.NORMAL, BaseColor.GREEN);

            Font orderNumberValueFont = new Font(fontName, valueFrontSize, Font.NORMAL, BaseColor.BLACK);


            Font smallWorlds = new Font(fontName, frontSize, Font.NORMAL, BaseColor.BLACK);


            addNewItem(document, "Order Date", Element.ALIGN_LEFT, orderNumberFont);
            addNewItem(document, time, Element.ALIGN_LEFT, orderNumberValueFont);

            addLineSeparator(document);

            addNewItem(document, "Pracownik:", Element.ALIGN_LEFT, orderNumberFont);
            addNewItem(document, userName, Element.ALIGN_LEFT, orderNumberValueFont);

            addLineSeparator(document);




            for (int i =0; i < size; i++)
            {
                HourPdf = String.valueOf(hour[i]);
                OrderPdf = String.valueOf(order[i]);
                AdressPdf =  String.valueOf(adress[i]);
                PhoneNumberPdf = String.valueOf(phoneNumber[i]);
                PricePdf = soloPrice[i];
                String complaintPdf = complaint[i];
                paymentPdf = payment[i];
                userNameStringPdf = userNameClient[i];
                pointsInt = points[i];
                double priceWihtPoints =  (double) (pointsInt/100) + PricePdf;
                pointsString = pointsInt + "pkt. " +"( laczna wartosc : " +priceWihtPoints + " PLN) " ;

                addLineSpace(document);
                addNewItem(document, "ZAMOWIENIE:", Element.ALIGN_CENTER, orderFont);



                addNewItem(document,  OrderPdf, Element.ALIGN_LEFT, smallWorlds);
                addNewItem(document, " ", Element.ALIGN_CENTER, smallWorlds);
                addNewItem(document, "Adres:", Element.ALIGN_CENTER, orderNumberValueFont);
                addNewItem(document,  AdressPdf, Element.ALIGN_CENTER, orderNumberValueFont);
                addNewItem(document, " ", Element.ALIGN_CENTER, smallWorlds);
                addNewItem(document, "Telefon: " + PhoneNumberPdf, Element.ALIGN_CENTER,orderNumberValueFont);
                addNewItem(document,  "Kwota: " + PricePdf + " PLN", Element.ALIGN_CENTER, orderNumberValueFont);
                addNewItem(document,  "Punkty: " + pointsString , Element.ALIGN_CENTER, orderNumberValueFont);
                addNewItem(document, "Metoda platnosci: " + paymentPdf, Element.ALIGN_CENTER,fontBlueBig);
                addNewItem(document,  "Uzytkownik: " + userNameStringPdf, Element.ALIGN_CENTER, fontGreenBig);
                addNewItem(document, "Powod reklamacji:", Element.ALIGN_CENTER, fontRedBig);
                addNewItem(document, complaintPdf, Element.ALIGN_CENTER, fontRedBig);
                addLineSeparator(document);
            }


            //Total
            addLineSpace(document);
            addLineSpace(document);

            addNewItemWithLeftAndRight(document, TotalPrice + " PLN", orderNumberValueFont, orderNumberValueFont);
            addNewItemWithLeftAndRight(document, TotalPoints + " Punktow" + " (" + TotalPoints/100 + " PLN)", orderNumberValueFont, orderNumberValueFont);
            double totalWithPoints = (double) (TotalPoints/100) + TotalPrice;
            addNewItemWithLeftAndRight(document,   " Laczna wartosc: " + totalWithPoints + " PLN", orderNumberValueFont, orderNumberValueFont);

            document.close();

            Toast.makeText(context, "Utworzono pdf", Toast.LENGTH_SHORT).show();

            PrintPDF();

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    private void getNameAndId() {

        String firebaseEmail = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();
        String beforeEmail = StringUtils.substringBefore(firebaseEmail, "@");
        String name = StringUtils.substringBefore(beforeEmail, ".");
        String lastName = StringUtils.substringAfter(beforeEmail, ".");
        userName = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase() + " "
                + lastName.substring(0,1).toUpperCase() + lastName.substring(1).toLowerCase();

    }



    private void createPDFFILE(String path)
    {
        if(new File(path).exists())
            new File(path).delete();
        try {
            //Stwórz dokument
            Document document = new Document();
            //Zapisz dokument
            PdfWriter.getInstance(document, new FileOutputStream(path));
            //Otwórz dokument
            document.open();
            //Ustawienia dokumentu
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor(userName);
            document.addCreator(userName);

            //Ustawienia czcionek
            BaseColor colorAccent = new BaseColor(0, 153, 204, 255);
            float frontSize = 11.0f;
            float valueFrontSize = 14.0f;


            //Wybranie własnej czcionki
            BaseFont fontName = BaseFont.createFont("assets/fonts/bigjohnpro_regular.otf",
                    "UTF-8", BaseFont.EMBEDDED);

            //Stworzenie tytułu dokumentu
            Font titleFont = new Font(fontName, 24.0f, Font.NORMAL, BaseColor.BLACK);
            Font orderFont = new Font(fontName, 18.0f, Font.NORMAL, BaseColor.BLACK);
            Font fontBlueBig = new Font(fontName, valueFrontSize, Font.NORMAL, colorAccent);
            Font fontGreenBig = new Font(fontName, valueFrontSize, Font.NORMAL, BaseColor.GREEN);
            addNewItem(document, "Zamowienia zrealizowane", Element.ALIGN_CENTER, titleFont);

            //Reszta górnej części dokumentu
            Font orderNumberFont = new Font(fontName, frontSize, Font.NORMAL, colorAccent);
            Font orderNumberValueFont = new Font(fontName, valueFrontSize, Font.NORMAL, BaseColor.BLACK);
            Font smallWorlds = new Font(fontName, frontSize, Font.NORMAL, BaseColor.BLACK);
            addNewItem(document, "Order Date", Element.ALIGN_LEFT, orderNumberFont);
            addNewItem(document, time, Element.ALIGN_LEFT, orderNumberValueFont);
            addLineSeparator(document);
            addNewItem(document, "Pracownik:", Element.ALIGN_LEFT, orderNumberFont);
            addNewItem(document, userName, Element.ALIGN_LEFT, orderNumberValueFont);
            addLineSeparator(document);

            //Zapętlamy, aby stworzyć odpowiednie linijki dla każdego jednego zamówienia z bazy.
            for (int i =0; i < size; i++)
            {
                HourPdf = String.valueOf(hour[i]);
                OrderPdf = String.valueOf(order[i]);
                AdressPdf =  String.valueOf(adress[i]);
                PhoneNumberPdf = String.valueOf(phoneNumber[i]);
                PricePdf = soloPrice[i];
                paymentPdf = payment[i];
                userNameStringPdf = userNameClient[i];
                pointsInt = points[i];
                double priceWihtPoints =  (double) (pointsInt/100) + PricePdf;
                pointsString = pointsInt + "pkt. " +"( laczna wartosc : " +priceWihtPoints + " PLN) " ;

                addLineSpace(document);
                addNewItem(document, "ZAMOWIENIE:", Element.ALIGN_CENTER, orderFont);
                addNewItem(document,  OrderPdf, Element.ALIGN_LEFT, smallWorlds);
                addNewItem(document, " ", Element.ALIGN_CENTER, smallWorlds);
                addNewItem(document, "Adres:", Element.ALIGN_CENTER, orderNumberValueFont);
                addNewItem(document,  AdressPdf, Element.ALIGN_CENTER, orderNumberValueFont);
                addNewItem(document, " ", Element.ALIGN_CENTER, smallWorlds);
                addNewItem(document, "Telefon: " + PhoneNumberPdf, Element.ALIGN_CENTER,orderNumberValueFont);
                addNewItem(document, "Metoda platnosci: " + paymentPdf, Element.ALIGN_CENTER,fontBlueBig);
                addNewItem(document,  "Kwota: " + PricePdf + " PLN", Element.ALIGN_CENTER, orderNumberValueFont);
                addNewItem(document,  "Punkty: " + pointsString , Element.ALIGN_CENTER, orderNumberValueFont);
                addNewItem(document,  "Uzytkownik: " + userNameStringPdf, Element.ALIGN_CENTER, fontGreenBig);
                addLineSeparator(document);
            }

            //Dolne podsumowanie dokumentu
            addLineSpace(document);
            addLineSpace(document);

            addNewItemWithLeftAndRight(document, TotalPrice + " PLN", orderNumberValueFont, orderNumberValueFont);
            addNewItemWithLeftAndRight(document, TotalPoints + " Punktow" + " (" + TotalPoints/100 + " PLN)", orderNumberValueFont, orderNumberValueFont);
            double totalWithPoints = (double) (TotalPoints/100) + TotalPrice;
            addNewItemWithLeftAndRight(document,   " Laczna wartosc: " + totalWithPoints + " PLN", orderNumberValueFont, orderNumberValueFont);
            document.close();
            Toast.makeText(context, "Utworzono pdf", Toast.LENGTH_SHORT).show();
            //wyświetlenie dokumentu
            PrintPDF();

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    private void PrintPDF() {
        //utworzenie obiektu printManager
        PrintManager printManager = (PrintManager) activity.getSystemService(Context.PRINT_SERVICE);
        try {
            //wywolanie adaptera, dzieki ktoremu wyswietlimy dokument. Adapter jest rowniez odpowiedzialny za pozniejsze zapisanie dokumentu.
            PrintDocumentAdapter printDocumentAdapter = new PdfDocuemntsAdapter(activity, Common.getAppPath(context) + "test_pdf.pdf");
            printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().build());
        }
        catch (Exception ex)
        {
            Log.e("error 410", ""+ex.getMessage());
        }

    }

    private void addNewItemWithLeftAndRight(Document document, String textRight, Font textLeftFont, Font textRightFont) throws DocumentException {
        // Utworzenie linijki, która dzięli się na 2 częśći: przyklejoną do lewej oraz do prawej strony.
        Chunk chunkTextLeft = new Chunk("SUMA", textLeftFont);
        Chunk chunkTextRight  = new Chunk(textRight, textRightFont);
        Paragraph p = new Paragraph(chunkTextLeft);
        p.add( new Chunk(new VerticalPositionMark()));
        p.add(chunkTextRight);
        document.add(p);
    }

    private void addLineSeparator(Document document) throws DocumentException {
        //utworzenie linii na cala szerokosc
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
        addLineSpace(document);
        document.add( new Chunk(lineSeparator));
        addLineSpace(document);
    }

    private void addLineSpace(Document document) throws DocumentException {
        //utworzenie pustej linijki
        document.add(new Paragraph(""));
    }

    private void addNewItem(Document document, String text, int align, Font font) throws DocumentException {
        //utworzenie linijki z pojedynczym tekstem, ktory jest wysrodkowany
        Chunk chunk = new Chunk(text, font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(align);
        document.add(paragraph);
    }

    @Override
    public void onPause() {
        super.onPause();
        TotalPrice =0;

    }

    @Override
    public void onResume() {
        super.onResume();
        fail = false;
        TotalPrice =0;
    }



    private void goToLogin() {

        Intent intent = new Intent(context, SignInActivity.class);
        startActivity(intent);
        activity.finish();
        Toast.makeText(context, "Utracono połączenie", Toast.LENGTH_SHORT).show();
    }

    private void Backpresses() {
        requireActivity().finish();
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }
}