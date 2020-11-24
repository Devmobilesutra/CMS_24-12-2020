package com.cms.callmanager.Foc_Chargeble;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.cms.callmanager.R;
import com.cms.callmanager.RepairDetailsActivity;
import com.cms.callmanager.constants.Constants;
import com.cms.callmanager.services.CallManagerAsyncTaskArray;
import com.cms.callmanager.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class ChargeListAdapter extends RecyclerView.Adapter<ChargeListAdapter.ViewHolder>{

    Context context;
    String TNA;
    //ArrayList<String> list_foc;
    public  static ArrayList<ModelClassForSavedData_Charge> list_charge = new ArrayList();
    ArrayList<String> plantsList = new ArrayList();
    int i=0; //used for for loop having some error so declare hare

    String Spinner_values;
    JSONArray jsonArray = new JSONArray();
    public static String qty_value;
    ProgressDialog progDailog;

    Boolean flag = false;

    List<String> categories;

    String DisplayMsg;
    String values;
    String str_item;
    ArrayList<SNA_Model> sna_model = new ArrayList<>();



    ArrayList<Charge_list_Model> charge_list_models = new ArrayList<>();
    ArrayList<String> charge_NO = new ArrayList<String>();



    ArrayAdapter<String> charge_dataAdapter;


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageButton plus, minus;
        Spinner spinner;
        EditText et_qty;
        TextView discription, SpinerItemCharge;

        public ViewHolder(View itemView) {
            super(itemView);
            plus = (ImageButton) itemView.findViewById(R.id.plus);
            minus = (ImageButton) itemView.findViewById(R.id.minus);
            et_qty = (EditText) itemView.findViewById(R.id.step);
            spinner =(Spinner)itemView.findViewById(R.id.spinner);
            SpinerItemCharge =(TextView) itemView.findViewById(R.id.SpinerItemCharge);

            discription=(TextView)itemView.findViewById(R.id.discription);

            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    try {
                        list_charge.remove(position);
                        notifyItemRemoved(position);
                    }catch (ArrayIndexOutOfBoundsException e){e.printStackTrace();}
                }
            });

        }
    }


    public ChargeListAdapter(ArrayList<ModelClassForSavedData_Charge> list_foc1, String tna, final Context context) {
        this.list_charge = list_foc1;
        this.context = context;
        this.TNA = tna;



    }


    public ArrayList<ModelClassForSavedData_Charge> getList_foc() {
        return list_charge;
    }

    void setCharge_NO(ArrayList<String> charge_NO, ArrayList<Charge_list_Model> charge_list_Model ){
        this.charge_NO=charge_NO;
        this.charge_list_models=charge_list_Model;
    }

    @Override
    public int getItemCount() {
        return list_charge.size();
    }

    @Override
    public ChargeListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.charge_list_item, viewGroup, false);
        return new ChargeListAdapter.ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ChargeListAdapter.ViewHolder holder, final int possition) {



        if (possition==0){
            holder.minus.setVisibility(View.INVISIBLE);

        } else {
            holder.minus.setVisibility(View.VISIBLE);

        }
        if(possition==charge_NO.size()-2){
            holder.plus.setVisibility(View.GONE);

        } else {
            holder.plus.setVisibility(View.VISIBLE);

        }


        plantsList = charge_NO;
        int x =possition;
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                context, R.layout.simple_spinner_dropdown_item,plantsList) {


            @Override
            public boolean isEnabled(int position) {

                for (i = 0; i < list_charge.size(); i++) {

                    if (position == list_charge.get(i).getSelectedItemPosition()) {

                        return false;
                    }
                }
                return  true;
            }



            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view =  super.getDropDownView(position, convertView, parent);;
                TextView tv = (TextView) view;;

                for(i = 0; i< list_charge.size(); i++) {

                    if(position == list_charge.get(i).getSelectedItemPosition()) {
                        //selectedPositions.get(i) ;

                        tv.setText("Selected-"+list_charge.get(i).getSelectedItem());


                        // tv.setEnabled(false);

                    }else {
                        // tv.setTextColor(Color.BLACK);


                    }
                }
                return view;

            }




        };





        spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(spinnerArrayAdapter);


/*

        int x =possition;
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<String>(context, R.layout.simple_spinner_dropdown_item, charge_NO);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(dataAdapter);
*/

       // holder.spinner.setVisibility
        holder.et_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
              //  list_charge.get(possition).setQty(s.toString());


                try {
                    if (Integer.parseInt(s.toString())==0){
                        holder.et_qty.setText("");
                    }
                    else
                    {
                        list_charge.get(possition).setQty(s.toString());

                    }
                } catch (Exception e){

                }

            }
        });

        if(list_charge.get(x).getQty().length() > 0 && list_charge.get(x).getDescription().length() > 0 && list_charge.get(x).getSelectedItem().length() > 0 ) {
            holder.spinner.setSelection(getIndex(holder.spinner, list_charge.get(x).getSelectedItem()));
            holder.et_qty.setText(list_charge.get(x).getQty());
            holder.discription.setText(list_charge.get(x).getDescription());


            if (list_charge.size()== 1) {

                holder.minus.setVisibility(View.GONE);

            } else {
                holder.minus.setVisibility(View.VISIBLE);

            }


        }
        else{
            holder.et_qty.setText(null);
            holder.et_qty.requestFocus();
        }



     /*   holder.et_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               // onEditTextChanged.onTextChanged(count, s.toString());

                qty_value = holder.et_qty.getText().toString();

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
*/

        //spinner


        holder.SpinerItemCharge.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                long downTime = SystemClock.uptimeMillis();
                long eventTime = SystemClock.uptimeMillis() + 100;
                float x = 0.0f;
                float y = 0.0f;
                int metaState = 0;
                MotionEvent motionEvent = MotionEvent.obtain(
                        downTime,
                        eventTime,
                        MotionEvent.ACTION_UP,
                        x,
                        y,
                        metaState
                );

                holder.spinner.dispatchTouchEvent(motionEvent);

            }
        });



        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                holder.discription.setText(charge_list_models.get(position).getDiscription());



                String split_item = holder.spinner.getSelectedItem().toString();

                String selected_split_item = split_item.split("~")[0];
                holder.SpinerItemCharge.setText(selected_split_item);
                Log.d("", "onItemSelected_selected_item: "+selected_split_item);



                list_charge.get(possition).setSelectedItemPosition(position);
                list_charge.get(possition).setSelectedItem(holder.spinner.getSelectedItem().toString());
                str_item = holder.spinner.getSelectedItem().toString();
                list_charge.get(possition).setDescription(holder.discription.getText().toString());


                if (selected_split_item.equalsIgnoreCase("-Select Item- ")) {


                }else {


                    new Charge_Validation(holder, Constants.DocketValidationForPartRequest + "?ATMID=" + RepairDetailsActivity.atm_id + "&PartCode=" + selected_split_item, "GET", context, new APIListner() {
                        @Override
                        public void onSuccess() {
                            //  Foc_NO.add("--select--");

                        }

                        @Override
                        public void onErrors() {

                        }
                    }).execute();

                }


                    if(TextUtils.isEmpty(TNA)) {

                    if (selected_split_item.equalsIgnoreCase("-Select Item- ")) {


                    }else{

                        new GetItemwiseItemTypeSNATNA_AsynTask(holder, Constants.SNATNA_url + "?ItemCode=" + selected_split_item, "GET", context, new APIListner() {
                            @Override
                            public void onSuccess() {
                                //  Foc_NO.add("--select--");

                            }

                            @Override
                            public void onErrors() {

                            }
                        }).execute();

                    }



                }else {



                }


            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                try {
                    TNA="";

                    if (holder.discription.getText().toString().equalsIgnoreCase("")) {

                        Toast.makeText(context, "Please Enter description", Toast.LENGTH_SHORT).show();

                    } else if (holder.et_qty.getText().toString().equalsIgnoreCase("")) {

                        Toast.makeText(context, "Please Enter QTY", Toast.LENGTH_SHORT).show();

                    } else {

                        holder.minus.setVisibility(View.VISIBLE);

                        //    dataAdapter.remove((String)spinner.getSelectedItem());
                        list_charge.add(possition + 1, new ModelClassForSavedData_Charge("","",""));
                        notifyItemInserted(possition + 1);

                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        });



        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // int position = getAdapterPosition();
                try {
                    holder.et_qty.clearFocus();
                    holder.discription.clearFocus();
                    list_charge.remove(possition);
                    notifyItemRemoved(possition);
                    notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public ArrayList<ModelClassForSavedData_Charge> getStepList(){
        return list_charge;
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }



    class Charge_Validation extends CallManagerAsyncTaskArray {
        JSONArray postParamData = new JSONArray();
        APIListner apiListner;
        ViewHolder holder;


        public Charge_Validation( ViewHolder holder,String action, String reqType, Context context, APIListner apiListner) {
            super(action, reqType, context);
            this.apiListner = apiListner;
            this.holder = holder;

        }


        @Override
        protected JSONArray doInBackground(Object... params) {

            try {

                return doWorkJSONArray(postParamData);
            } catch (ConnectException e) {
                e.printStackTrace();
            } catch (EOFException e) {
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*progDailog = new ProgressDialog(context);
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();*/
        }

        @Override
        protected void onPostExecute(JSONArray json) {
            super.onPostExecute(json);
            if (json != null) {
                Utils.Log("JSON FOC_CAHRGE_Validation=========" + json.toString());

                try {
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject dataobj = json.getJSONObject(i);
                        String foc_validation = dataobj.getString("ReturnValue");


                        if(foc_validation.equalsIgnoreCase("Not Allow")){
                            holder.spinner.setSelection(0);
                            Toast.makeText(context, "This part already requested for this ATM", Toast.LENGTH_LONG).show();

                        }else{

                            //holder.spinner.setSelection(0);

                           // Toast.makeText(context, "Allow", Toast.LENGTH_SHORT).show();


                        }


                        Log.d("", "foc_cahrge_validation: "+foc_validation);

                    }

                    apiListner.onSuccess();
                    //  progDailog.dismiss();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                // Utils.showAlertBox("Something Went wrong!!", (Activity) context);
                // progDailog.dismiss();

            }
        }

    }






    private class GetItemwiseItemTypeSNATNA_AsynTask extends CallManagerAsyncTaskArray {
        JSONArray postParamData = new JSONArray();
        APIListner apiListner;
        ViewHolder holder;


        public GetItemwiseItemTypeSNATNA_AsynTask(ViewHolder holder, String action, String reqType, Context context, APIListner apiListner) {
            super(action, reqType, context);
            this.apiListner = apiListner;
            this.holder = holder;
        }



        @Override
        protected JSONArray doInBackground(Object... params) {

            try {

                return doWorkJSONArray(postParamData);
            } catch (ConnectException e) {
                e.printStackTrace();
            } catch (EOFException e) {
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

          /*  progDailog = new ProgressDialog(context);
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();*/
        }

        @Override
        protected void onPostExecute(JSONArray json) {
            super.onPostExecute(json);
            if (json != null) {
                Utils.Log("JSON response_SNA_TNA=========" + json.toString());
                try {

                    for (int i = 0; i < json.length(); i++) {
                        JSONObject dataobj = json.getJSONObject(i);

                        values = (dataobj.getString("Values"));
                        DisplayMsg = (dataobj.getString("DisplayMsg"));


                        Log.d("", "response_SNA_TNA: " + values);

                        if (values.equalsIgnoreCase("BLANK")) {


                        } else if (values.equalsIgnoreCase("TNA")) {
                            TNA_Dialoge();


                        } else if (values.equalsIgnoreCase("SNA")) {




                            // list_foc.get(0).setSelectedItem(holder.spinner.getSelectedItem().toString());


                            // Toast.makeText(context, ""+list_foc.get(0).getSelectedItem(), Toast.LENGTH_SHORT).show();

                            holder.spinner.setSelection(0);
                            new GetSNA_AsynTask(Constants.SNA_url + "?ItemCode=" + str_item, "GET", context, new APIListner() {
                                @Override
                                public void onSuccess() {
                                    //  Foc_NO.add("--select--");
                                    SNA_Dialoge();
                                }
                                @Override
                                public void onErrors() {
                                }
                            }).execute();


                        }


                    }


                    apiListner.onSuccess();
                   // progDailog.dismiss();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                 Utils.showAlertBox("Something Went wrong!!", (Activity) context);
               // progDailog.dismiss();

            }
        }
////////////////////////////////////////////////////  TNA_Dialoge  /////////////////////////////////////////


        void TNA_Dialoge() {



            AlertDialog.Builder builder = new AlertDialog.Builder(
                    context);
            builder.setTitle("CMS");
            builder.setMessage(DisplayMsg);
            builder.setPositiveButton("Close",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {

                            flag = true;
                            // Toast.makeText(context, "Yes is clicked", Toast.LENGTH_LONG).show();
                        }
                    });
            builder.show();
        }

////////////////////////////////////////////////////  SNA_Dialoge  /////////////////////////////////////////

        void SNA_Dialoge() {





            Dialog dialog;
            dialog = new Dialog(context);

            dialog.setCancelable(false);
            dialog.setTitle("CMS");
            dialog.setContentView(R.layout.sna_row);

            Button btndialog = (Button) dialog.findViewById(R.id.button);
            TextView txt_msg =(TextView)dialog.findViewById(R.id.txt_msg);
            txt_msg.setText(DisplayMsg);
            btndialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });
            RecyclerView recyclerView = dialog.findViewById(R.id.sna_listview);
            SnaAdapter adapterRe = new SnaAdapter(context, sna_model);
            recyclerView.setAdapter(adapterRe);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

            recyclerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            dialog.show();

        }
    }


    class GetSNA_AsynTask extends CallManagerAsyncTaskArray {
        JSONArray postParamData = new JSONArray();
        APIListner apiListner;

        public GetSNA_AsynTask(String action, String reqType, Context context, APIListner apiListner) {
            super(action, reqType, context);
            this.apiListner = apiListner;
        }

        @Override
        protected JSONArray doInBackground(Object... params) {

            try {

                return doWorkJSONArray(postParamData);
            } catch (ConnectException e) {
                e.printStackTrace();
            } catch (EOFException e) {
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(JSONArray json) {
            super.onPostExecute(json);
            if (json != null) {
                Utils.Log("JSON response_SNADATA=========" + json.toString());
                try {
                    sna_model.clear();
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject dataobj = json.getJSONObject(i);

                        SNA_Model model = new SNA_Model();

                        model.setSubstituteNo(dataobj.getString("SubstituteNo_"));
                        model.setDescription(dataobj.getString("Description"));
                        String DisplayMsg = (dataobj.getString("Description"));

                        sna_model.add(model);
                        Log.d("", "onPostExecuteDisplayMsg: "+DisplayMsg);

                    }

                    apiListner.onSuccess();
                    //  progDailog.dismiss();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Utils.showAlertBox("Record Not Found!!", (Activity) context);
                // progDailog.dismiss();

            }
        }

    }



}
