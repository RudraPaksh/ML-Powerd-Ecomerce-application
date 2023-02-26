package io.shubh.e_comm_ver1.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import io.shubh.e_comm_ver1.ItemDetailPage.View.ItemDetailFragment;
import io.shubh.e_comm_ver1.LikedItems.View.LikedItemsFragment;
import io.shubh.e_comm_ver1.Models.LikedItem;
import io.shubh.e_comm_ver1.R;

public class ReclrAdapterClassForLikedItemsList extends RecyclerView.Adapter<ReclrAdapterClassForLikedItemsList.ViewHolder> {
    private List<LikedItem> dataForItemArrayList;
    private Context context;
    LikedItemsFragment callback ;
    Context applicationContext;
   // BagItemsFragment bagItemsFragment;
   /* private CategoryItemsFragment categoryItemsFragment;
    private FragmentActivity activity*/;


    public ReclrAdapterClassForLikedItemsList(Context context,Context applicationContext, LikedItemsFragment callback, List<LikedItem> dataForItems) {
        this.context = context;
        this.dataForItemArrayList = dataForItems;
        this.callback = callback;
        this.applicationContext = applicationContext;
       // this.bagItemsFragment = bagItemsFragment;
       /* this.categoryItemsFragment = categoryItemsFragment;
        this.activity = activity;*/
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName;
        TextView tvItemPrice;
        TextView tvCtgr;
        ImageView ivItemImage;

        RelativeLayout rlErrorOverlay;
        TextView tvErrorMsg;

        RelativeLayout rlContainerFrVariety;
        LinearLayout llVarietyContainer;

        ImageButton btDelete;

        public ViewHolder(View view) {
            super(view);

            tvItemName = (TextView) view.findViewById(R.id.tvItemName);
            tvItemPrice = (TextView) view.findViewById(R.id.tvPrice);
            tvCtgr = (TextView) view.findViewById(R.id.tvCtgr);

            ivItemImage = (ImageView) view.findViewById(R.id.ivItemImage);

            rlErrorOverlay = (RelativeLayout) view.findViewById(R.id.rlErrorOverlay);
            tvErrorMsg = (TextView) view.findViewById(R.id.tvCenterMsg);

            rlContainerFrVariety = (RelativeLayout) view.findViewById(R.id.rlContainerFrVariety);
            llVarietyContainer = (LinearLayout) view.findViewById(R.id.llVarietyContainer);

            btDelete = (ImageButton) view.findViewById(R.id.btDelete);
        }
    }

    @Override
    public ReclrAdapterClassForLikedItemsList.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reclr_item_fr_liked_item_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReclrAdapterClassForLikedItemsList.ViewHolder holder, int position) {


        if (dataForItemArrayList.get(position).isTheOriginalItemDeleted() == false) {

            Log.i("####", "name: " + dataForItemArrayList.get(position).getItemObject().getName());


            holder.tvItemName.setText(dataForItemArrayList.get(position).getItemObject().getName());
            holder.tvCtgr.setText(dataForItemArrayList.get(position).getItemObject().getCategory().replace("//","/"));

            holder.tvItemPrice.setText("₹" + dataForItemArrayList.get(position).getItemObject().getItem_price());
            Glide.with(applicationContext).load(dataForItemArrayList.get(position).getItemObject().getListOfImageURLs().get(0)).centerCrop().into(holder.ivItemImage);

dataForItemArrayList.get(position).getItemObject().setItemLiked(true);

            holder.btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String docId =   dataForItemArrayList.get(position).getItemId() + dataForItemArrayList.get(position).getUserId();
                    callback.onDeleteItemClick(docId ,position);

                    holder.btDelete.setClickable(false);
                }
            });


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ItemDetailFragment itemDetailFragment = new ItemDetailFragment();
                    itemDetailFragment.passData(dataForItemArrayList.get(position).getItemObject());

                    ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                            .add(R.id.drawerLayout, itemDetailFragment ,"ItemDetailFragment")
                           .addToBackStack(null)
                            .commit();

                }
            });


            if (dataForItemArrayList.get(position).getItemObject().isVisibility() == false) {

                //make green overlay ...item aint available now
                holder.rlErrorOverlay.setVisibility(View.VISIBLE);
                holder.tvErrorMsg.setText("Item Not Available");
            }
        } else {
            //item is deleted
            //make a red overlay on the top of item
            holder.rlErrorOverlay.setVisibility(View.VISIBLE);
            holder.tvErrorMsg.setText("Item Deleted");

            holder.btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String docId =   dataForItemArrayList.get(position).getItemId() + dataForItemArrayList.get(position).getUserId();
                    callback.onDeleteItemClick(docId ,position);

                    holder.btDelete.setClickable(false);
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return dataForItemArrayList.size();
    }


}