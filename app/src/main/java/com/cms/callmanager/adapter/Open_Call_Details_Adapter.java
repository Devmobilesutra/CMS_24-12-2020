package com.cms.callmanager.adapter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cms.callmanager.Prefs;
import com.cms.callmanager.R;
import com.cms.callmanager.RepairDetailsActivity;
import com.cms.callmanager.model.Open_Call_Details_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.cms.callmanager.constants.Constant.UserId;

public class Open_Call_Details_Adapter extends RecyclerView.Adapter<Open_Call_Details_Adapter.ViewHolder>{
    private ArrayList<Open_Call_Details_model>  listdata ;
    RadioButton lastCheckedRB = null;

    JSONObject jsonObject ;
    public static JSONArray jsonArrayfor_OpenCallDetail = new JSONArray();
    ;



    //RadioGroup lastCheckedRadioGroup = null;

    // RecyclerView recyclerView;
    public Open_Call_Details_Adapter(ArrayList<Open_Call_Details_model> listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View listItem= layoutInflater.inflate(R.layout.open_call_detail_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      //  final Open_Call_Details_model myListData = listdata[position];
        final Open_Call_Details_model list = listdata.get(position);
        holder.tv_atm_id1.setText(list.getATMId());
        holder.tv_docketNo1.setText(list.getDocketNo());
        holder.tv_call_type1.setText(list.getCallType());
        holder.tv_sub_status1.setText(list.getSubStatus());



        if ( holder.radioGroup!= null){


            holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                switch (checkedId){
                    case  R.id.radio_yes:
                        listdata.get(position).setSelectedYesNo("Yes");




                    break;
                    case  R.id.radio_no:
                        listdata.get(position).setSelectedYesNo("No");
                        break;


                }

                jsonObject = new JSONObject();


                if (jsonArrayfor_OpenCallDetail != null) {
                    for (int i = 0; i < jsonArrayfor_OpenCallDetail.length(); i++) {

                        try {
                            JSONObject testObj = jsonArrayfor_OpenCallDetail.getJSONObject(i);
                            String name = testObj.getString("DocketNo");
                            if (name.equals(list.getDocketNo())) {
                                jsonArrayfor_OpenCallDetail.remove(i);

                                Log.d("", "removeData: "+name);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }





                try {

                    //    jsonObject.put("No_", item.getSelectedItem());
                    jsonObject.put("ParentDocketNo", RepairDetailsActivity.docket_no);
                    jsonObject.put("DocketNo", list.getDocketNo());
                    jsonObject.put("SubStatus", list.getSubStatus());
                    jsonObject.put("DoYouWanttoCloseStatus", list.getSelectedYesNo());
                    jsonObject.put("CreatedBy", Prefs.with(group.getContext()).getString(UserId, ""));
                    jsonObject.put("CallType", list.getCallType());


                    Log.d("", "onClick_jsonObject: "+jsonObject);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                jsonArrayfor_OpenCallDetail.put(jsonObject);
                    Log.d("", "jsonArrayfor_OpenCallDetail: "+jsonArrayfor_OpenCallDetail);


                }
            }
        });

        }else {

        }




        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             /*   jsonObject = new JSONObject();


                Toast.makeText(view.getContext(),"click on item: "+ position, Toast.LENGTH_LONG).show();
                try {

                    //    jsonObject.put("No_", item.getSelectedItem());
                    jsonObject.put("ParentDocketNo",list.getATMId());
                    jsonObject.put("DocketNo", list.getDocketNo());
                    jsonObject.put("SubStatus", list.getSubStatus());
                    jsonObject.put("DoYouWanttoCloseStatus", list.getSelectedYesNo());
                    jsonObject.put("CreatedBy", Prefs.with(view.getContext()).getString(UserId, ""));
                    jsonObject.put("CallType", list.getCallType());


                    Log.d("", "onClick_jsonObject: "+jsonObject);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                jsonArrayfor_OpenCallDetail.put(jsonObject);


*/

            }
        });

    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView tv_docketNo1,tv_call_type1,tv_sub_status1,tv_atm_id1;
        public LinearLayout relativeLayout;
        private RadioGroup radioGroup;
        RadioButton radioYes, radioNo;



        public ViewHolder(View itemView) {
            super(itemView);
            this.tv_atm_id1 = (TextView) itemView.findViewById(R.id.tv_atm_id1);

            this.tv_docketNo1 = (TextView) itemView.findViewById(R.id.tv_docketNo1);
            this.tv_call_type1 = (TextView) itemView.findViewById(R.id.tv_call_type1);
            this.tv_sub_status1 = (TextView) itemView.findViewById(R.id.tv_sub_status1);


            radioYes = itemView.findViewById(R.id.radio_yes);
            radioNo = itemView.findViewById(R.id.radio_no);
            radioGroup = (RadioGroup)itemView. findViewById(R.id.radioGroup);
            relativeLayout = (LinearLayout)itemView.findViewById(R.id.relativeLayout1);


            Log.d("", "jsonArrayfor_OpenCallDetail: "+jsonArrayfor_OpenCallDetail);





        }
    }
}