package com.example.evault;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<CardModel> cardList;
    private OnItemClickListener listener;

    public CardAdapter(List<CardModel> cardList) {
        this.cardList = cardList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardModel currentItem = cardList.get(position);
        holder.bind(currentItem);
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.card_tittle);

            itemView.setOnClickListener(v -> {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION && listener != null) {
                            listener.onItemClick(position, cardList.get(position));
                        }
                    });

                    // Implement long-press logic
                    itemView.setOnLongClickListener(v -> {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION && listener != null) {
                            listener.onItemLongClick(position, cardList.get(position));
                        }
                        return true;
                    });
        }

        public void bind(CardModel card) {
            titleTextView.setText(card.getTitle());
        }
    }

    public void updateData(List<CardModel> updatedList) {
        cardList = updatedList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, CardModel item);
        void onItemLongClick(int position, CardModel item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
