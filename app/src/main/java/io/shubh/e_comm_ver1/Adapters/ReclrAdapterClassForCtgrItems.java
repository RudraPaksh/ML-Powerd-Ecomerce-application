package io.shubh.e_comm_ver1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import io.shubh.e_comm_ver1.ItemDetailPage.View.ItemDetailFragment;
import io.shubh.e_comm_ver1.Utils.InterfaceForClickCallbackFromAnyAdaptr;
import io.shubh.e_comm_ver1.Utils.Utils;
import io.shubh.e_comm_ver1.Models.ItemsForSale;
import io.shubh.e_comm_ver1.R;

public class ReclrAdapterClassForCtgrItems extends RecyclerView.Adapter<ReclrAdapterClassForCtgrItems.ViewHolder> {
    private List<ItemsForSale> dataForItemArrayList;
    private Context context;
    private boolean ifInitiatedFromSelelrdashboard = false;
    private InterfaceForClickCallbackFromAnyAdaptr interfaceForClickCallbackFromCtgrAdaptr;
    Context applicationContext;
     private FragmentActivity activity;//get activty for showing ksnack for log in


    public ReclrAdapterClassForCtgrItems(InterfaceForClickCallbackFromAnyAdaptr interfaceForClickCallbackFromCtgrAdaptr, Context context,Context applicationContext, List<ItemsForSale> dataForItems, boolean ifInitiatedFromSelelrdashboard ,FragmentActivity activity) {
        this.context = context;
        this.applicationContext = applicationContext;
        this.dataForItemArrayList = dataForItems;
        this.ifInitiatedFromSelelrdashboard = ifInitiatedFromSelelrdashboard;
        this.interfaceForClickCallbackFromCtgrAdaptr = interfaceForClickCallbackFromCtgrAdaptr;
           this.activity = activity;


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_item_Title;
        TextView tv_item_price;

        ImageView iv_item_image;

        ImageButton edit_bt;

        public ViewHolder(View view) {
            super(view);

            tv_item_Title = (TextView) view.findViewById(R.id.id_tv_title_fR_rclr_item_ctgr_item_list);
            tv_item_price = (TextView) view.findViewById(R.id.id_tv_price_fR_rclr_item_ctgr_item_list);

            iv_item_image = (ImageView) view.findViewById(R.id.id_iv_fR_rclr_item_ctgr_item_list);

            edit_bt = (ImageButton) view.findViewById(R.id.id_bt_save_fR_rclr_item_ctgr_item_list);
        }
    }

    @Override
    public ReclrAdapterClassForCtgrItems.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reclr_item_fr_ctgr_items_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReclrAdapterClassForCtgrItems.ViewHolder holder, int position) {

        holder.tv_item_Title.setText(dataForItemArrayList.get(position).getName());
        holder.tv_item_price.setText("₹" + dataForItemArrayList.get(position).getItem_price());

        Glide.with(applicationContext).load(dataForItemArrayList.get(position).getListOfImageURLs().get(0)).centerCrop().into(holder.iv_item_image);

        if (dataForItemArrayList.get(position).isItemLiked() == true) {
            holder.edit_bt.setImageDrawable(context.getDrawable(R.drawable.ic_heart2_svg));
        }else{
            holder.edit_bt.setImageDrawable(context.getDrawable(R.drawable.ic_heart_svg));
        }

        if (ifInitiatedFromSelelrdashboard == true) {
            holder.edit_bt.setImageDrawable(context.getDrawable(R.drawable.edit_bt));
        }


        if (ifInitiatedFromSelelrdashboard == false) {
            holder.edit_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(Utils.isUserLoggedIn()==true) {
                        if (dataForItemArrayList.get(position).isItemLiked() == false) {
                            animateHeart(holder.edit_bt);
                            interfaceForClickCallbackFromCtgrAdaptr.onClickOnSaveToLikedItemsBt(String.valueOf(dataForItemArrayList.get(position).getItem_id()));
                            dataForItemArrayList.get(position).setItemLiked(true);
                        } else {
                            holder.edit_bt.setImageDrawable(context.getDrawable(R.drawable.ic_heart_svg));
                            interfaceForClickCallbackFromCtgrAdaptr.onClickOnDeleteFromLikedItemsBt(String.valueOf(dataForItemArrayList.get(position).getItem_id()));
                            dataForItemArrayList.get(position).setItemLiked(false);
                        }
                    }else{
                        Utils.showKsnackForLogin(activity);
                    }
                }
            });
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ItemDetailFragment itemDetailFragment = new ItemDetailFragment();
                itemDetailFragment.passData(dataForItemArrayList.get(position));

                    ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                            .add(R.id.drawerLayout, itemDetailFragment,"ItemDetailFragment")
                            .addToBackStack(null)
                            .commit();





            }
        });


    }


    @Override
    public int getItemCount() {
        return dataForItemArrayList.size();
    }


    public void animateHeart( ImageView view) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        prepareAnimation(scaleAnimation);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        prepareAnimation(alphaAnimation);

        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(alphaAnimation);
        animation.addAnimation(scaleAnimation);
        animation.setDuration(400);
        animation.setFillAfter(false);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                view.setImageDrawable(context.getDrawable(R.drawable.ic_heart2_svg));

            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        view.startAnimation(animation);

    }

    private Animation prepareAnimation(Animation animation){
        animation.setRepeatCount(1);
        animation.setRepeatMode(Animation.REVERSE);
        return animation;
    }

}