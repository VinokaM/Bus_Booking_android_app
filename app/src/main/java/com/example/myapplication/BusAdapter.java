// BusAdapter.java
package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.BusViewHolder> {

    // List to hold Bus objects and a listener for item clicks
    private List<Bus> busList;
    private OnItemClickListener listener;

    // Interface for handling item clicks
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Method to set the item click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public BusAdapter(List<Bus> busList) {
        this.busList = busList;
    }

    // Inflate the layout for each item in the RecyclerView
    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus, parent, false);
        return new BusViewHolder(view);
    }

    // Bind data to each item in the RecyclerView
    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        Bus bus = busList.get(position);
        holder.bind(bus);
    }

    // Return the total number of items in the RecyclerView
    @Override
    public int getItemCount() {
        return busList.size();
    }

    // ViewHolder class to hold and bind data for each item
    class BusViewHolder extends RecyclerView.ViewHolder {

        // TextViews for displaying bus information
        private TextView txtBusNumber;
        private TextView txtStartEnd;

        private TextView txtTime;

        // Constructor to initialize the TextViews and set the click listener
        public BusViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBusNumber = itemView.findViewById(R.id.txtBusNumber);
            txtStartEnd = itemView.findViewById(R.id.txtEndCity);
            txtTime = itemView.findViewById(R.id.txtTime);

            // Set an OnClickListener to handle item clicks
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }

        // Method to bind bus data to the TextViews
        public void bind(Bus bus) {
            txtBusNumber.setText(bus.getBusNumber());
            txtStartEnd.setText(bus.getStartCity() + " - " + bus.getEndCity());
            txtTime.setText(bus.getTime());
        }
    }
}
