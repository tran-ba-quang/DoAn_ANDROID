package com.example.myloginapp_2.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myloginapp_2.Model.Chat;
import com.example.myloginapp_2.Model.User;
import com.example.myloginapp_2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context mContext;
    private List<Chat> mChat;
    private String imageurl;
    FirebaseUser firebaseUser;
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    public MessageAdapter(Context mContext, List<Chat> mChat, String imageurl){
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==MSG_TYPE_RIGHT) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);

            return new ViewHolder(view);
        }else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);

            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Context context = mContext.getApplicationContext();
        Chat chat = mChat.get(position);
        if (isValidContextForGlide(context)){
            // Load image via Glide lib using context
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.img_1)
                    .error(R.drawable.img_1);
            holder.show_message.setText(chat.getMessage());
            if (imageurl.equals("default"))
            {
                holder.profile_image.setImageResource(R.drawable.img_1);
            }else {
                Glide.with(context).load(imageurl).apply(options).into(holder.profile_image);
            }
        }
//        Chat chat = mChat.get(position);
//        holder.show_message.setText(chat.getMessage());
//        if (imageurl.equals("default")){
//            holder.profile_image.setImageResource(R.drawable.img_1);
//        }else {
//            Glide.with(mContext).load(imageurl).into(holder.profile_image);
//        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public static boolean isValidContextForGlide(final Context context){
        if(context == null){
            return false;
        }
        if (context instanceof Activity){
            final Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isDestroyed()){
                return false;
            }
        }
        return true;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profile_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(firebaseUser.getUid())){
            return  MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}
