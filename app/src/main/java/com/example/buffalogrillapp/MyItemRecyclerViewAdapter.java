package com.example.buffalogrillapp;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.buffalogrillapp.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<Menu> mValues;

    public MyItemRecyclerViewAdapter(List<Menu> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.itemId.setText(mValues.get(position).getId());
        holder.itemName.setText(mValues.get(position).getName());
        holder.itemDuré.setText(mValues.get(position).getDuré_d_vie());
        holder.itemMode.setText(mValues.get(position).getMode_d_emploie());
        holder.itemTmp.setText(mValues.get(position).getTemperature());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView itemId,itemName,itemMode,itemDuré,itemTmp;
        public Menu mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            itemId = (TextView) view.findViewById(R.id.item_number);
            itemName = (TextView) view.findViewById(R.id.item_name);
            itemMode = (TextView) view.findViewById(R.id.item_mode);
            itemDuré = (TextView) view.findViewById(R.id.item_duré);
            itemTmp = (TextView) view.findViewById(R.id.item_temp);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mValues.toString() + "'";
        }
    }
}