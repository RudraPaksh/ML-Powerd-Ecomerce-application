package io.shubh.e_comm_ver1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.shubh.e_comm_ver1.MyOrders.View.MyOrdersFragment;
import io.shubh.e_comm_ver1.Models.Order;
import io.shubh.e_comm_ver1.R;

public class ReclrAdapterClassForOrderItemsList extends RecyclerView.Adapter<ReclrAdapterClassForOrderItemsList.ViewHolder> {
    private List<Order> dataForItemArrayList;
    private Context context;
    private MyOrdersFragment myOrdersFragment;
    Context applicationContext;
    // BagItemsFragment bagItemsFragment;
   /* private CategoryItemsFragment categoryItemsFragment;
    private FragmentActivity activity*/;


    public ReclrAdapterClassForOrderItemsList(MyOrdersFragment myOrdersFragment, Context context,    Context applicationContext, List<Order> dataForItems) {
        this.context = context;
        this.applicationContext = applicationContext;

        this.dataForItemArrayList = dataForItems;
        this.myOrdersFragment =myOrdersFragment;
        //    this.bagItemsFragment = bagItemsFragment;
       /* this.categoryItemsFragment = categoryItemsFragment;
        this.activity = activity;*/
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrdreNo;
        TextView tvOrdrePrice;
        TextView tvTimeOfCreation;
        LinearLayout llContainerFrInlatedItems;


        public ViewHolder(View view) {
            super(view);

            tvOrdreNo = (TextView) view.findViewById(R.id.tvOrdreNo);
            tvOrdrePrice = (TextView) view.findViewById(R.id.tvOrdrePrice);
            tvTimeOfCreation = (TextView) view.findViewById(R.id.tvTimeOfCreation);
            llContainerFrInlatedItems = (LinearLayout) view.findViewById(R.id.rlContainerFrInlatedItems);

        }
    }

    @Override
    public ReclrAdapterClassForOrderItemsList.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reclr_item_fr_order_items_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReclrAdapterClassForOrderItemsList.ViewHolder holder, int position) {



        holder.tvOrdreNo.setText("Order No "+dataForItemArrayList.get(position).getOrderId());
        holder.tvOrdrePrice.setText("₹"+dataForItemArrayList.get(position).getTotalPrice());
        holder.tvTimeOfCreation.setText(getDateFromUnix(dataForItemArrayList.get(position).getTimeOfCreationOfOrder()));


        for (int i = 0; i < dataForItemArrayList.get(position).getSubOrderItems().size(); i++) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View inflatedVarietyBox = inflater.inflate(R.layout.infalte_single_ordered_item_in_order_itmes_of_reclr_view, holder.llContainerFrInlatedItems, false);
            holder.llContainerFrInlatedItems.addView(inflatedVarietyBox);

            ImageView ivItemImage = (ImageView) inflatedVarietyBox.findViewById(R.id.ivItemImage);
            Glide.with(applicationContext).load(dataForItemArrayList.get(position).getSubOrderItems().get(i).getImageUrl()).centerCrop().into(ivItemImage);

            TextView tvItemStatus = (TextView) inflatedVarietyBox.findViewById(R.id.tvItemStatus);
            TextView tvItemPrice = (TextView) inflatedVarietyBox.findViewById(R.id.tvItemPrice);
            TextView tvStatusDate = (TextView) inflatedVarietyBox.findViewById(R.id.tvStatusDate);
            TextView tvItemName = (TextView) inflatedVarietyBox.findViewById(R.id.tvItemName);

            if (dataForItemArrayList.get(position).getSubOrderItems().get(i).getStatusOfOrder() == 2) {
                tvItemStatus.setText("Order Placed");
                tvItemStatus.setTextColor(context.getResources().getColor(R.color.colorGreen));
                tvStatusDate.setText(getDateFromUnix(dataForItemArrayList.get(position).getTimeOfCreationOfOrder()));
            } else if (dataForItemArrayList.get(position).getSubOrderItems().get(i).getStatusOfOrder() == 3) {
                tvItemStatus.setText("Order Packaged And Ready For Shipping");
                tvItemStatus.setTextColor(context.getResources().getColor(R.color.colorGreen));
                tvStatusDate.setText(getDateFromUnix(dataForItemArrayList.get(position).getSubOrderItems().get(i).getTimeOfPackagedOfItem()));
            } else if (dataForItemArrayList.get(position).getSubOrderItems().get(i).getStatusOfOrder() == 4) {
                tvItemStatus.setText("Order is Shipped");
                tvItemStatus.setTextColor(context.getResources().getColor(R.color.colorGreen));
                tvStatusDate.setText(getDateFromUnix(dataForItemArrayList.get(position).getSubOrderItems().get(i).getTimeOfShippedOfItem()));
            } else if (dataForItemArrayList.get(position).getSubOrderItems().get(i).getStatusOfOrder() == 5) {
                tvItemStatus.setText("Order delivered");
                tvItemStatus.setTextColor(context.getResources().getColor(R.color.colorGreen));
                tvStatusDate.setText(getDateFromUnix(dataForItemArrayList.get(position).getSubOrderItems().get(i).getTimeOfDeliveryOfItem()));
            }else if (dataForItemArrayList.get(position).getSubOrderItems().get(i).getStatusOfOrder() == 6) {
                tvItemStatus.setText("Order Cancelled By Seller");
                tvItemStatus.setTextColor(context.getResources().getColor(R.color.colorRed));
                tvStatusDate.setText(getDateFromUnix(dataForItemArrayList.get(position).getSubOrderItems().get(i).getTimeOfCancellationOfItemBySeller()));
                //Todo - in this case add one more tile saying ..that your Amount will be returened ..pls wait for 6 days after cancellatoin
                //if it still amount is not returned ..contact admin at this email
            }

            tvItemPrice.setText("₹"+dataForItemArrayList.get(position).getSubOrderItems().get(i).getItemPrice() );
            tvItemName.setText(dataForItemArrayList.get(position).getSubOrderItems().get(i).getItemName() );


            if( dataForItemArrayList.get(position).getSubOrderItems().size()>1){
                if(i!= dataForItemArrayList.get(position).getSubOrderItems().size()-1){
                    View inflatedBorder = inflater.inflate(R.layout.inflate_divider, holder.llContainerFrInlatedItems, false);
                    holder.llContainerFrInlatedItems.addView(inflatedBorder);
                }
            }


            int finalI = i;
            inflatedVarietyBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myOrdersFragment.onSubOrderItemClick(dataForItemArrayList.get(position) ,finalI);
                }
            });

        }


    }

    public String getDateFromUnix(Long unix) {
        long unixSeconds = unix;
// convert seconds to milliseconds
        Date date = new java.util.Date(unixSeconds * 1000L);
// the format of your date
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMMM yyyy");
// give a timezone reference for formatting (see comment at the bottom)
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-4"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }


    @Override
    public int getItemCount() {
        return dataForItemArrayList.size();
    }


}