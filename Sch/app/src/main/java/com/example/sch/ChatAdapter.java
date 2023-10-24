package com.example.sch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<ChatMessage> chatMessages;

    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
        notifyDataSetChanged();
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_text, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);
        // Bind the chat message data to the views in the ViewHolder
        holder.bind(chatMessage);
    }

    @Override
    public int getItemCount() {
        return chatMessages != null ? chatMessages.size() : 0;
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView senderTextView;
        private TextView messageTextView;
        private ImageView imageView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.senderTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            imageView = itemView.findViewById(R.id.imageView);
        }

        public void bind(ChatMessage chatMessage) {
            // Bind the chat message data to the views
            if (chatMessage.getSenderId().equals("v85XBgdSOgO2TYvgV8Aw0vGfB9u1")) {
                // Set the sender as "Admin" for messages sent by the current user
                senderTextView.setText("Admin");
            } else {
                senderTextView.setText(chatMessage.getSenderId());
            }

            messageTextView.setText(chatMessage.getMessage());

            // Load and display the image using Picasso
            if (chatMessage.getImageUrl() != null) {
                Picasso.get().load(chatMessage.getImageUrl()).into(imageView);
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }
        }
    }
}
