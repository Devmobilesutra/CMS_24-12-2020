package com.cms.callmanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cms.callmanager.R;
import com.cms.callmanager.model.Alert_model;
import com.cms.callmanager.model.Open_Call_Details_model;

import java.util.ArrayList;

class DialogAlert_Adapter extends RecyclerView.Adapter<DialogAlert_Adapter.ViewHolder>{
    private ArrayList<Open_Call_Details_model>  listdata ;


    // RecyclerView recyclerView;
    public DialogAlert_Adapter(ArrayList<Open_Call_Details_model> listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View listItem= layoutInflater.inflate(R.layout.alert_recycle_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Open_Call_Details_model list = listdata.get(position);
        holder.tv_atm_no.setText(list.getATMId());
        holder.tv_docket_no.setText(list.getDocketNo());
        holder.tv_call_type.setText(list.getCallType());
        holder.tv_sub_status.setText(list.getSubStatus());



        //  holder.imageView.setImageResource(listdata[position].getImgId());
/*        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"click on item: "+myListData.getDescription(),Toast.LENGTH_LONG).show();
            }
        });*/
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView tv_atm_no ,tv_docket_no,tv_call_type,tv_sub_status;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.tv_atm_no = (TextView) itemView.findViewById(R.id.tv_atm_no);
            this.tv_docket_no =(TextView)itemView.findViewById(R.id.tv_docket_no);
            this.tv_call_type =(TextView)itemView.findViewById(R.id.tv_call_type);
            this.tv_sub_status =(TextView)itemView.findViewById(R.id.tv_sub_status);

            //  relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}