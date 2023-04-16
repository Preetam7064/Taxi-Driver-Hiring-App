package com.DriverHiring.Taxi.Requests;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.DriverHiring.Taxi.Adatpters.PostAdapter;
import com.DriverHiring.Taxi.DriverRideMap;
import com.DriverHiring.Taxi.ModelClasses.NewPostRider;
import com.DriverHiring.Taxi.ModelClasses.PostDriver;
import com.DriverHiring.Taxi.ModelClasses.PostRider;
import com.DriverHiring.Taxi.ModelClasses.RequestsModel;
import com.DriverHiring.Taxi.ProfilePageStuff.MyAddedPosts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.DriverHiring.Taxi.R;

import static android.content.Context.MODE_PRIVATE;


public class AcceptedRequest extends Fragment {


    String currentUserUid;
    PostAdapter postAdapter;
    ArrayList<PostDriver> postDriverArrayList = new ArrayList<>();
    ListView listView;
    ArrayList<String> myStringData = new ArrayList<>();
    private MyAddedPosts.OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private DatabaseReference myRef;
    private ArrayList<NewPostRider> listrider = new ArrayList<>();
    private String type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_accepted_request, container, false);


        SharedPreferences prefs = getActivity().getSharedPreferences("saveddata", MODE_PRIVATE);
        currentUserUid = prefs.getString("uid", "");
        type = prefs.getString("type", "");

        recyclerView = view.findViewById(R.id.rec_acceptedreq_posts);


        listView = view.findViewById(R.id.mylistV);

        if (type.equals("driver")) {

            recyclerView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);

            myRef = FirebaseDatabase.getInstance().getReference("Accepted Requests").child(currentUserUid);

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    System.err.println("mydata" + dataSnapshot.getKey());
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {


                        myStringData.add(ds.getKey());


                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, myStringData);

                    listView.setAdapter(arrayAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                long id) {
                            Intent intent = new Intent(getActivity(), DriverRideMap.class);
                            Toast.makeText(view.getContext(), ""+myStringData.get(position), Toast.LENGTH_SHORT).show();

                            intent.putExtra("myid", myStringData.get(position));
                            startActivity(intent);
                        }
                    });


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } else if (type.equals("rider")) {

            recyclerView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);

            myRef = FirebaseDatabase.getInstance().getReference("Accepted Requests").child(currentUserUid);

            postAdapter = new PostAdapter(false, getActivity(), postDriverArrayList, listrider, "not");
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(postAdapter);


            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    System.err.println("mydata" + dataSnapshot.getKey());
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        //to avoid nuul exception
                        postDriverArrayList.add(new PostDriver("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));


                        RequestsModel mode = ds.getValue(RequestsModel.class);

                        System.err.println("mydata" + dataSnapshot.getValue());

                        listrider.add(new NewPostRider("", mode.getEndpoint(), mode.getSendername(), mode.getId(), "", "", "", "", "","", mode.getImgurl(),"" , "", mode.getStartpoint(), mode.getSenderid(), ""));


                        postAdapter.notifyDataSetChanged();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

//            myRef.addChildEventListener(new ChildEventListener() {
//                @Override
//                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
//
//                        //to avoid nuul exception
//                        postDriverArrayList.add(new PostDriver("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
//
//
//                        RequestsModel mode = ds.getValue(RequestsModel.class);
//
//                        System.err.println("mydata" + mode.getStartpoint());
//
//                        listrider.add(new PostRider("", "", mode.getEndpoint(), mode.getSendername(), mode.getId(), "", "", "", "", mode.getImgurl(), "", "", mode.getStartpoint(), mode.getSenderid()));
//
//
//                        postAdapter.notifyDataSetChanged();
//
//                    }
//
//
//                }
//
//                @Override
//                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                }
//
//                @Override
//                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                }
//
//                @Override
//                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });

        }

        return view;


    }


}
