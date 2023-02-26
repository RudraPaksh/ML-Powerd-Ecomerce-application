package io.shubh.e_comm_ver1.Adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.shubh.e_comm_ver1.Models.NotifcationObject;
import io.shubh.e_comm_ver1.MyOrders.View.MyOrdersFragment;
import io.shubh.e_comm_ver1.SellerDashboard.View.SellerDashboardFragment;
import io.shubh.e_comm_ver1.Utils.InterfaceForClickCallbackFromAnyAdaptr;
import io.shubh.e_comm_ver1.R;

public class ReclrAdapterClassForNotificationObjects extends RecyclerView.Adapter<ReclrAdapterClassForNotificationObjects.ViewHolder> {
    private List<NotifcationObject> dataForItemArrayList;
    private Context context;
    Context applicationContext;
  //  private boolean ifInitiatedFromSelelrdashboard = false;
    private InterfaceForClickCallbackFromAnyAdaptr interfaceForClickCallbackFromCtgrAdaptr;
    /* private FragmentActivity activity*/;


    public ReclrAdapterClassForNotificationObjects(InterfaceForClickCallbackFromAnyAdaptr interfaceForClickCallbackFromCtgrAdaptr, Context context,     Context applicationContext,List<NotifcationObject> dataForItems) {
        this.context = context;
        this.applicationContext = applicationContext;
        this.dataForItemArrayList = dataForItems;
      //  this.ifInitiatedFromSelelrdashboard = ifInitiatedFromSelelrdashboard;
        this.interfaceForClickCallbackFromCtgrAdaptr = interfaceForClickCallbackFromCtgrAdaptr;
        /*   this.activity = activity;*/


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvContent;
        TextView tvTime;

        ImageView iv;
        RelativeLayout rlContainerReclrItem;

        public ViewHolder(View view) {
            super(view);

            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvContent = (TextView) view.findViewById(R.id.tvContent);
            tvTime = (TextView) view.findViewById(R.id.tvTime);

            iv = (ImageView) view.findViewById(R.id.iv);
            rlContainerReclrItem = (RelativeLayout) view.findViewById(R.id.rlContainerReclrItem);
        }
    }

    @Override
    public ReclrAdapterClassForNotificationObjects.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reclr_item_fr_notification_item_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReclrAdapterClassForNotificationObjects.ViewHolder holder, int position) {

        holder.tvTitle.setText(dataForItemArrayList.get(position).getTitle());
        holder.tvContent.setText( dataForItemArrayList.get(position).getContent());
        holder.tvTime.setText(getDateFromUnix( dataForItemArrayList.get(position).getTime()));

        Glide.with(applicationContext).load(dataForItemArrayList.get(position).getImage_url()).centerCrop().into(holder.iv);

        //todo-  change the color of the holder item  if the notif item hasnt already been read..do this later when app used for real purpose
      //  if(dataForItemArrayList.get(position).isHasItBeenRead()== true){

            holder.rlContainerReclrItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(dataForItemArrayList.get(position).getType().equals("1")){

                        SellerDashboardFragment sellerDashboardFragment = new SellerDashboardFragment();
                        sellerDashboardFragment.setEnterTransition(new Slide(Gravity.RIGHT));
                        sellerDashboardFragment.setExitTransition(new Slide(Gravity.RIGHT));

                        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                                //both parameters for instantiating the fragment will be same as at rootl level of ctgr tree ,the name of ctgr and path is same
                                .add(R.id.drawerLayout,sellerDashboardFragment,"SellerDashboardFragment")
                                .addToBackStack(null)
                                .commit();

                    }else  if(dataForItemArrayList.get(position).getType().equals("2")){

                        MyOrdersFragment myOrdersFragment = new MyOrdersFragment();
                        myOrdersFragment.setEnterTransition(new Slide(Gravity.RIGHT));
                        myOrdersFragment.setExitTransition(new Slide(Gravity.RIGHT));


                        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                                .add(R.id.drawerLayout,  myOrdersFragment,"MyOrdersFragment")
                               .addToBackStack(null)
                                .commit();
                    }

                    //todo- on an item click make the interactor set the status of the notif as read ..do this when app is used forr eal purppose

                }
            });


    }


    @Override
    public int getItemCount() {
        return dataForItemArrayList.size();
    }


    public String getDateFromUnix(Long unix) {
        long unixSeconds = unix;
// convert seconds to milliseconds
        Date date = new java.util.Date(unixSeconds * 1000L);
// the format of your date
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMMM ");
// give a timezone reference for formatting (see comment at the bottom)
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-4"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

}