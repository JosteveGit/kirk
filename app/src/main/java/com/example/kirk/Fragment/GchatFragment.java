package com.example.kirk.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kirk.Adapter.ChatUserAdapter;
import com.example.kirk.Message;
import com.example.kirk.Model.Chat;
import com.example.kirk.Model.User;
import com.example.kirk.OneViewModel;
import com.example.kirk.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class GchatFragment extends Fragment {
    private RecyclerView recyclerView;

    private ChatUserAdapter chatUserAdapter;
    private List<Message> mUsers;

    FirebaseUser fuser;
    DatabaseReference reference;

    private List<String> usersList;

    private OneViewModel oneViewModel;

    private Context context;

    public GchatFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gchat, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(chatUserAdapter);


        mUsers = new ArrayList<>();

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        oneViewModel = ViewModelProviders.of(this).get(OneViewModel.class);
        getEveryMessage();


//        usersList = new ArrayList<>();
//
//        reference = FirebaseDatabase.getInstance().getReference("Chats").child(fuser.getUid());
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                usersList.clear();
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    Chat chat = snapshot.getValue(Chat.class);
//
//                    if (chat.getSender().equals(fuser.getUid())){
//                        usersList.add(chat.getReceiver());
//                    }
//                    if (chat.getReceiver().equals(fuser.getUid())){
//                        usersList.add(chat.getSender());
//                    }
//
//                }
//
//                readChats();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        return view;
    }

//    private void readChats() {
//
//        reference = FirebaseDatabase.getInstance().getReference("Users");
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mUsers.clear();
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    User user = snapshot.getValue(User.class);
//
//                    // display 1 user from chats
//                    for (String id : usersList) {
//                        if (user.getId().equals(id)) {
//                            if (mUsers.size() != 0) {
//                                for (User user1 : mUsers) {
//                                    if (!user.getId().equals(user1.getId())) {
//                                        mUsers.add(user);
//                                    }
//                                }
//                            } else {
//                                mUsers.add(user);
//                            }
//                        }
//                    }
//                }
//
//                chatUserAdapter = new ChatUserAdapter(getContext(), mUsers, true);
//                recyclerView.setAdapter(chatUserAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    private void getEveryMessage() {
        final List<Chat> chats = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Chats");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                String theIdToSearchFor = fuser.getUid();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Chat chat = dataSnapshot1.getValue(Chat.class);
                    if (Objects.requireNonNull(chat).getSender().equals(theIdToSearchFor)) {
                        chats.add(chat);
                    }
                }

                for (final Chat chat1 : removeRedundancies(chats)) {
                    FirebaseDatabase.getInstance().getReference("Users").child(chat1.getReceiver()).addValueEventListener(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    User user = dataSnapshot.getValue(User.class);
                                    mUsers.add(new Message(user, chat1.getMessage()));
                                    oneViewModel.set_messages(mUsers);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            }
                    );
                }

                oneViewModel.get_messages().observe((LifecycleOwner) context, new Observer<List<Message>>() {
                    @Override
                    public void onChanged(List<Message> messages) {
                        chatUserAdapter = new ChatUserAdapter(getContext(), mUsers, true, null);
                        Log.d("SettingAdapter", "YES");
                        recyclerView.setAdapter(chatUserAdapter);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private List<Chat> arrangeChat(List<Chat> chats) {
        Chat[] chats1 = new Chat[chats.size()];
        for(int i=0;i< chats.size();i++){
            chats1[i] = chats.get(i);
        }
        for (int i = 0; i < chats.size(); i++) {
            for (int j = i + 1; j < chats.size(); j++) {
                if (chats1[i].getCount() < chats1[j].getCount()) {
                    Chat temp = chats1[i];
                    chats1[i] = chats1[j];
                    chats1[j] = temp;
                }
            }
        }
        List<Chat> result = new ArrayList<>();
        Collections.addAll(result, chats1);
        Log.d("ArrangeValue", result.size()+"");

        return result;
    }

    private List<Chat> removeRedundancies(List<Chat> result) {
        Map<String, Integer> receivers = new HashMap<>();
        for (Chat chat : result) {
            if (receivers.containsKey(chat.getReceiver())) {
                Log.d("ReceiverContainsKey", "Yes");
                if (receivers.get(chat.getReceiver()) < chat.getCount()) {
                    receivers.remove(chat.getReceiver());
                    receivers.put(chat.getReceiver(), chat.getCount());
                }
            } else {
                receivers.put(chat.getReceiver(), chat.getCount());
            }
        }
        Log.d("ReceiversValue", receivers.size()+"");
        List<Chat> chats = new ArrayList<>();
        for (Chat chat : result) {
            if (receivers.get(chat.getReceiver()) == chat.getCount()) {
                chats.add(chat);
            }
        }
        Log.d("ReceiversValue", chats.size()+"");

        return arrangeChat(chats);
    }


}
