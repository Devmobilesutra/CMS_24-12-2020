package com.cms.callmanager.Foc_Chargeble;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cms.callmanager.R;

import java.util.ArrayList;

class SnaAdapter extends RecyclerView.Adapter<SnaAdapter.MyViewHolder> {

private LayoutInflater inflater;
private ArrayList<SNA_Model> sna_model;



public SnaAdapter(Context ctx, ArrayList<SNA_Model> myImageNameList){

        inflater = LayoutInflater.from(ctx);
        this.sna_model = myImageNameList;
        }

        @Override
        public SnaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.sna_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
        }


        @Override
        public void onBindViewHolder(SnaAdapter.MyViewHolder holder, int position) {

        holder.SubstituteNo.setText(sna_model.get(position).getSubstituteNo());
        holder.dis.setText(sna_model.get(position).getDescription());

        }


        @Override
        public int getItemCount() {
        return sna_model.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{

          TextView SubstituteNo ,dis;

          public MyViewHolder(View itemView) {

              super(itemView);
              SubstituteNo = (TextView) itemView.findViewById(R.id.SubstituteNo);
              dis = (TextView) itemView.findViewById(R.id.dis);

       /* itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.textView.setText("You have selected : "+myImageNameList[getAdapterPosition()]);
                MainActivity.dialog.dismiss();
            }
        });*/

    }

}
}