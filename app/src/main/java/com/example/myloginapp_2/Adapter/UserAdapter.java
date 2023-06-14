package com.example.myloginapp_2.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myloginapp_2.MessageActivity;
import com.example.myloginapp_2.Model.Chat;
import com.example.myloginapp_2.Model.User;
import com.example.myloginapp_2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.app.Application;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private List<User> mUsers;
    private boolean ischat;

    private static final String TAG = "Firebase";
    String theLastMessage;

   public UserAdapter (Context mContext, List<User> mUsers, boolean ischat){
       this.mUsers = mUsers;
       this.mContext = mContext;
       this.ischat = ischat;
   }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent,false);

        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//       final User user = mUsers.get(position);
//        holder.username.setText(user.getUsername());
//        if (user.getImageURL().equals("default")){
//            holder.profile_image.setImageResource(R.drawable.img_1);
//        }else {
//            Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
//        }

        final Context context = mContext.getApplicationContext();
        final User user = mUsers.get(position);
        if (isValidContextForGlide(context)){
            // Load image via Glide lib using context

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.img_1)
                    .error(R.drawable.img_1);
            holder.username.setText(user.getUsername());
            if (user.getImageURL().equals("default"))
            {
                holder.profile_image.setImageResource(R.drawable.img_1);
            }else {

                Glide.with(context).load(user.getImageURL()).apply(options).into(holder.profile_image);
            }
        }

        if (ischat){
            if (user.getStatus().equals("online")){
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            }else {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
            lastMessage(user.getId(),holder.last_msg);
            if (user.getUnseenMessages() == 0){
                holder.unseenMessages.setVisibility(View.GONE);
            }
            else {
                holder.unseenMessages.setVisibility(View.VISIBLE);
                holder.unseenMessages.setText(user.getUnseenMessages()+"");
            }
        }else {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.GONE);
                holder.last_msg.setVisibility(View.GONE);
            }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                mContext.startActivity(intent);
            }
        });
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

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

       public TextView username;
       public  ImageView profile_image;
       private ImageView img_on;
       private ImageView img_off;
       private TextView last_msg;
       private TextView unseenMessages;


       public ViewHolder(@NonNull View itemView) {
           super(itemView);

           username = itemView.findViewById(R.id.username);
           profile_image = itemView.findViewById(R.id.profile_image);
           last_msg = itemView.findViewById(R.id.last_msg);
           img_on = itemView.findViewById(R.id.img_on);
           img_off = itemView.findViewById(R.id.img_off);
           unseenMessages = itemView.findViewById(R.id.unseenMessges);
       }
   }

   private void lastMessage(String userid, TextView last_msg){
       theLastMessage = "default";
       FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
       DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                   Chat chat = dataSnapshot.getValue(Chat.class);
                  if (firebaseUser != null){
                      if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                              chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())){
                          theLastMessage = chat.getMessage();
                      }
                  }else {
                      Log.d(TAG,"Firebase User is null");
                  }
               }
               switch (theLastMessage){
                   case "default":
                       last_msg.setText("No Message");
                       break;

                   default:
                       last_msg.setText(theLastMessage);
                       break;
               }
               theLastMessage = "default";
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
   }
}
