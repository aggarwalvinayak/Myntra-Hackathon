package c.hackathon.my_bong.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import c.hackathon.my_bong.Activities.HomeActivity;
import c.hackathon.my_bong.R;
import c.hackathon.my_bong.adapters.Trending_grid_view_adapter;
import c.hackathon.my_bong.recyclerModels.Trending_grid_model;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrendingFragment extends Fragment {


    private Trending_grid_view_adapter itemsRecyclerAdapter;
    private List<Trending_grid_model> itemsList = new ArrayList<>();
    private RecyclerView itemsRecyclerView;
    private DatabaseReference mdatabase;
    private ProgressDialog pd;

    public TrendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_trending, container, false);
        itemsRecyclerAdapter = new Trending_grid_view_adapter(itemsList,this.getActivity());
        itemsRecyclerView = v.findViewById(R.id.itemsRecyclerView);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("crowdfunding");

        pd = new ProgressDialog(this.getActivity());
        pd.setMessage("loading");
        pd.setCancelable(false);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this.getActivity(), 2);
        itemsRecyclerView.setLayoutManager(mGridLayoutManager);

        itemsRecyclerView.setLayoutManager(mGridLayoutManager);
        itemsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        itemsRecyclerView.setAdapter(itemsRecyclerAdapter);


        pd.show();
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemsList.clear();
                for(DataSnapshot AllItems : dataSnapshot.getChildren()){
                    pd.dismiss();
                    Trending_grid_model items = new Trending_grid_model();
                    items.setName("Brand xyz");
                    items.setPrice("Rs 599");
                    items.setUrl(AllItems.child("url").getValue().toString());
                    itemsList.add(items);
                }
                itemsRecyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }

}
