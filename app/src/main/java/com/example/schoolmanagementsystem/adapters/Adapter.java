package com.example.schoolmanagementsystem.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.widget.ImageButton;

import com.example.schoolmanagementsystem.Models.Model;
import com.example.schoolmanagementsystem.R;

//"hello my name is yehya im 20 years old"
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<Model> data;

    public Adapter(List<Model> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Model model = data.get(position);
        holder.bindData(model);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


//VIEWWWWWWWWWWWWWWHOLDERRRRRRRR
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageButton imageButton;
        private TextView cardTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageButton = itemView.findViewById(R.id.imageButton);
            cardTextView = itemView.findViewById(R.id.cardTextView);

            // Set OnClickListener for the ImageButton
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Model model = data.get(position);

                        // Start the new activity with an Intent
                        Intent intent = new Intent(view.getContext(), model.getDestinationActivity());
                        view.getContext().startActivity(intent);
                    }
                }
            });
        }

    public void bindData(Model model) {
            imageButton.setImageResource(model.getButtonImageResourceId());
            cardTextView.setText(model.getCardText());
    }
    }
}
