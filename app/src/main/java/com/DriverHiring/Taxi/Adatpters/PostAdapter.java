package com.DriverHiring.Taxi.Adatpters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.DriverHiring.Taxi.AllPostsWork.ScreenAfterPostIsSelectedFromList;
import com.DriverHiring.Taxi.ModelClasses.NewPostRider;
import com.DriverHiring.Taxi.ModelClasses.PostDriver;
import com.DriverHiring.Taxi.ModelClasses.PostRider;
import com.DriverHiring.Taxi.ModelClasses.RequestsModel;
import com.DriverHiring.Taxi.ProfilePageStuff.RecivedRequestsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.DriverHiring.Taxi.R;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    Context context;
    boolean isDriverP = true;
    String isProfile = "false";


    ArrayList<String> dataremoveTemp = new ArrayList<>();

    private ArrayList<PostDriver> list = new ArrayList<>();
    private ArrayList<NewPostRider> listrider = new ArrayList<>();


    public PostAdapter(Context context, ArrayList<PostDriver> list, ArrayList<NewPostRider> listrider, boolean isDriverP) {
        this.list = list;
        this.listrider = listrider;
        this.isDriverP = isDriverP;
        this.context = context;
    }

    public PostAdapter(boolean isDriverP, Context context, ArrayList<PostDriver> list, ArrayList<NewPostRider> listrider, String isProfile) {
        this.isDriverP = isDriverP;
        this.context = context;
        this.list = list;
        this.listrider = listrider;
        this.isProfile = isProfile;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_row_rec_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final int pos = position;
        //System.err.println("working " + position);
        //System.out.println(listrider.get(0).getFullname());

        if (isProfile.equals("false")) {

            if (isDriverP) {

                final PostDriver obj = list.get(position);

                System.err.println("name is is " + obj.getFullname());
                System.err.println("name 2  is  " + list.get(pos).getFullname());
                System.err.println("driver is here");

                holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(context, ScreenAfterPostIsSelectedFromList.class);
                        intent.putExtra("departuredatetime", obj.getDeparturedatetime());
                        intent.putExtra("endpoint", obj.getEndpoint());
                        intent.putExtra("id", obj.getId());
                        intent.putExtra("latend", obj.getLatend());
                        intent.putExtra("latstart", obj.getLatstart());
                        intent.putExtra("lngend", obj.getLngend());
                        intent.putExtra("lngstart", obj.getLngstart());
                        intent.putExtra("noofpassenger", obj.getNoofpassenger());
                        intent.putExtra("offermessage", obj.getOffermessage());
                        intent.putExtra("profileimgurl", obj.getProfileimgurl());
                        intent.putExtra("regulartrip", obj.getRegulartrip());
                        intent.putExtra("roundtrip", obj.getRoundtrip());
                        intent.putExtra("startpoint", obj.getStartpoint());
                        intent.putExtra("uid", obj.getUid());
                        intent.putExtra("vehicaltype", obj.getVehicaltype());
                        intent.putExtra("phoneno", obj.getPhoneno());
                        intent.putExtra("typeOfIntent", "driver");
                        intent.putExtra("fareamount", obj.getFareamount());
                        intent.putExtra("fullname", obj.getFullname());
                        context.startActivity(intent);


                    }
                });

                holder.nametxt.setText(obj.getFullname());
                holder.startpointtxt.setText(obj.getStartpoint());
                holder.startpointtxt.setSelected(true);

                holder.endpointtxt.setText(obj.getEndpoint());
                holder.endpointtxt.setSelected(true);


            } else {

                final NewPostRider obj = listrider.get(position);

                System.err.println("nameR is is " + obj.getFullname());
                System.err.println("nameR 2  is  " + listrider.get(pos).getFullname());

                System.err.println("rider is here");

                holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(context, ScreenAfterPostIsSelectedFromList.class);
                        intent.putExtra("departuredatetime", obj.getDeparturedatetime());
                        intent.putExtra("endpoint", obj.getEndpoint());
                        intent.putExtra("id", obj.getId());
                        intent.putExtra("latend", obj.getLatend());
                        intent.putExtra("latstart", obj.getLatstart());
                        intent.putExtra("lngend", obj.getLngend());
                        intent.putExtra("lngstart", obj.getLngstart());
                        intent.putExtra("profileimgurl", obj.getProfileimgurl());
                        intent.putExtra("regulartrip", obj.getRegulartrip());
                        intent.putExtra("roundtrip", obj.getRoundtrip());
                        intent.putExtra("startpoint", obj.getStartpoint());
                        intent.putExtra("uid", obj.getUid());
                        intent.putExtra("phoneno", obj.getPhoneno());
                        intent.putExtra("typeOfIntent", "passenger");
                        intent.putExtra("fullname", obj.getFullname());

                        context.startActivity(intent);

                    }
                });

                holder.nametxt.setText(obj.getFullname());
                holder.startpointtxt.setText(obj.getStartpoint());
                holder.startpointtxt.setSelected(true);

                holder.endpointtxt.setText(obj.getEndpoint());
                holder.endpointtxt.setSelected(true);


                Picasso.get()
                        .load(obj.getProfileimgurl())
                        .placeholder(R.drawable.placeholder_user)
                        .error(R.drawable.ic_close)
                        .into(holder.user_profile_img_rec_post);
            }
        } else if (isProfile.equals("true")) {

            if (isDriverP) {

                final PostDriver obj = list.get(position);

                System.err.println("name is is " + obj.getFullname());
                System.err.println("name 2  is  " + list.get(pos).getFullname());
                System.err.println("driver is here");

                holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(context, RecivedRequestsActivity.class);
                        intent.putExtra("departuredatetime", obj.getDeparturedatetime());
                        intent.putExtra("endpoint", obj.getEndpoint());
                        intent.putExtra("id", obj.getId());
                        intent.putExtra("latend", obj.getLatend());
                        intent.putExtra("latstart", obj.getLatstart());
                        intent.putExtra("lngend", obj.getLngend());
                        intent.putExtra("lngstart", obj.getLngstart());
                        intent.putExtra("noofpassenger", obj.getNoofpassenger());
                        intent.putExtra("offermessage", obj.getOffermessage());
                        intent.putExtra("profileimgurl", obj.getProfileimgurl());
                        intent.putExtra("regulartrip", obj.getRegulartrip());
                        intent.putExtra("roundtrip", obj.getRoundtrip());
                        intent.putExtra("startpoint", obj.getStartpoint());
                        intent.putExtra("uid", obj.getUid());
                        intent.putExtra("vehicaltype", obj.getVehicaltype());
                        intent.putExtra("phoneno", obj.getPhoneno());
                        intent.putExtra("typeOfIntent", "driver");
                        intent.putExtra("fareamount", obj.getFareamount());
                        intent.putExtra("fullname", obj.getFullname());
                        context.startActivity(intent);


                    }
                });

                holder.nametxt.setText(obj.getFullname());
                holder.startpointtxt.setText(obj.getStartpoint());
                holder.startpointtxt.setSelected(true);

                holder.endpointtxt.setText(obj.getEndpoint());
                holder.endpointtxt.setSelected(true);


            } else {

                final NewPostRider obj = listrider.get(position);

                System.err.println("nameR is is " + obj.getFullname());
                System.err.println("nameR 2  is  " + listrider.get(pos).getFullname());

                System.err.println("rider is here");
                //Toast.makeText(context, ""+obj.getId(), Toast.LENGTH_SHORT).show();

                holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(context, RecivedRequestsActivity.class);
                        intent.putExtra("departuredatetime", obj.getDeparturedatetime());
                        intent.putExtra("endpoint", obj.getEndpoint());
                        intent.putExtra("id", obj.getId());
                        intent.putExtra("latend", obj.getLatend());
                        intent.putExtra("latstart", obj.getLatstart());
                        intent.putExtra("lngend", obj.getLngend());
                        intent.putExtra("lngstart", obj.getLngstart());
                        intent.putExtra("profileimgurl", obj.getProfileimgurl());
                        intent.putExtra("regulartrip", obj.getRegulartrip());
                        intent.putExtra("roundtrip", obj.getRoundtrip());
                        intent.putExtra("startpoint", obj.getStartpoint());
                        intent.putExtra("uid", obj.getUid());
                        intent.putExtra("phoneno", obj.getPhoneno());
                        intent.putExtra("typeOfIntent", "passenger");
                        intent.putExtra("fullname", obj.getFullname());

                        context.startActivity(intent);

                    }
                });

                holder.nametxt.setText(obj.getFullname());
                holder.startpointtxt.setText(obj.getStartpoint());
                holder.startpointtxt.setSelected(true);

                holder.endpointtxt.setText(obj.getEndpoint());
                holder.endpointtxt.setSelected(true);


                Picasso.get()
                        .load(obj.getProfileimgurl())
                        .placeholder(R.drawable.placeholder_user)
                        .error(R.drawable.ic_close)
                        .into(holder.user_profile_img_rec_post);
            }

        } else if (isProfile.equals("not")) {

            if (isDriverP) {

                final PostDriver obj = list.get(position);


                holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        System.err.println("pro here");

                        //Creating the instance of PopupMenu
                        PopupMenu popup = new PopupMenu(context, holder.mainLayout);
                        //Inflating the Popup using xml file
                        popup.getMenuInflater().inflate(R.menu.menu_select_request_option, popup.getMenu());

                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {


                                switch (item.getTitle().toString()) {
                                    case "Accept Request":

                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accepted Requests").child(obj.getUid()).push();

                                        RequestsModel requestsModel = new RequestsModel();
                                        requestsModel.setEndpoint(obj.getEndpoint());
                                        requestsModel.setStartpoint(obj.getStartpoint());
                                        requestsModel.setImgurl(obj.getProfileimgurl());
                                        requestsModel.setPostid(obj.getPhoneno());
                                        requestsModel.setSendername(obj.getFullname());
                                        requestsModel.setSenderid(obj.getUid());
                                        requestsModel.setReciverid(obj.getNoofpassenger());

                                        requestsModel.setId(ref.getKey());

                                        requestsModel.setStatus("accepted");


                                        ref.setValue(requestsModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Requests").child(obj.getNoofpassenger()).child(obj.getPhoneno()).child(obj.getId());

                                                    ref2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if (task.isSuccessful()) {

                                                                Toast.makeText(context, "Request Accepted, list will be updated on next launch", Toast.LENGTH_SHORT).show();

                                                            }

                                                        }
                                                    });


                                                }
                                            }
                                        });

                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Driver Accepted Requests").child(user.getUid()).child(obj.getPhoneno()).push();


                                        ref2.setValue(requestsModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
//dofrom here
                                                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Requests").child(obj.getNoofpassenger()).child(obj.getPhoneno()).child(obj.getId());

                                                    ref2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if (task.isSuccessful()) {

                                                                Toast.makeText(context, "Request Accepted, list will be updated on next launch", Toast.LENGTH_SHORT).show();

                                                            }

                                                        }
                                                    });


                                                }
                                            }
                                        });


                                        break;

                                    case "Reject Request":
                                        DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference("Declined Requests").child(obj.getUid()).push();

                                        RequestsModel requestsM = new RequestsModel();
                                        requestsM.setEndpoint(obj.getEndpoint());
                                        requestsM.setStartpoint(obj.getStartpoint());
                                        requestsM.setImgurl(obj.getProfileimgurl());
                                        requestsM.setPostid(obj.getId());
                                        requestsM.setSendername(obj.getFullname());
                                        requestsM.setSenderid(obj.getUid());
                                        requestsM.setReciverid(obj.getNoofpassenger());

                                        requestsM.setId(ref3.getKey());

                                        requestsM.setStatus("accepted");

                                        System.err.println("myid is" + obj.getNoofpassenger());
                                        System.err.println("myid is" + obj.getPhoneno());
                                        System.err.println("myid is" + obj.getId());


                                        ref3.setValue(requestsM).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Requests").child(obj.getNoofpassenger()).child(obj.getPhoneno()).child(obj.getId());

                                                    ref2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if (task.isSuccessful()) {

                                                                Toast.makeText(context, "Request removed, list will be updated on next launch", Toast.LENGTH_SHORT).show();

                                                            }

                                                        }
                                                    });
                                                }
                                            }
                                        });


                                        break;


                                }
                                return true;
                            }
                        });

                        popup.show();//showing popup menu
                    }
                });

                holder.nametxt.setText(obj.getFullname());
                holder.startpointtxt.setText(obj.getStartpoint());
                holder.startpointtxt.setSelected(true);

                holder.endpointtxt.setText(obj.getEndpoint());
                holder.endpointtxt.setSelected(true);


            } else {

                final NewPostRider obj = listrider.get(position);

                System.err.println("nameR is is " + obj.getFullname());
                System.err.println("nameR 2  is  " + listrider.get(pos).getFullname());
//                System.err.println("Vehicle Details"+obj.getVehicleDetails());
//                System.err.println("Vehicle Details"+obj.getVehicleDetails());
//                System.err.println("Vehicle Details"+obj.getVehicleDetails());
//                System.err.println("Vehicle Details"+obj.getVehicleDetails());
//                System.err.println("Vehicle Details"+obj.getVehicleDetails());
//                System.err.println("Vehicle Details"+obj.getVehicleDetails());
//                System.err.println("Vehicle Details"+obj.getVehicleDetails());
//                System.err.println("Vehicle Details"+obj.getVehicleDetails());

                System.err.println("rider is here");
                //Toast.makeText(context, "rider is clicked in Post your Travel Rider", Toast.LENGTH_SHORT).show();

                holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(context, "relative layout is clicked", Toast.LENGTH_SHORT).show();
                        //Creating the instance of PopupMenu
                        PopupMenu popup = new PopupMenu(context, holder.mainLayout);
                        //Inflating the Popup using xml file
                        popup.getMenuInflater().inflate(R.menu.menu_select_request_option, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch(menuItem.getTitle().toString()){
                                    case "Accept Request":
                                        //final NewPostRider obj = listrider.get(position);
                                        Log.d("ListRider Object",obj.toString());
                                        //Toast.makeText(context, ""+obj, Toast.LENGTH_SHORT).show();
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accepted Requests").child(obj.getUid()).push();

                                        RequestsModel requestsModel = new RequestsModel();
                                        requestsModel.setEndpoint(obj.getEndpoint());
                                        requestsModel.setStartpoint(obj.getStartpoint());
                                        requestsModel.setImgurl(obj.getProfileimgurl());
                                        requestsModel.setReciverid(obj.getPhoneno());
                                        requestsModel.setPostid(obj.getVehicleDetails());
                                        requestsModel.setSendername(obj.getFullname());
                                        requestsModel.setSenderid(obj.getUid());

                                        requestsModel.setId(ref.getKey());

                                        requestsModel.setStatus("accepted");
                                        ref.setValue(requestsModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Requests").child(obj.getNoOfPassengers()).child(obj.getPhoneno()).child(obj.getId());

                                                    ref2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if (task.isSuccessful()) {

                                                                Toast.makeText(context, "Request Accepted, list will be updated on next launch", Toast.LENGTH_SHORT).show();

                                                            }

                                                        }
                                                    });
                                                }
                                            }
                                        });

                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Passenger Accepted Requests").child(user.getUid()).child(obj.getPhoneno()).push();


                                        ref2.setValue(requestsModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
//dofrom here
                                                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Requests").child(obj.getNoOfPassengers()).child(obj.getPhoneno()).child(obj.getId());

                                                    ref2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if (task.isSuccessful()) {

                                                                Toast.makeText(context, "Request Accepted, list will be updated on next launch", Toast.LENGTH_SHORT).show();

                                                            }

                                                        }
                                                    });


                                                }
                                            }
                                        });

                                        break;
                                    case "Reject Request":
                                        DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference("Passenger Declined Requests").child(obj.getUid()).push();

                                        RequestsModel requestModel = new RequestsModel();
                                        requestModel.setEndpoint(obj.getEndpoint());
                                        requestModel.setStartpoint(obj.getStartpoint());
                                        requestModel.setImgurl(obj.getProfileimgurl());
                                        requestModel.setReciverid(obj.getPhoneno());
                                        requestModel.setPostid(obj.getVehicleDetails());
                                        requestModel.setSendername(obj.getFullname());
                                        requestModel.setSenderid(obj.getUid());
                                        requestModel.setReciverid(obj.getNoOfPassengers());

                                        requestModel.setId(ref3.getKey());
                                        requestModel.setStatus("Rejected");

                                        System.err.println("myid is" + obj.getNoOfPassengers());
                                        System.err.println("myid is" + obj.getPhoneno());
                                        System.err.println("myid is" + obj.getId());


                                        ref3.setValue(requestModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Requests").child(obj.getNoOfPassengers()).child(obj.getPhoneno()).child(obj.getId());

                                                    ref2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if (task.isSuccessful()) {

                                                                Toast.makeText(context, "Request removed, list will be updated on next launch", Toast.LENGTH_SHORT).show();

                                                            }

                                                        }
                                                    });
                                                }
                                            }
                                        });
                                        break;
                                    default:
                                        Toast.makeText(context, "Default", Toast.LENGTH_SHORT).show();
                                }

                                return true;
                            }
                        });
                        popup.show();

                    }
                });

                holder.nametxt.setText(obj.getFullname());
                holder.startpointtxt.setText(obj.getStartpoint());
                holder.startpointtxt.setSelected(true);

                holder.endpointtxt.setText(obj.getEndpoint());
                holder.endpointtxt.setSelected(true);
                //Toast.makeText(context, ""+obj.getProfileimgurl(), Toast.LENGTH_SHORT).show();


//                Picasso.get()
//                        .load(obj.getProfileimgurl())
//                        .placeholder(R.drawable.placeholder_user)
//                        .error(R.drawable.ic_close)
//                        .into(holder.user_profile_img_rec_post);
            }
        } else if (isProfile.equals("Daccept")) {

            if (isDriverP) {

                final PostDriver obj = list.get(position);


                holder.mainLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();


                        //TODO

                        final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Driver Accepted Requests").child(fuser.getUid()).child(obj.getId());


                        ref2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//                                for (DataSnapshot ds: dataSnapshot.getChildren() ) {
//
//                                    System.err.println("mykeyis"+ds.getKey());
//                                    dataremoveTemp.add(ds.getKey());
//
//                                }
//
//                                DatabaseReference ref3 = FirebaseDatabase.getInstance()
//                                        .getReference("Driver Accepted Requests")
//                                        .child(fuser.getUid())
//                                        .child(obj.getId())
//                                        .child(dataremoveTemp.get(pos));
//
//
//                                ref3.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//
//                                        if (task.isSuccessful()) {
//
//                                            Toast.makeText(context, "Request removed, list will be updated on next launch", Toast.LENGTH_SHORT).show();
//
//                                        }
//
//                                    }
//                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        return false;
                    }
                });


                holder.nametxt.setText(obj.getFullname());
                holder.startpointtxt.setText(obj.getStartpoint());
                holder.startpointtxt.setSelected(true);

                holder.endpointtxt.setText(obj.getEndpoint());
                holder.endpointtxt.setSelected(true);


            } else {

                final NewPostRider obj = listrider.get(position);


                holder.mainLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

                        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Driver Accepted Requests").child(fuser.getUid()).child(obj.getId());

                        ref2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {

                                    Toast.makeText(context, "Request removed, list will be updated on next launch", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });

                        return false;
                    }
                });


                holder.nametxt.setText(obj.getFullname());
                holder.startpointtxt.setText(obj.getStartpoint());
                holder.startpointtxt.setSelected(true);

                holder.endpointtxt.setText(obj.getEndpoint());
                holder.endpointtxt.setSelected(true);


                Picasso.get()
                        .load(obj.getProfileimgurl())
                        .placeholder(R.drawable.placeholder_user)
                        .error(R.drawable.ic_close)
                        .into(holder.user_profile_img_rec_post);
            }
        }

    }

    @Override
    public int getItemCount() {

        if (isDriverP) {
            return list.size();

        } else {
            return listrider.size();

        }


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView mainLayout;
        private TextView startpointtxt;
        private TextView endpointtxt;
        private TextView nametxt;
        private CircularImageView user_profile_img_rec_post;

        public MyViewHolder(View view) {
            super(view);
            startpointtxt = (TextView) view.findViewById(R.id.startpointtxt);
            endpointtxt = (TextView) view.findViewById(R.id.endpointtxt);
            nametxt = (TextView) view.findViewById(R.id.name_rec_post);
            mainLayout = view.findViewById(R.id.main_layout);
            user_profile_img_rec_post = (CircularImageView) view.findViewById(R.id.user_profile_img_rec_post);
        }
    }
}
