package com.example.schoolmanagementsystem;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView title, description, fullName; // Adding fullName attribute
    View mview;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mview = itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mclicklistener.onitemclick(v, getAdapterPosition());

            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mclicklistener.onitemlongclick(v, getAdapterPosition());
                return true;
            }
        });

        title = itemView.findViewById(R.id.rtitle);
        description = itemView.findViewById(R.id.rdescription);
        fullName = itemView.findViewById(R.id.rfullname); // Initialize fullName TextView
    }

    private ViewHolder.clicklistener mclicklistener;

    public interface clicklistener {
        void onitemclick(View view, int position);

        void onitemlongclick(View view, int position);

    }

    public void setonclicklistener(ViewHolder.clicklistener clicklistener) {
        mclicklistener = clicklistener;

    }
}
