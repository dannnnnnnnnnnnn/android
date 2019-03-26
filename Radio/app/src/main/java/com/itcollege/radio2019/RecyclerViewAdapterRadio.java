package com.itcollege.radio2019;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerViewAdapterRadio extends RecyclerView.Adapter<RecyclerViewAdapterRadio.ViewHolder> {
    private List<Radio> radios;
    private LayoutInflater inflater;
    private ItemClickListener itemClickListener;
    private Context context;


    public RecyclerViewAdapterRadio(Context context, List<Radio> radios) {
        this.radios = radios;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerViewAdapterRadio.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rowView = inflater.inflate(R.layout.recycler_radio_row, viewGroup, false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterRadio.ViewHolder viewHolder, int i) {
        viewHolder.textViewRadioName.setText(radios.get(i).name);
    }

    @Override
    public int getItemCount() {
        return radios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewRadioName;
        Text textViewRadioStreamName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRadioName = itemView.findViewById(R.id.textViewRadioName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onRecyclerViewRowClick(v, getAdapterPosition());
            }
        }
    }
    void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onRecyclerViewRowClick(View view, int position);
    }
}
