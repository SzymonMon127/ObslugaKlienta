package com.example.obslugaklienta.ObjectsAndAdapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.obslugaklienta.R;

import org.jetbrains.annotations.NotNull;


public class CaptionedImagesAdapterComplaints extends RecyclerView.Adapter<CaptionedImagesAdapterComplaints.ViewHolder> {

    private final String[] hour;
    private final String[] order;
    private final String[] adress;
    private final String[] phone;
    private final String[] cost;
    private final String[] payment;
    private final String[] complaints;
    private final String[] points;
    public Listener listener;

    public interface Listener
    {
        void onClick(int position);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }


    public CaptionedImagesAdapterComplaints(String[] hour, String[] order, String[] adress, String[] phone, String[] cost, String[]payment, String[] complaints, String[] points) {
        this.hour = hour;
        this.order = order;
        this.adress = adress;
        this.phone = phone;
        this.cost = cost;
        this.payment = payment;
        this.complaints = complaints;
        this.points = points;
    }

    @NotNull
    @Override
  public CaptionedImagesAdapterComplaints.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
  {
      CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_captioned_image_complaints, parent, false);
      return new ViewHolder(cv);
  }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final CardView cardView = holder.cardView;

        TextView textView5 = cardView.findViewById(R.id.price);
        textView5.setText(cost[position]);

        TextView textView6 = cardView.findViewById(R.id.payment);
        textView6.setText(payment[position]);

        TextView textView7 = cardView.findViewById(R.id.complaints);
        textView7.setText(complaints[position]);

        TextView textView = cardView.findViewById(R.id.hour);
        textView.setText(hour[position]);

        TextView textView1 = cardView.findViewById(R.id.delivery);
        textView1.setText(order[position]);

        TextView textView8 = cardView.findViewById(R.id.points);
        textView8.setText(points[position] );

        TextView textView2 = cardView.findViewById(R.id.adress);
        textView2.setText(adress[position]);

        TextView textView3 = cardView.findViewById(R.id.phoneNumber);
        textView3.setText(phone[position]);

        cardView.setOnClickListener(v -> {
            if (listener != null)
            {
                listener.onClick(position);
            }
        });



    }

    @Override
    public int getItemCount() {
        return order.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        private final CardView cardView;

        public ViewHolder(CardView v)
        {
            super(v);
            cardView = v;
        }

    }
}

