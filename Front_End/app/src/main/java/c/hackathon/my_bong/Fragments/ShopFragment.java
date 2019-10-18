package c.hackathon.my_bong.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import c.hackathon.my_bong.Activities.HomeActivity;
import c.hackathon.my_bong.R;
import c.hackathon.my_bong.adapters.Shop_grid_view_adapter;
import c.hackathon.my_bong.adapters.Trending_grid_view_adapter;
import c.hackathon.my_bong.recyclerModels.Trending_grid_model;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends Fragment {


    private AppBarConfiguration mAppBarConfiguration;
    private RecyclerView itemsRecyclerView;
    private List<Trending_grid_model> itemsList = new ArrayList<>();
    private Shop_grid_view_adapter itemsRecyclerAdapter;
    private TextView popularity,orderHistory;
    private DatabaseReference mDatabasePopularity;
    private DatabaseReference mDatabaseOrderHistory;
    private ProgressDialog pd;

    public ShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.fragment_shop, container, false);
        itemsRecyclerView = v.findViewById(R.id.shopRecyclerView);

        popularity = v.findViewById(R.id.popularityTxtView);
        orderHistory = v.findViewById(R.id.orderHistoryTxtView);

        pd = new ProgressDialog(this.getActivity());
        pd.setMessage("loading");
        pd.setCancelable(false);

        mDatabasePopularity = FirebaseDatabase.getInstance().getReference().child("popularity");
        mDatabaseOrderHistory = FirebaseDatabase.getInstance().getReference().child("orderHistory");



        mDatabasePopularity.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemsList.clear();
                for(DataSnapshot AllNews : dataSnapshot.getChildren()){
                    pd.dismiss();
                    Trending_grid_model items = new Trending_grid_model();
                    items.setName("Brand xyz");
                    items.setPrice("Rs 599");
                    Log.e("shop",AllNews.child("url").getValue().toString());
                    items.setUrl(AllNews.child("url").getValue().toString());
                    itemsList.add(items);
                    itemsList.add(items);
                    itemsList.add(items);
                }
                itemsRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd.dismiss();

            }
        });



        popularity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.show();
                popularity.setTextColor(getResources().getColor(R.color.white));
                popularity.setBackgroundColor(getResources().getColor(R.color.textColor));
                orderHistory.setTextColor(getResources().getColor(R.color.textColor));
                orderHistory.setBackgroundColor(getResources().getColor(R.color.white));


                itemsList.clear();
                mDatabasePopularity.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        itemsList.clear();
                        for(DataSnapshot AllNews : dataSnapshot.getChildren()){
                            pd.dismiss();
                            Trending_grid_model items = new Trending_grid_model();
                            items.setName("Brand xyz");
                            items.setPrice("Rs 599");
                            items.setUrl(AllNews.child("url").getValue().toString());
                            itemsList.add(items);
                            itemsList.add(items);
                            itemsList.add(items);
                        }
                        itemsRecyclerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        pd.dismiss();

                    }
                });

            }
        });

        orderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                orderHistory.setTextColor(getResources().getColor(R.color.white));
                orderHistory.setBackgroundColor(getResources().getColor(R.color.textColor));
                popularity.setTextColor(getResources().getColor(R.color.textColor));
                popularity.setBackgroundColor(getResources().getColor(R.color.white));
                pd.show();
                itemsList.clear();
                mDatabaseOrderHistory.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot AllItems : dataSnapshot.getChildren()){
                            pd.dismiss();
                            Trending_grid_model items = new Trending_grid_model();
                            items.setName("Brand xyz");
                            items.setPrice("Rs 599");
                            items.setUrl(AllItems.child("url").getValue().toString());
                            itemsList.add(items);
                            itemsList.add(items);
                            itemsList.add(items);
                        }
                        itemsRecyclerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        pd.dismiss();
                    }
                });

            }
        });




        itemsRecyclerAdapter = new Shop_grid_view_adapter(itemsList,this.getActivity());

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this.getActivity(), 2);
        itemsRecyclerView.setLayoutManager(mGridLayoutManager);

        itemsRecyclerView.setLayoutManager(mGridLayoutManager);
        itemsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        itemsRecyclerView.setAdapter(itemsRecyclerAdapter);


        return v;
    }

}
