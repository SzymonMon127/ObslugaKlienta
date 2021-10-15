package com.example.obslugaklienta.ObjectsAndAdapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.obslugaklienta.R;

import org.jetbrains.annotations.NotNull;


public class CaptionedImagesAdapterWarenhouse extends RecyclerView.Adapter<CaptionedImagesAdapterWarenhouse.ViewHolder> {

    private final String[] name;
    private final int[] availability;

    public Listener listener;

    public interface Listener
    {
        void onClick(int position);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }


    public CaptionedImagesAdapterWarenhouse(String[] name, int[] availability) {
        this.name = name;
        this.availability = availability;

    }

    @NotNull
    @Override
  public CaptionedImagesAdapterWarenhouse.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
  {
      CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_captioned_image_warenhouse, parent, false);
      return new ViewHolder(cv);
  }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final CardView cardView = holder.cardView;



        TextView textView = cardView.findViewById(R.id.name);
        textView.setText(name[position]);

        ImageView imageView = cardView.findViewById(R.id.availability);
        imageView.setBackgroundResource(availability[position]);



        cardView.setOnClickListener(v -> {
            if (listener != null)
            {
                listener.onClick(position);
            }
        });



    }

    @Override
    public int getItemCount() {
        return availability.length;
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

