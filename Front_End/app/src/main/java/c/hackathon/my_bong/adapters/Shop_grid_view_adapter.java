package c.hackathon.my_bong.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import c.hackathon.my_bong.R;
import c.hackathon.my_bong.recyclerModels.Trending_grid_model;

public class Shop_grid_view_adapter  extends RecyclerView.Adapter<Shop_grid_view_adapter.MyViewHolder> {

    private List<Trending_grid_model> itemsList;
    private View itemView;
    private Context mcontext;
    private int flag = 0;


    public Shop_grid_view_adapter(List<Trending_grid_model> itemsList, Context context){
        this.itemsList = itemsList;
        this.mcontext = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,price;
        private ImageView heart,product;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.brandTxtView);
            price = view.findViewById(R.id.priceTxtView);
            heart = view.findViewById(R.id.heartImg);
            product = view.findViewById(R.id.productImgView);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_view_shopping, parent, false);
        return new Shop_grid_view_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Trending_grid_model items = itemsList.get(position);
        holder.name.setText(items.getName());
        holder.price.setText(items.getPrice());


        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .priority(Priority.HIGH);

        Glide.with(mcontext /* context */)
                .applyDefaultRequestOptions(options)
                .load(items.getUrl())
                .into(holder.product);


        holder.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag==0){
                    flag=1;
                    holder.heart.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.insta_heart));
                }else{
                    flag=0;
                    holder.heart.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.heart));
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return itemsList.size();
    }


}
