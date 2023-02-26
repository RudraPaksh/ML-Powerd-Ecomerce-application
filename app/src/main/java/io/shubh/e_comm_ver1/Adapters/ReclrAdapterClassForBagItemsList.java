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
import io.shubh.e_comm_ver1.BagItems.View.BagItemsFragment;
import io.shubh.e_comm_ver1.Models.BagItem;
import io.shubh.e_comm_ver1.R;

public class ReclrAdapterClassForBagItemsList extends RecyclerView.Adapter<ReclrAdapterClassForBagItemsList.ViewHolder> {
    private List<BagItem> dataForItemArrayList;
    private Context context;
    BagItemsFragment bagItemsFragment;
    Context applicationContext;
   /* private CategoryItemsFragment categoryItemsFragment;
    private FragmentActivity activity*/;


    public ReclrAdapterClassForBagItemsList(Context context,Context applicationContext, BagItemsFragment bagItemsFragment, List<BagItem> dataForItems) {
        this.context = context;
        this.dataForItemArrayList = dataForItems;
        this.bagItemsFragment = bagItemsFragment;
        this.applicationContext = applicationContext;

       /* this.categoryItemsFragment = categoryItemsFragment;
        this.activity = activity;*/
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName;
        TextView tvItemPrice;
        TextView tvItemAmount;
        ImageView ivItemImage;

        ImageButton btPlusItemAmount;
        ImageButton btMinusItemAmount;

        RelativeLayout rlErrorOverlay;
        TextView tvErrorMsg;

        RelativeLayout rlContainerFrVariety;
        LinearLayout llVarietyContainer;
        TextView tvVarietyName;

        ImageButton btDelete;

        public ViewHolder(View view) {
            super(view);

            tvItemName = (TextView) view.findViewById(R.id.tvItemName);
            tvItemPrice = (TextView) view.findViewById(R.id.tvItemPrice);
            tvItemAmount = (TextView) view.findViewById(R.id.tvItemAmount);

            ivItemImage = (ImageView) view.findViewById(R.id.ivItemImage);

            btPlusItemAmount = (ImageButton) view.findViewById(R.id.btPlusItemAmount);
            btMinusItemAmount = (ImageButton) view.findViewById(R.id.btMinusItemAmount);

            rlErrorOverlay = (RelativeLayout) view.findViewById(R.id.rlErrorOverlay);
            tvErrorMsg = (TextView) view.findViewById(R.id.tvCenterMsg);

            rlContainerFrVariety = (RelativeLayout) view.findViewById(R.id.rlContainerFrVariety);
            llVarietyContainer = (LinearLayout) view.findViewById(R.id.llVarietyContainer);
            tvVarietyName = (TextView) view.findViewById(R.id.tvVarietyName);

            btDelete = (ImageButton) view.findViewById(R.id.btDelete);
        }
    }

    @Override
    public ReclrAdapterClassForBagItemsList.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reclr_item_fr_bag_items_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReclrAdapterClassForBagItemsList.ViewHolder holder, int position) {


        if (dataForItemArrayList.get(position).isTheOriginalItemDeleted() == false) {

            Log.i("####", "name: " + dataForItemArrayList.get(position).getItemObject().getName());


            holder.tvItemName.setText(dataForItemArrayList.get(position).getItemObject().getName());
            int itemPrice = Integer.parseInt(dataForItemArrayList.get(position).getItemObject().getItem_price());
            final int[] itemAmount = {Integer.parseInt(dataForItemArrayList.get(position).getItemAmount())};

            holder.tvItemAmount.setText(String.valueOf(itemAmount[0]));
            holder.tvItemPrice.setText("₹" + itemPrice * itemAmount[0]);
            Glide.with(applicationContext).load(dataForItemArrayList.get(position).getItemObject().getListOfImageURLs().get(0)).centerCrop().into(holder.ivItemImage);

            holder.btPlusItemAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    itemAmount[0]++;
                    dataForItemArrayList.get(position).setItemAmount(String.valueOf(itemAmount[0]));
                    holder.tvItemPrice.setText("₹" + String.valueOf(itemPrice * itemAmount[0]));

                    holder.tvItemAmount.setText(String.valueOf(itemAmount[0]));

                }
            });

            holder.btMinusItemAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (Integer.valueOf(dataForItemArrayList.get(position).getItemAmount()) > 1) {
                        itemAmount[0]--;
                        dataForItemArrayList.get(position).setItemAmount(String.valueOf(itemAmount[0]));
                        holder.tvItemPrice.setText("₹" + String.valueOf(itemPrice * itemAmount[0]));

                        holder.tvItemAmount.setText(String.valueOf(itemAmount[0]));
                    }
                }
            });

            //-------------------------doing variety workk------------

            if (dataForItemArrayList.get(position).getItemObject().getVarieies().size() != 0) {
                holder.rlContainerFrVariety.setVisibility(View.VISIBLE);
                holder.tvVarietyName.setText(dataForItemArrayList.get(position).getItemObject().getVarietyName());

                for (int i = 0; i < dataForItemArrayList.get(position).getItemObject().getVarieies().size(); i++) {

                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View inflatedVarietyBox = inflater.inflate(R.layout.infalte_variety_boxes_in_item_detail_frag, holder.llVarietyContainer, false);
                    holder.llVarietyContainer.addView(inflatedVarietyBox);
                    TextView tvIndivVarietyNmae = (TextView) inflatedVarietyBox.findViewById(R.id.tvIndivVarietyNmae);
                    tvIndivVarietyNmae.setText(dataForItemArrayList.get(position).getItemObject().getVarieies().get(i));

                    int finalI = i;
                    tvIndivVarietyNmae.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // chosenvarietyName = tvIndivVarietyNmae.getText().toString();
                            dataForItemArrayList.get(position).setSelectedVarietyIndexInList(String.valueOf(finalI));

                            //removing color from every other if they have it
                            final int childCount = holder.llVarietyContainer.getChildCount();
                            for (int i = 0; i < childCount; i++) {
                                RelativeLayout rlContainingOtherTv = (RelativeLayout) holder.llVarietyContainer.getChildAt(i);
                                TextView otherTv = (TextView) rlContainingOtherTv.findViewById(R.id.tvIndivVarietyNmae);
                                otherTv.setBackgroundColor(context.getResources().getColor(R.color.colorTransparent));
                            }
                            //after removing color now setting it
                            tvIndivVarietyNmae.setBackgroundColor(context.getResources().getColor(R.color.colorSecondaryAtHalfTransparency));
                        }
                    });

                    //coloring the variety as is in databse selected befor
                    if (i == Integer.valueOf(dataForItemArrayList.get(position).getSelectedVarietyIndexInList())) {
                        //  chosenvarietyName = tvIndivVarietyNmae.getText().toString();
                        dataForItemArrayList.get(position).setSelectedVarietyIndexInList(String.valueOf(i));
                        tvIndivVarietyNmae.setBackgroundColor(context.getResources().getColor(R.color.colorSecondaryAtHalfTransparency));
                    }
                }
            }


            holder.btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String docId = dataForItemArrayList.get(position).getUserId() + dataForItemArrayList.get(position).getItemId();
                    bagItemsFragment.onDeleteItemClick(docId, position);

                    holder.btDelete.setClickable(false);
                }
            });


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ItemDetailFragment itemDetailFragment = new ItemDetailFragment();
                    itemDetailFragment.passData(dataForItemArrayList.get(position).getItemObject());

                    ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                            .add(R.id.drawerLayout, itemDetailFragment,"ItemDetailFragment")
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
                    String docId = dataForItemArrayList.get(position).getUserId() + dataForItemArrayList.get(position).getItemId();
                    bagItemsFragment.onDeleteItemClick(docId, position);

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