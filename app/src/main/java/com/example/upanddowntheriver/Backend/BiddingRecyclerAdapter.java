package com.example.upanddowntheriver.Backend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upanddowntheriver.R;

import java.util.ArrayList;


public class BiddingRecyclerAdapter extends RecyclerView.Adapter<BiddingRecyclerAdapter.ViewHolder> {
    private final ArrayList<String> names;
    private final Round round;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameText;
        private final TextView bidText;

        public ViewHolder(View view) {
            super(view);
            this.nameText = view.findViewById(R.id.biddingNameTextId);
            this.bidText = view.findViewById(R.id.biddingBidTextId);
        }

        public TextView getNameText() {
            return nameText;
        }

        public TextView getBidText() {
            return bidText;
        }
    }

    public BiddingRecyclerAdapter(ArrayList<String> names, Round round) {
        this.names = names;
        this.round = round;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bidding_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        String name = names.get(position);
        int bid = round.getBid(name);
        viewHolder.getNameText().setText(name);
        viewHolder.getBidText().setText(String.valueOf(bid));
    }

    @Override
    public int getItemCount() {
        return this.names.size();
    }
}