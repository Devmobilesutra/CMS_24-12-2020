package com.cms.callmanager.fcm_notification;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cms.callmanager.R;
import com.cms.callmanager.model.Open_Call_Details_model;

import java.util.ArrayList;

class Notification_Adapter extends RecyclerView.Adapter<Notification_Adapter.ViewHolder>{
    private ArrayList<Notification_Model> listdata ;

    ;



    //RadioGroup lastCheckedRadioGroup = null;

    // RecyclerView recyclerView;
    public Notification_Adapter(ArrayList<Notification_Model> listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View listItem= layoutInflater.inflate(R.layout.notification_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //  final Open_Call_Details_model myListData = listdata[position];
        final Notification_Model list = listdata.get(position);
        holder.tv_doc_no.setText(list.getDocketNo());
        holder.tv_atmID.setText(list.getATMId());
        Log.d("", "onBindViewHolder_tv_atmID: "+list.getATMId());
        holder.tv_address.setText(list.getAddress());
        holder.tv_CalllogDateTime.setText(list.getCalllogDateTime());
        holder.tv_TargetResponseTime.setText(list.getTargetResponseTime());
        holder.tv_TargetResolutionTime.setText(list.getTargetResolutionTime());
        holder.RequestType.setText(list.getRequestType());





    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView tv_doc_no,tv_atmID,tv_address,tv_CalllogDateTime,tv_TargetResponseTime,tv_TargetResolutionTime,RequestType;
        public LinearLayout relativeLayout;



        public ViewHolder(View itemView) {
            super(itemView);

            this.tv_doc_no = (TextView) itemView.findViewById(R.id.tv_doc_no);
            this.tv_atmID = (TextView) itemView.findViewById(R.id.tv_atmID);
            this.tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            this.tv_CalllogDateTime = (TextView) itemView.findViewById(R.id.tv_CalllogDateTime);
            this.tv_TargetResponseTime = (TextView) itemView.findViewById(R.id.tv_TargetResponseTime);
            this.tv_TargetResolutionTime = (TextView) itemView.findViewById(R.id.tv_TargetResolutionTime);
            this.RequestType = (TextView) itemView.findViewById(R.id.RequestType);




        }
    }
}