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

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import c.hackathon.my_bong.R;
import c.hackathon.my_bong.adapters.Trending_grid_view_adapter;
import c.hackathon.my_bong.recyclerModels.Trending_grid_model;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends Fragment {


    private Trending_grid_view_adapter itemsRecyclerAdapter;
    private List<Trending_grid_model> itemsList = new ArrayList<>();
    private RecyclerView itemsRecyclerView;
    private DatabaseReference mdatabase;
    private DatabaseReference mdatabase1;
    private ProgressDialog pd;


    public CategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_categories, container, false);
        itemsRecyclerAdapter = new Trending_grid_view_adapter(itemsList,this.getActivity());
        itemsRecyclerView = v.findViewById(R.id.itemsRecyclerView);

        mdatabase = FirebaseDatabase.getInstance().getReference().child("category_no");
        mdatabase1 = FirebaseDatabase.getInstance().getReference().child("category");


        pd = new ProgressDialog(this.getActivity());
        pd.setMessage("loading");
        pd.setCancelable(false);


        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemsList.clear();
                String folder1 = dataSnapshot.child("1").getValue().toString();
                String folder2 = dataSnapshot.child("2").getValue().toString();
                getData(folder1,folder2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//
//        Trending_grid_model items = new Trending_grid_model();
//        items.setName("Brand xyz");
//        items.setPrice("Rs 599");
//        items.setUrl("https://firebasestorage.googleapis.com/v0/b/my-bong.appspot.com/o/trending%2Fmodel2.jpg?alt=media&token=ba34e179-8a51-4c67-9373-f0f0e97df413");
//        itemsList.add(items);
//        itemsList.add(items);
//        itemsList.add(items);
//        itemsList.add(items);
//        itemsList.add(items);
//        itemsList.add(items);
//        itemsList.add(items);


        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this.getActivity(), 2);
        itemsRecyclerView.setLayoutManager(mGridLayoutManager);

        itemsRecyclerView.setLayoutManager(mGridLayoutManager);
        itemsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        itemsRecyclerView.setAdapter(itemsRecyclerAdapter);

        return v;
    }

    private void getData(String folder1, String folder2) {
        itemsList.clear();
        mdatabase1.child(folder1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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


        mdatabase1.child(folder2).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
    }

}
