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
import android.widget.ListView;
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

import static com.androidbuts.multispinnerfilter.SingleSpinner.builder;


public class FOC_ListAdapter extends RecyclerView.Adapter<FOC_ListAdapter.ViewHolder> {

    Context context;
   public static ArrayList<ModelClassForSavedData> list_foc = new ArrayList();
    String TNA;
    int Spinner_values;
    int Spinner_posstion;
    int i;
    String str_item;
    public static String qty_value;
    ProgressDialog progDailog;
    ArrayList<String> item = new ArrayList<>();
    AlertDialog.Builder builder;
    String DisplayMsg;
    String values;

    ArrayList<SNA_Model> sna_model = new ArrayList<>();


    //  SNA LIST

    Boolean  flag = false;

    ArrayList<Foc_list_Model> foc_list_models = new ArrayList<>();
    ArrayList<String> Foc_NO = new ArrayList<String>();


    ArrayList<String> plantsList = new ArrayList();


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton plus, minus;
        EditText step;
        TextView discription,SpinerItem;
        Spinner spinner;

        public ViewHolder(View itemView) {
            super(itemView);
            plus = (ImageButton) itemView.findViewById(R.id.plus);
            minus = (ImageButton) itemView.findViewById(R.id.minus_foc);
            step = (EditText) itemView.findViewById(R.id.step);
            spinner = (Spinner) itemView.findViewById(R.id.spinner1);
            SpinerItem =(TextView) itemView.findViewById(R.id.SpinerItem);

            discription = (TextView) itemView.findViewById(R.id.discription);

         /*   minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    try {
                        list_foc.remove(position);
                        notifyItemRemoved(position);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
            });*/


/*
            step.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    list_foc.set(getAdapterPosition(), new ModelClassForSavedData("",s.toString(),""));
                    qty_value = step.getText().toString();
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });*/
        }
    }


    public FOC_ListAdapter(ArrayList<ModelClassForSavedData> list_foc1,String tna, final Context context) {
        this.list_foc = list_foc1;
        this.context = context;
        this.TNA = tna;


        // call foc_asyntask method

        /*

         */


    }


    public ArrayList<ModelClassForSavedData> getList_foc() {
        return list_foc;
    }

    void setFoc_NO(ArrayList<String> foc_NO, ArrayList<Foc_list_Model> foc_list_models) {
        this.Foc_NO = foc_NO;
        this.foc_list_models = foc_list_models;
    }

    @Override
    public int getItemCount() {
        return list_foc.size();
    }


    @Override
    public FOC_ListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final FOC_ListAdapter.ViewHolder holder, final int possition) {


        if (possition == 0) {
            holder.minus.setVisibility(View.INVISIBLE);
        } else {
            holder.minus.setVisibility(View.VISIBLE);

        }
        if (possition == Foc_NO.size() - 2) {
            holder.plus.setVisibility(View.GONE);

        } else {
            holder.plus.setVisibility(View.VISIBLE);

        }


        plantsList = Foc_NO;
        int x = possition;
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                context, R.layout.simple_spinner_dropdown_item, plantsList);


        spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(spinnerArrayAdapter);

      /*  int x =possition;

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<String>(context, R.layout.simple_spinner_dropdown_item, Foc_NO);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(dataAdapter);
        //holder.spinner.setPrompt("--Select--");

*/
        holder.step.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (Integer.parseInt(s.toString()) == 0) {
                        holder.step.setText("");
                    } else {
                        list_foc.get(possition).setQty(s.toString());

                    }
                } catch (Exception e) {

                }
            }
        });

        //   if(list_foc != null ){
        if (list_foc.get(x).getQty().length() > 0 && list_foc.get(x).getDescription().length() > 0 && list_foc.get(x).getSelectedItem().length() > 0) {

           String str_item1 = (list_foc.get(x).getSelectedItem());

            String selected_item = str_item1.split("--")[0];
            Log.d("", "onItemSelectedItemSplit1234: "+selected_item);

            holder.spinner.setSelection(getIndex(holder.spinner, list_foc.get(x).getSelectedItem()));
            holder.step.setText(list_foc.get(x).getQty());
            holder.discription.setText(list_foc.get(x).getDescription());

            // holder.minus.setVisibility(View.VISIBLE);


            if (list_foc.size() == 1) {

                holder.minus.setVisibility(View.GONE);

            } else {
                holder.minus.setVisibility(View.VISIBLE);

            }


            //   holder.minus.setVisibility(View.VISIBLE);

        } else {
            holder.step.setText(null);
            holder.step.requestFocus();
        }

        holder.SpinerItem.setOnClickListener(new View.OnClickListener() {

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
            public void onItemSelected(AdapterView<?> parent, View view, int position1, long id) {

                holder.discription.setText(foc_list_models.get(position1).getDiscription());
                str_item = (holder.spinner.getSelectedItem().toString());


                // Toast.makeText(context, ""+Spinner_values, Toast.LENGTH_SHORT).show();

                String split_item = holder.spinner.getSelectedItem().toString();

                String selected_split_item = split_item.split("~")[0];
                holder.SpinerItem.setText(selected_split_item);
                Log.d("", "onItemSelected_selected_item: "+selected_split_item);

                item.add(str_item);
                list_foc.get(possition).setSelectedPosition(holder.spinner.getSelectedItemPosition());
                list_foc.get(possition).setSelectedItem(holder.spinner.getSelectedItem().toString());
                list_foc.get(possition).setDescription(holder.discription.getText().toString());

                if (selected_split_item.equalsIgnoreCase("-Select Item-")) {
                } else{

                    new FOC_Validation(holder, Constants.DocketValidationForPartRequest + "?ATMID=" + RepairDetailsActivity.atm_id + "&PartCode=" + selected_split_item, "GET", context, new APIListner() {
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

                    if (selected_split_item.equalsIgnoreCase("-Select Item-")) {


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

                TNA="";

                if (list_foc.size() < Foc_NO.size() - 1) {
                    try {
                        if (holder.discription.getText().toString().equalsIgnoreCase("")) {
                            Toast.makeText(context, "please enter Description", Toast.LENGTH_SHORT).show();
                        } else if (holder.step.getText().toString().equalsIgnoreCase("")) {

                            Toast.makeText(context, "please enter QTY", Toast.LENGTH_SHORT).show();


                        } else {

                            holder.minus.setVisibility(View.VISIBLE);

                            //    dataAdapter.remove((String)spinner.getSelectedItem());
                            list_foc.add(possition + 1, new ModelClassForSavedData("", "", ""));
                            notifyItemInserted(possition + 1);

                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // int position = getAdapterPosition();
                try {
                    holder.step.clearFocus();
                    holder.discription.clearFocus();
                    list_foc.remove(possition);
                    notifyItemRemoved(possition);
                    notifyDataSetChanged();

                    /*    if (list_foc.size()== 1) {
                            // notifyDataSetChanged();
                            holder.minus.setVisibility(View.GONE);

                        } else {
                            holder.minus.setVisibility(View.VISIBLE);

                        }*/


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });






        /*if(list_foc.get(x).length() > 0) {
            holder.step.setText(list_foc.get(x));
        }
        else{
            holder.step.setText(null);
            holder.step.requestFocus();
        }

    }


    public ArrayList<String> getStepList(){
        return list_foc;
    }*/

    }


    public ArrayList<ModelClassForSavedData> getStepList() {
        return list_foc;
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class FOC_Validation extends CallManagerAsyncTaskArray {
        JSONArray postParamData = new JSONArray();
        APIListner apiListner;
        ViewHolder holder;


        public FOC_Validation( ViewHolder holder,String action, String reqType, Context context, APIListner apiListner) {
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
                          //  Toast.makeText(context, "Allow", Toast.LENGTH_SHORT).show();
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
/*
            progDailog = new ProgressDialog(context);
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
               //  Utils.showAlertBox("Something Went wrong!!", (Activity) context);
              //  progDailog.dismiss();

            }
        }
////////////////////////////////////////////////////  TNA_Dialoge  /////////////////////////////////////////


        void TNA_Dialoge() {

            flag = true;

            AlertDialog.Builder builder = new AlertDialog.Builder(
                    context);
            builder.setTitle("CMS");
            builder.setMessage(DisplayMsg);
            builder.setPositiveButton("Close",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
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
/*
            progDailog = new ProgressDialog(context);
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








