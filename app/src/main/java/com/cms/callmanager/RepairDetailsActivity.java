package com.cms.callmanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cms.callmanager.Foc_Chargeble.APIListner;
import com.cms.callmanager.Foc_Chargeble.FOC_CHARGE_Activity;
import com.cms.callmanager.Foc_Chargeble.FOC_ListAdapter;
import com.cms.callmanager.Foc_Chargeble.Foc_list_Model;
import com.cms.callmanager.Foc_Chargeble.ModelClassForSavedData;
import com.cms.callmanager.Foc_Chargeble.ModelClassForSavedData_Charge;
import com.cms.callmanager.Foc_Chargeble.SNA_Model;
import com.cms.callmanager.adapter.CustomeSpinnerAdapter;
import com.cms.callmanager.adapter.Open_Call_Details_Adapter;
import com.cms.callmanager.constants.Constant;
import com.cms.callmanager.constants.Constants;
import com.cms.callmanager.dto.ProblemFixDTO;
import com.cms.callmanager.dto.ResonseCategeoryDTO;
import com.cms.callmanager.dto.SolutionDTO;
import com.cms.callmanager.model.Alert_model;
import com.cms.callmanager.model.Open_Call_Details_model;
import com.cms.callmanager.multispinner.CallClloserActivity;
import com.cms.callmanager.multispinner.QuestionairActivity;
import com.cms.callmanager.multispinner.clousermodel.ImageModel;
import com.cms.callmanager.services.CallManagerAsyncTask;
import com.cms.callmanager.services.CallManagerAsyncTaskArray;
import com.cms.callmanager.utils.ProgressUtil;
import com.cms.callmanager.utils.Utils;
import com.cms.callmanager.utils.Validation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import static com.cms.callmanager.constants.Constant.UserId;

/**
 * Created by Monika zogato on 7/1/17.
 */
public class RepairDetailsActivity extends AppCompatActivity {

    private Spinner spinnerCategory, spinnerSparesUsed,statusSpinnerCategory, spinnerSolution, spinnerProblemFix;
    Button saveRepairDetails;
    // public  static Spinner  statusSpinnerCategory;
    EditText edtTxtTicketNumber, edtTxtDistance, edtTxtComment;
    SharedPreferences preferences;
    Toolbar toolbar;
    public static String FallInValidation;
    CustomeSpinnerAdapter spinnerAdapter = null;
    List<ResonseCategeoryDTO.PayLoad> responseCategoryoadList = null;
    List<SolutionDTO.PayLoad> solutionLoadList = null;
    List<ProblemFixDTO.PayLoad> problemLoadList = new ArrayList<>();
    ArrayList<String> searchField = new ArrayList<String>();
    ArrayList<String> ArraySubstatusId = new ArrayList<String>();


    int status_possiton;
   public static String userId;
    /*= {"Select Status", "Engineer Reached", "Engineer Started", "Repair Started", "Repair Completed", "Hold"};
     */


    // vishnu
    String str_jsonArray_image;
    String str_jsonArray_foc;
    String str_jsonArray_charge;
    private ProgressDialog progDailog;


    String Status_values;
    JSONArray jsonArray_foc;
    JSONArray jsonArray_image;
    JSONArray jsonArray_charge;
    Bundle bundle;

    final int LIST_REQUEST = 1;
    final int LIST_RESULT = 100;
    String key = null;
    ArrayList<ModelClassForSavedData> list;
    ArrayList<ModelClassForSavedData_Charge> list1;
   public static String atm_id;
   public static String docket_no;
    public static String call_types;
    Button btn_view;
    public static int selectedColor = 0;
    String subStatusId;

    LinearLayout spinnerStatusLayout;
    CheckBox checkboxEngineerOnSite, checkboxSparesUsed, checkboxUnproductive, checkboxReturnVisit;
    private Button btn_view_callClosour;


    String followupError;
    private String saveCallErrorMsg;
    private String CallImageCallErrorMsg;
    private String IdealHrsErrorMsg;
    private String questionErrormsg;
    private String Foc_errorMsg;
    private String ImageChargeErrormsg;
    private String ChargableDataErrormsg;
    TextView  open_call;
     ArrayList<Open_Call_Details_model> Open_Call  = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_details);

       /* toolbar       = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.action_back);
        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        spinnerProblemFix = (Spinner) findViewById(R.id.spinnerProblem);
        spinnerSolution = (Spinner) findViewById(R.id.spinnerSolution);
        statusSpinnerCategory = (Spinner) findViewById(R.id.statusSpinnerCategory);
        saveRepairDetails = (Button) findViewById(R.id.save_repair_detail);
        edtTxtTicketNumber = (EditText) findViewById(R.id.edtTxtTicketNumber);
        edtTxtDistance = (EditText) findViewById(R.id.edtTxtDistance);
        edtTxtComment = (EditText) findViewById(R.id.edtTxtComment);
        checkboxEngineerOnSite = (CheckBox) findViewById(R.id.checkboxEngineerOnSite);
        checkboxSparesUsed = (CheckBox) findViewById(R.id.checkboxSparesUsed);
        checkboxUnproductive = (CheckBox) findViewById(R.id.checkboxUnproductive);
        checkboxReturnVisit = (CheckBox) findViewById(R.id.checkboxReturnVisit);
        spinnerStatusLayout = (LinearLayout) findViewById(R.id.spinnerStatusLayout);
        btn_view =(Button)findViewById(R.id.view);
        btn_view_callClosour =(Button)findViewById(R.id.view_Closour);
          open_call =(TextView)findViewById(R.id.open_call);


        Prefs.remove(Constant.FCRAttachment);
        Prefs.remove(Constant.InstallationCertificate);
        Prefs.remove(Constant.ATMImages1);
        Prefs.remove(Constant.ATMImages2);
        Prefs.remove(Constant.TransactionImage1);
        Prefs.remove(Constant.TransactionImage2);
        Prefs.remove(Constant.ErrorHistory);











        searchField.add("Select");
        ArraySubstatusId.add("");
        ArrayAdapter<String> searchFieldAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, searchField);
        statusSpinnerCategory.setAdapter(searchFieldAdapter);

    /*    StatusAdapter adapter = new StatusAdapter(RepairDetailsActivity.this, searchField);
       // spColors = (Spinner) findViewById(R.id.spColors);
        statusSpinnerCategory.setAdapter(adapter);
*/

        if (getIntent() != null && getIntent().getStringExtra("docketNo") != null) {

            docket_no =(getIntent().getStringExtra("docketNo"));
            edtTxtTicketNumber.setText(getIntent().getStringExtra("docketNo"));
            edtTxtTicketNumber.setEnabled(false);
        }
        if (getIntent() != null && getIntent().getStringExtra("ATM_id") != null) {
            atm_id = (getIntent().getStringExtra("ATM_id"));
            edtTxtTicketNumber.setEnabled(false);
        }

        if (getIntent() != null && getIntent().getStringExtra("CALL_TYPES") != null) {
            call_types = (getIntent().getStringExtra("CALL_TYPES"));
            // edtTxtTicketNumber.setEnabled(false);
        }

        Log.d("", "docket_no: "+ docket_no);
        Log.d("", "call_types123: "+ call_types);





        //       atm_id
        //Prefs.with(RepairDetailsActivity.this).getString(UserId, "")

        new GetOpenCall(Constants.GetOpenCallOfaEng + "?ATMID="+atm_id+"&EngineerID="+Prefs.with(RepairDetailsActivity.this).getString(UserId, "")+"&DocketNo="+docket_no, "GET", this, new APIListner() {
            @Override
            public void onSuccess() {
            }
            @Override
            public void onErrors() {
            }
        }).execute();



       /* RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv_rd);
        Open_Call_Details_Adapter adapter = new Open_Call_Details_Adapter(Open_Call);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
       */ // Open_Call  = new ArrayList<>();

        findViewById(R.id.open_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (recyclerView.getVisibility() == View.GONE) {

                    recyclerView.setVisibility(View.VISIBLE);
                } else{
                    recyclerView.setVisibility(View.GONE);

                }*/
            }


        });




        btn_view_callClosour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RepairDetailsActivity.this, CallClloserActivity.class);
                intent.putExtra("CALL_TYPE_VALUES",call_types );

                startActivity(intent);
            }
        });


        //  Toast.makeText(this, "" + atm_id, Toast.LENGTH_SHORT).show();
        spinnerCategory.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        // statusSpinnerCategory.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        checkboxUnproductive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn_view_callClosour.setVisibility(View.GONE);

                    spinnerStatusLayout.setVisibility(View.VISIBLE);



                    if(Status_values != "TSS APPROVAL PENDING" || Status_values != "Select")
                    {
                        //  btn_view_callClosour.setVisibility(View.VISIBLE);
                    } else {
                        btn_view_callClosour.setVisibility(View.GONE);
                    }





                    try {
                        if(list != null  || list.size() > 0 ){
                            btn_view.setVisibility(View.VISIBLE);

                        }else{
                            btn_view.setVisibility(View.GONE);

                        }


                        // if(list1.size() > 0|| list1 != null)
                        if(jsonArray_charge.length() > 0|| jsonArray_charge != null)
                        {
                            btn_view_callClosour.setVisibility(View.VISIBLE);
                        }else{
                            btn_view_callClosour.setVisibility(View.GONE);

                        }

                    }catch (Exception e){}


                    //  view.setVisibility(View.VISIBLE);
                } else {
                    spinnerStatusLayout.setVisibility(View.GONE);
                    btn_view.setVisibility(View.GONE);
                    btn_view_callClosour.setVisibility(View.VISIBLE);
                }

            }
        });


        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(RepairDetailsActivity.this, FOC_CHARGE_Activity.class);

                if (list == null || list1 == null) {
                    list = new ArrayList<>();
                    list1 = new ArrayList<>();
                }
                i.putExtra("list", list);
                i.putExtra("list1", list1);
                i.putExtra("ATM_ID", atm_id);
                i.putExtra("DOCKET_NO",docket_no );
                i.putExtra("subStatusId",subStatusId);
                i.putExtra("TNA","TNA_values");

                startActivityForResult(i, LIST_REQUEST);


            }
        });

        statusSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                selectedColor = position;

                statusSpinnerCategory.setSelection(position);

                status_possiton =  statusSpinnerCategory.getSelectedItemPosition();



                Status_values = statusSpinnerCategory.getItemAtPosition(position).toString();
                Log.d("", "onItemSelected_status_possiton: "+Status_values);

                subStatusId = ArraySubstatusId.get(position);



                Log.d("", "ArraySubstatusId: "+ subStatusId);


                //  Toast.makeText(RepairDetailsActivity.this, Status_values, Toast.LENGTH_SHORT).show();
                if(statusSpinnerCategory.getItemAtPosition(position).toString() != "Select") {

                    if ("TSS APPROVAL PENDING".equalsIgnoreCase(Status_values)) {


                        Intent i = new Intent(RepairDetailsActivity.this, FOC_CHARGE_Activity.class);

                        if (list == null || list1 == null) {
                            list = new ArrayList<>();
                            list1 = new ArrayList<>();
                        }
                        i.putExtra("list", list);
                        i.putExtra("list1", list1);
                        i.putExtra("ATM_ID", atm_id);
                        i.putExtra("DOCKET_NO", docket_no);
                        i.putExtra("subStatusId", subStatusId);

                        startActivityForResult(i, LIST_REQUEST);

                        btn_view_callClosour.setVisibility(View.GONE);

                    } else {

                        try {
                            if (list == null || list1 == null) {
                                btn_view.setVisibility(View.GONE);

                            } else {
                                btn_view.setVisibility(View.VISIBLE);

                            }
                            //******** added by vibha
                           /* btn_view_callClosour.setVisibility(View.GONE);
                            Intent intent = new Intent(RepairDetailsActivity.this, CallClloserActivity.class);
                            intent.putExtra("CALL_TYPE_VALUES",call_types );
                            startActivity(intent);*/
                        } catch (Exception e) {

                        }


                        btn_view.setVisibility(View.GONE);

                    }
                }else{
                    btn_view_callClosour.setVisibility(View.GONE);

                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinnerSolution.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerProblemFix.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (problemLoadList != null && problemLoadList.size() > 0) {
                    ProblemFixDTO.PayLoad payLoad = problemLoadList.get(i);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });






        new ValidationPartRequest(Constants.ValidationPartRequest + "?DocketNo=" + docket_no, "GET", this, new APIListner() {
            @Override
            public void onSuccess() {
                //  Foc_NO.add("--select--");
            }
            @Override
            public void onErrors() {
            }
        }).execute();




        saveRepairDetails.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.d("", "onCreate_Open_Call: "+Open_Call_Details_Adapter.jsonArrayfor_OpenCallDetail);

                if(Open_Call_Details_Adapter.jsonArrayfor_OpenCallDetail != null){

                    new SendOpenCallDetail(Constants.SendOpenCallDetail, "POST"
                            , RepairDetailsActivity.this).execute();
                }






                if (getIntent() != null && getIntent().getStringExtra("docketNo") != null) {

                    ProblemFixDTO.PayLoad payLoad = (ProblemFixDTO.PayLoad) spinnerProblemFix.getSelectedItem();
                    String payLoadId = payLoad.getCode();

                    SolutionDTO.PayLoad sPayLoad = (SolutionDTO.PayLoad) spinnerSolution.getSelectedItem();
                    String sPayLoadId = sPayLoad.getCode();

                    ResonseCategeoryDTO.PayLoad rCPayLoad = (ResonseCategeoryDTO.PayLoad) spinnerCategory.getSelectedItem();
                    String rCPayLoadId = rCPayLoad.getCode();

                    JSONObject postParamData = new JSONObject();
                    preferences = getSharedPreferences("CMS", Context.MODE_PRIVATE);
                    userId = preferences.getString("userId", null);
                    Log.d("", "onClickUserId: "+userId);

                    try {
                        postParamData.put("Docket_No", getIntent().getStringExtra("docketNo"));
                        postParamData.put("Problem_Fix", payLoadId);
                        postParamData.put("Solution", sPayLoadId);

                        //      postParamData.put("responsecategory", rCPayLoadId); prob fix, sol, response
                        if (statusSpinnerCategory.getSelectedItem().toString() != null &&
                                !statusSpinnerCategory.getSelectedItem().toString().equals("Select")) {
                            postParamData.put("Follow_up_Status", statusSpinnerCategory.getSelectedItem().toString());
                            postParamData.put("Action_Taken", statusSpinnerCategory.getSelectedItem().toString());
                        } else {
                            postParamData.put("Follow_up_Status", "REPAIR DETAILS");
                            postParamData.put("Action_Taken", "REPAIR COMPLETED");
                        }
                        postParamData.put("Last_modified_By", userId);

                        if (spinnerCategory.getSelectedItem().toString() != null &&
                                !spinnerCategory.getSelectedItem().toString().equals("Select Category")) {
                            postParamData.put("Response_Category", rCPayLoadId);
                        } else {
                            postParamData.put("Response_Category", "");
                        }
                        postParamData.put("Distance", "");
                        if (checkboxEngineerOnSite.isChecked()) {
                            postParamData.put("Engineer_On_Site", "1");
                        } else {
                            postParamData.put("Engineer_On_Site", "0");
                        }
                        if (checkboxSparesUsed.isChecked()) {
                            postParamData.put("Spare_Call_1", "1");
                        } else {
                            postParamData.put("Spare_Call_1", "0");
                        }
                        if (checkboxReturnVisit.isChecked()) {
                            postParamData.put("Return_Visit", "1");
                        } else {
                            postParamData.put("Return_Visit", "0");
                        }
                       /* if (checkboxUnproductive.isChecked()) {

                            Utils.Log("In checked-----------");
                            postParamData.put("Un_Productive_Visit", "1");
                            if (statusSpinnerCategory.getSelectedItem().toString() != null) {
                                Utils.Log("======" + statusSpinnerCategory.getSelectedItem().toString());
                                postParamData.put("Sub_Status_Name", statusSpinnerCategory.getSelectedItem().toString());



                                // ===================== Call FOC_SAVE API Serices ===========================

                                if ("TSS APPROVAL PENDING".equalsIgnoreCase(Status_values)) {

                                    if (key.equalsIgnoreCase("foc")) {

                                        new Save_foc_list_asyntask(Constants.SAVE_FOC_URL, "POST"
                                                , RepairDetailsActivity.this).execute();
                                    } else {

                                     //   if( userId != null) {
                                            new Save_charge_images_asyntask(Constants.SAVE_CHARGE_IMAGES_URL + " ?userId=" + userId, "POST"
                                                    , RepairDetailsActivity.this).execute();
                                     *//*   }else{

                                        }*//*


                                        new Save_charge_list_asyntask(Constants.SAVE_CHARGE_URL, "POST"
                                                , RepairDetailsActivity.this).execute();
                                    }
                                }else{

                                }


                            }
                        } else {
                            Utils.Log("In Unchecked-----------");
                            postParamData.put("Un_Productive_Visit", "0");
                            postParamData.put("Sub_Status_Name", "REPAIR COMPLETED");
                        }
*/
                        postParamData.put("Follow_up_Status", "repair details");

                        if (edtTxtDistance.getText() != null) {
                            postParamData.put("Distance", edtTxtDistance.getText().toString());
                        }
                        if (edtTxtComment.getText() != null) {
                            postParamData.put("Comments", edtTxtComment.getText().toString());
                            postParamData.put("Action_Taken", edtTxtComment.getText().toString());

                            postParamData.put("Latitude", Prefs.with(RepairDetailsActivity.this).getString(Constant.LATI, ""));
                            postParamData.put("Longitude", Prefs.with(RepairDetailsActivity.this).getString(Constant.LONGI, ""));
                            postParamData.put("Mobile_Device_Id", Prefs.with(RepairDetailsActivity.this).getString(Constant.DEVICE_ID, ""));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                    postParamData.put("problemfix", payLoadId);
                    //                  postParamData.put("solution", sPayLoadId);

                  /*  if (payLoadId.equalsIgnoreCase("") || sPayLoadId.equalsIgnoreCase("-1") || rCPayLoadId.equalsIgnoreCase("-1")) {
                        Utils.showAlertBox("Please Enter All Values", RepairDetailsActivity.this);
                    } else {*/
                        /*new RepairDetailsAsyncTask(Constants.FOLLOWUP, "POST"
                                , RepairDetailsActivity.this, postParamData).execute();*/

                    ///////////////////////////////////////////////////////////////
                    try {
                        if (checkboxUnproductive.isChecked()) {

                            Utils.Log("In checked-----------");
                            postParamData.put("Un_Productive_Visit", "1");

                            Log.d("", "onClickVALUESSSS: " + statusSpinnerCategory.getSelectedItem().toString());
                            if (statusSpinnerCategory.getSelectedItem().toString() != null) {
                                if (statusSpinnerCategory.getSelectedItem().toString().equalsIgnoreCase("TSS APPROVAL PENDING") && list1.size() > 0) {
                                    if (key.equalsIgnoreCase("charge")) {
                                        postParamData.put("Sub_Status_Name", "COMPLETED");
                                    } else {
                                        postParamData.put("Sub_Status_Name", statusSpinnerCategory.getSelectedItem().toString());

                                    }
                                } else {
                                    Utils.Log("======" + statusSpinnerCategory.getSelectedItem().toString());
                                    postParamData.put("Sub_Status_Name", statusSpinnerCategory.getSelectedItem().toString());
                                }

                            }
                        } else {
                            Utils.Log("In Unchecked-----------");
                            postParamData.put("Un_Productive_Visit", "0");
                            postParamData.put("Sub_Status_Name", "REPAIR COMPLETED");
                        }
                    } catch (Exception e) {
                    }
                    if (checkboxUnproductive.isChecked()) {
                        if (statusSpinnerCategory.getSelectedItem().toString().equals("TSS APPROVAL PENDING") && list1.size() > 0) {
                            if (CallClloserActivity.ImagesJsonArray != null) {
                                new RepairDetailsAsyncTask(Constants.FOLLOWUP, "POST"
                                        , RepairDetailsActivity.this, postParamData).execute();
                            } else {
                                Utils.showAlertBox("Please Enter values for Call Closure by click Call Closure Details", RepairDetailsActivity.this);
                            }

                        } else if (statusSpinnerCategory.getSelectedItem().toString().equals("TSS APPROVAL PENDING") && list.size() > 0) {
                            new RepairDetailsAsyncTask(Constants.FOLLOWUP, "POST"
                                    , RepairDetailsActivity.this, postParamData).execute();

                        } else if (!statusSpinnerCategory.getSelectedItem().toString().equals("Select") && !statusSpinnerCategory.getSelectedItem().toString().equals("TSS APPROVAL PENDING")) {
                              /*  if(CallClloserActivity.CallClosurejson !=null)
                                {*/
                            new RepairDetailsAsyncTask(Constants.FOLLOWUP, "POST"
                                    , RepairDetailsActivity.this, postParamData).execute();
                                /*}else
                                {
                                    Utils.showAlertBox("Please Enter values for Call Closure by click Call Closure Details",RepairDetailsActivity.this);
                                }*/
                        } else {
                            Utils.showAlertBox("Please Select SubStatus", RepairDetailsActivity.this);
                        }
                    } else {

                        if(FallInValidation.equalsIgnoreCase("Allow")) {

                            if (CallClloserActivity.ImagesJsonArray != null) {
                                new RepairDetailsAsyncTask(Constants.FOLLOWUP, "POST"
                                        , RepairDetailsActivity.this, postParamData).execute();
                            } else {
                                Utils.showAlertBox("Please Enter values for Call Closure by click Call Closure Details", RepairDetailsActivity.this);
                            }
                        }else{

                            if( CallClloserActivity.ImagesJsonArray != null) {


                                Log.d("TAG", "onClick_ImagesJsonArray: "+CallClloserActivity.ImagesJsonArray);

                                ValidationPartRequest_UploadFileCallClosour uploadFileCallClosour = new ValidationPartRequest_UploadFileCallClosour(Constants.SAVE_CALL_CLOSOUR_IMAGES + "?UserId=" + userId, "POST", CallClloserActivity.ImagesJsonArray, RepairDetailsActivity.this);
                                uploadFileCallClosour.execute();

                            }else {
                                //  Utils.showAlertBox("Please Enter values for Call Closure by click Call Closure Details", RepairDetailsActivity.this);


                            }


                        }
                        //   Utils.showAlertBox("Please Enter values for Call Closure by click Call Closure Details", RepairDetailsActivity.this);

                    }


                    // }
                }

               /* }else {



                    if( CallClloserActivity.ImagesJsonArray != null) {


                        Log.d("TAG", "onClick_ImagesJsonArray: "+CallClloserActivity.ImagesJsonArray);


                        ValidationPartRequest_UploadFileCallClosour uploadFileCallClosour = new ValidationPartRequest_UploadFileCallClosour(Constants.SAVE_CALL_CLOSOUR_IMAGES + "?UserId=" + userId, "POST", CallClloserActivity.ImagesJsonArray, RepairDetailsActivity.this);
                        uploadFileCallClosour.execute();
                    }else {

                    }

                }*/
            }
        });






        new GetCallClosureImages(Constants.GetCallCloserImages + "?DocketNo=" + RepairDetailsActivity.docket_no, "GET"
                , RepairDetailsActivity.this).execute();


        new SubstatusAsyncTask(Constants.SUBSTATUS, "GET"
                , RepairDetailsActivity.this).execute();


    }


    public class ValidationPartRequest_UploadFileCallClosour extends CallManagerAsyncTask {

        String comment,uploadedFile;
        JSONArray callClosour_images;

        public ValidationPartRequest_UploadFileCallClosour(String action, String reqType, JSONArray CallClosour_images, Context context){
            super(action, reqType, context);
            this.callClosour_images = CallClosour_images;

            // this.uploadedFile = uploadedFile;

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            JSONObject postParamData = new JSONObject();
            try {
              /*  postParamData.put("DocketNo",docketNo);
                postParamData.put("File",uploadedFile);
                postParamData.put("FileName",userId+String.valueOf(System.currentTimeMillis())+".jpg");
                //  postParamData.put("FileName","");

                postParamData.put("Comments",comment);*/
                return doWorkJSONArray(callClosour_images);
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
            //  ProgressUtil.showProgressBar(RepairDetailsActivity.this,
            //        findViewById(R.id.root), R.id.progressBar);

            progDailog = new ProgressDialog(RepairDetailsActivity.this);
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();

        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            //ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if(json != null){
                Utils.Log("JSON Response========="+json.toString());
                try {
                    if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")){
                        /*Intent i = new Intent(AttachImageActivity.this, AcknowledgementActivity.class);
                        i.putExtra("docketNo",docketNo);
                        startActivity(i);
                        finish();*/
                        progDailog.dismiss();
                        CallImageCallErrorMsg = json.getString("ErrorMessage").toString();
                        //   Toast.makeText(RepairDetailsActivity.this, json.getString("ErrorMessage").toString(), Toast.LENGTH_LONG).show();
                        //  Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);




                        //  progDailog.dismiss();

                        //  Toast.makeText(RepairDetailsActivity.this, "ImagesUpload", Toast.LENGTH_SHORT).show();
                        showAlertBox1("Image Upload : "+ CallImageCallErrorMsg + " System does not allow to close the docket because some part request does not fully proccess." +
                                "So, system only save the selected  images for closing  details",RepairDetailsActivity.this);






                    }else if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        //  Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);
                        CallImageCallErrorMsg = json.getString("ErrorMessage").toString();
                        progDailog.dismiss();
                        //showAlertBox1(" Image Upload : "+ CallImageCallErrorMsg,RepairDetailsActivity.this);
                        showAlertBox1("Image Upload : "+ CallImageCallErrorMsg + " System does not allow to close the docket because some part request does not fully proccess." +
                                "So, system only save the selected  images for closing  details",RepairDetailsActivity.this);


                        // Utils.showAlertBox("Follow up : "+followupError+"\n Call Closure : "+saveCallErrorMsg +"\n Image Upload : "+ CallImageCallErrorMsg,RepairDetailsActivity.this);
                    }else {
                        CallImageCallErrorMsg = json.getString("ErrorMessage").toString();
                        progDailog.dismiss();
                        // showAlertBox1("Image Upload : "+ CallImageCallErrorMsg,RepairDetailsActivity.this);
                        showAlertBox1("Image Upload : "+ CallImageCallErrorMsg + " System does not allow to close the docket because some part request does not fully proccess." +
                                "So, system only save the selected  images for closing  details",RepairDetailsActivity.this);


                        //  Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                progDailog.dismiss();
                Utils.showAlertBox("Something Went wrong!!",RepairDetailsActivity.this);
               /* Intent i = new Intent(CallClloserActivity.this,CallClloserActivity.class);
                startActivity(i);*/
            }
        }
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RepairDetailsActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //  return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                //finish();
                onBackPressed();
                break;
        }
        return true;
    }

    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        String firstItem = String.valueOf(spinnerCategory.getSelectedItem());

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if (firstItem.equals(String.valueOf(spinnerCategory.getSelectedItem()))) {
                // ToDo when first item is selected
            } else {
               /* Toast.makeText(parent.getContext(),
                        "You have selected : " + parent.getItemAtPosition(pos).toString(),
                        Toast.LENGTH_SHORT).show();*/
            }


        }

        @Override
        public void onNothingSelected(AdapterView<?> arg) {

        }
    }

    private void showRepairCompleteAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RepairDetailsActivity.this);

// set title
        alertDialogBuilder.setTitle("CMS");

// set dialog message
        alertDialogBuilder
                .setMessage("Do you want to attach the file?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(RepairDetailsActivity.this, AttachImageActivity.class);
                        i.putExtra("docketNo", edtTxtTicketNumber.getText().toString());
                        startActivity(i);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent i = new Intent(RepairDetailsActivity.this, HomeActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();

// show it
        alertDialog.show();
    }

    private class RepairDetailsAsyncTask extends CallManagerAsyncTask {

        JSONObject postParamData = new JSONObject();

        public RepairDetailsAsyncTask(String action, String reqType, Context context
                , JSONObject postParamData) {
            super(action, reqType, context);
            this.postParamData = postParamData;
        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            try {

                return doWork(postParamData);
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

            progDailog = new ProgressDialog(RepairDetailsActivity.this);
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            if (json != null) {
                Utils.Log("JSON Response=========" + json.toString());
                try {
                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")) {

                        followupError = json.getString("ErrorMessage");



                        if(checkboxUnproductive.isChecked())
                        {

                            if ("TSS APPROVAL PENDING".equalsIgnoreCase(Status_values)) {
                                if(key != null)
                                {
                                    if (key.equalsIgnoreCase("foc")) {

                                        // ============== FOC SAVE=====
                                        new Save_foc_list_asyntask(Constants.SAVE_FOC_URL, "POST"
                                                , RepairDetailsActivity.this).execute();
                                    } else {

                                        /*new Save_charge_list_asyntask(Constants.SAVE_CHARGE_URL, "POST"
                                                , RepairDetailsActivity.this).execute();*/

                                        UploadFileChargeImage uploadFileChargeImage = new UploadFileChargeImage(Constants.SAVE_CHARGE_IMAGES_URL +"?userId=" + userId , "POST", FOC_CHARGE_Activity.jsonArray_for_images, RepairDetailsActivity.this);
                                        uploadFileChargeImage.execute();

                                    }
                                }

                            } else {

                                progDailog.dismiss();
                                showAlertBox1(json.getString("ErrorMessage").toString(), RepairDetailsActivity.this);

                            }
                        }else {  // unchecked


                            if(FallInValidation.equalsIgnoreCase("Allow")) {



                                if(CallClloserActivity.ImagesJsonArray != null)
                                {
                                    if( CallClloserActivity.ImagesJsonArray.length() > 0)
                                    {
                                        UploadFileCallClosour uploadFileCallClosour = new UploadFileCallClosour(Constants.SAVE_CALL_CLOSOUR_IMAGES + "?UserId="+userId , "POST", CallClloserActivity.ImagesJsonArray, RepairDetailsActivity.this);
                                        uploadFileCallClosour.execute();
                                    }

                                }


                            }else {
                                if( CallClloserActivity.ImagesJsonArray != null) {


                                    Log.d("TAG", "onClick_ImagesJsonArray: "+CallClloserActivity.ImagesJsonArray);

                                    ValidationPartRequest_UploadFileCallClosour uploadFileCallClosour = new ValidationPartRequest_UploadFileCallClosour(Constants.SAVE_CALL_CLOSOUR_IMAGES + "?UserId=" + userId, "POST", CallClloserActivity.ImagesJsonArray, RepairDetailsActivity.this);
                                    uploadFileCallClosour.execute();

                                }else {


                                }
                            }




                            //    showRepairCompleteAlert();
                        }



                    } else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        progDailog.dismiss();
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), RepairDetailsActivity.this);
                    } /*else {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), RepairDetailsActivity.this);
                    }*/
                } catch (JSONException e) {
                    progDailog.dismiss();
                    e.printStackTrace();
                }
            } else {
                progDailog.dismiss();
                Utils.showAlertBox("Something Went wrong!!", RepairDetailsActivity.this);
            }
        }

/*        private void showRepairCompleteAlert() {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RepairDetailsActivity.this);

// set title
            alertDialogBuilder.setTitle("CMS");

// set dialog message
            alertDialogBuilder
                    .setMessage("Do you want to attach the file?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(RepairDetailsActivity.this, AttachImageActivity.class);
                            i.putExtra("docketNo", edtTxtTicketNumber.getText().toString());
                            startActivity(i);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent i = new Intent(RepairDetailsActivity.this, HomeActivity.class);
                            startActivity(i);

                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();

// show it
            alertDialog.show();
        }*/

    }

    class ValidationPartRequest extends CallManagerAsyncTaskArray {
        JSONArray postParamData = new JSONArray();
        APIListner apiListner;

        public ValidationPartRequest(String action, String reqType, Context context, APIListner apiListner) {
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
                Utils.Log("JSON docket_noValidationPartRequest=========" + json.toString());
                try {
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject dataobj = json.getJSONObject(i);


                        FallInValidation = (dataobj.getString("FallInValidation"));

                        Log.d("", "FallInValidation: "+FallInValidation);

                    }

                    apiListner.onSuccess();
                    //  progDailog.dismiss();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Utils.showAlertBox("Record Not Found!!", RepairDetailsActivity.this);
                // progDailog.dismiss();

            }
        }

    }


    private class SubstatusAsyncTask extends CallManagerAsyncTask {
        JSONObject postParamData = new JSONObject();

        public SubstatusAsyncTask(String action, String reqType, Context context
        ) {
            super(action, reqType, context);

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            try {

                return doWork(postParamData);
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
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            if (json != null) {
                Utils.Log("JSON Response=========" + json.toString());
                try {
                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")) {
                        if (json.getJSONArray("PayLoad").get(0).toString().equalsIgnoreCase("No record(s) to display.")) {
                            Utils.Log(json.getJSONArray("PayLoad").get(0).toString());
                            Utils.showAlertBox(json.getJSONArray("PayLoad").get(0).toString(), RepairDetailsActivity.this);
                        } else {
                            JSONArray respSubstatusList = json.getJSONArray("PayLoad");

                            for (int i = 0; i < respSubstatusList.length(); i++) {
                                JSONObject jsonData = (JSONObject) respSubstatusList.get(i);
                                if (jsonData.get("DisplayToCustomer").toString().equalsIgnoreCase("1") &&
                                        jsonData.getString("SubStatus") != null && !jsonData.getString("SubStatus").toString().equalsIgnoreCase("")) {
                                    searchField.add(jsonData.get("SubStatus").toString());

                                    subStatusId = jsonData.get("SubStatusId").toString();
                                    Log.d("", "onPostExecute_substatusID: "+subStatusId);

                                    ArraySubstatusId .add( jsonData.get("SubStatusId").toString());
                                    Log.d("", "onPostExecute__SubStatusId: "+ subStatusId);

                                }
                            }
                        }

                        new ResponseCategeoryTask(Constants.ResponseCategory,
                                "GET", RepairDetailsActivity.this, 0).execute();
                    } else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), RepairDetailsActivity.this);
                    } else {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), RepairDetailsActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.showAlertBox("Something Went wrong!!", RepairDetailsActivity.this);
            }
        }

    }








    private class ResponseCategeoryTask extends CallManagerAsyncTask {
        JSONObject postParamData = new JSONObject();
        int flag = -1;

        public ResponseCategeoryTask(String action, String reqType, Context context, int flag
        ) {
            super(action, reqType, context);
            this.flag = flag;
            ProgressUtil.showProgressBar(RepairDetailsActivity.this,
                    findViewById(R.id.root), R.id.progressBar);

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            try {

                return doWork(postParamData);
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
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if (json != null) {
                Utils.Log("JSON Response=========" + json.toString());
                try {

                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")) {
                        if (json.getJSONArray("PayLoad").get(0).toString().equalsIgnoreCase("No record(s) to display.")) {
                            Utils.Log(json.getJSONArray("PayLoad").get(0).toString());
                            Utils.showAlertBox(json.getJSONArray("PayLoad").get(0).toString(), RepairDetailsActivity.this);
                        } else {
                            JSONArray respSubstatusList = json.getJSONArray("PayLoad");
                            if (flag == 0) {
                                responseCategoryoadList = new ArrayList<>();

                                ResonseCategeoryDTO.PayLoad payLoad = new ResonseCategeoryDTO.PayLoad();
                                payLoad.setCode("-1");
                                payLoad.setDisplayToCustomer(-1);
                                payLoad.setName("--Select--");
                                responseCategoryoadList.add(payLoad);

                                for (int i = 0; i < respSubstatusList.length(); i++) {
                                    JSONObject jsonData = (JSONObject) respSubstatusList.get(i);
                                    payLoad = new ResonseCategeoryDTO.PayLoad();
                                    payLoad.setCode(jsonData.getString("Code"));
                                    payLoad.setDisplayToCustomer(Integer.parseInt(jsonData.getString("DisplayToCustomer")));
                                    payLoad.setName(jsonData.getString("Name"));
                                    responseCategoryoadList.add(payLoad);

                                }
                            }
                        }
                        setResponseCategeorySpinner(responseCategoryoadList);
                        new ResponseSolution(Constants.Solution,
                                "GET", RepairDetailsActivity.this, 1).execute();


                    } else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), RepairDetailsActivity.this);
                    } else {
                        ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), RepairDetailsActivity.this);
                    }
                } catch (JSONException e) {
                    ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
                    e.printStackTrace();
                }
            } else {
                ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
                Utils.showAlertBox("Something Went wrong!!", RepairDetailsActivity.this);
            }
        }

    }

    private class ResponseSolution extends CallManagerAsyncTask {
        JSONObject postParamData = new JSONObject();
        int flag = -1;

        public ResponseSolution(String action, String reqType, Context context, int flag
        ) {
            super(action, reqType, context);
            this.flag = flag;
            ProgressUtil.showProgressBar(RepairDetailsActivity.this,
                    findViewById(R.id.root), R.id.progressBar);
        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            try {

                return doWork(postParamData);
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
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if (json != null) {
                Utils.Log("JSON Response=========" + json.toString());
                try {

                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")) {
                        if (json.getJSONArray("PayLoad").get(0).toString().equalsIgnoreCase("No record(s) to display.")) {
                            Utils.Log(json.getJSONArray("PayLoad").get(0).toString());
                            Utils.showAlertBox(json.getJSONArray("PayLoad").get(0).toString(), RepairDetailsActivity.this);
                        } else {
                            JSONArray respSubstatusList = json.getJSONArray("PayLoad");
                            if (flag == 1) {
                                solutionLoadList = new ArrayList<>();


                                SolutionDTO.PayLoad payLoad = new SolutionDTO.PayLoad();
                                payLoad.setCode("-1");
                                payLoad.setDisplayToCustomer(-1);
                                payLoad.setName("--Select--");
                                payLoad.setCreatedBy("-1");
                                payLoad.setCreatedDate("-1");

                                solutionLoadList.add(payLoad);


                                for (int i = 0; i < respSubstatusList.length(); i++) {
                                    JSONObject jsonData = (JSONObject) respSubstatusList.get(i);
                                    payLoad = new SolutionDTO.PayLoad();
                                    payLoad.setCode(jsonData.getString("Code"));
                                    payLoad.setDisplayToCustomer(Integer.parseInt(jsonData.getString("Display_To_Customer")));
                                    payLoad.setName(jsonData.getString("Name"));
                                    payLoad.setCreatedBy(jsonData.getString("Created_By"));
                                    payLoad.setCreatedDate(jsonData.getString("Created_Date"));

                                    solutionLoadList.add(payLoad);
                                }
                            }
                        }

                        setSolutionSpinner(solutionLoadList);
                        new ResponseProblem(Constants.ProblemFix,
                                "GET", RepairDetailsActivity.this, 2).execute();

                    } else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), RepairDetailsActivity.this);
                    } else {
                        ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), RepairDetailsActivity.this);
                    }
                } catch (JSONException e) {
                    ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
                    e.printStackTrace();
                }
            } else {
                ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
                Utils.showAlertBox("Something Went wrong!!", RepairDetailsActivity.this);
            }
        }

    }

    private class ResponseProblem extends CallManagerAsyncTask {
        JSONObject postParamData = new JSONObject();
        int flag = -1;

        public ResponseProblem(String action, String reqType, Context context, int flag
        ) {

            super(action, reqType, context);
            this.flag = flag;
            ProgressUtil.showProgressBar(RepairDetailsActivity.this,
                    findViewById(R.id.root), R.id.progressBar);
        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            try {

                return doWork(postParamData);
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
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if (json != null) {
                Utils.Log("JSON Response=========" + json.toString());
                try {

                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")) {
                        if (json.getJSONArray("PayLoad").get(0).toString().equalsIgnoreCase("No record(s) to display.")) {
                            Utils.Log(json.getJSONArray("PayLoad").get(0).toString());
                            Utils.showAlertBox(json.getJSONArray("PayLoad").get(0).toString(), RepairDetailsActivity.this);
                        } else {
                            JSONArray respSubstatusList = json.getJSONArray("PayLoad");
                            if (flag == 2) {
                                ProblemFixDTO.PayLoad payLoad = new ProblemFixDTO.PayLoad();
                                payLoad.setCode("-1");
                                payLoad.setDisplayToCustomer(-1);
                                payLoad.setName("--Select--");
                                payLoad.setProblemCategory("-1");
                                problemLoadList.add(payLoad);
                                for (int i = 0; i < respSubstatusList.length(); i++) {
                                    JSONObject jsonData = (JSONObject) respSubstatusList.get(i);
                                    payLoad = new ProblemFixDTO.PayLoad();
                                    payLoad.setCode(jsonData.getString("Code"));
                                    payLoad.setDisplayToCustomer(Integer.parseInt(jsonData.getString("DisplayToCustomer")));
                                    payLoad.setName(jsonData.getString("Name"));
                                    payLoad.setProblemCategory(jsonData.getString("ProblemCategory"));
                                    problemLoadList.add(payLoad);


                                }
                            }
                        }

                        setProblemSpinner(problemLoadList);
                    } else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), RepairDetailsActivity.this);
                    } else {
                        ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), RepairDetailsActivity.this);
                    }
                } catch (JSONException e) {
                    ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
                    e.printStackTrace();
                }
            } else {
                ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
                Utils.showAlertBox("Something Went wrong!!", RepairDetailsActivity.this);
            }
        }

    }


    private void setProblemSpinner(List<ProblemFixDTO.PayLoad> loadList) {
        spinnerAdapter = new CustomeSpinnerAdapter(this, loadList, 2);
        spinnerProblemFix.setAdapter(spinnerAdapter);
    }

    private void setSolutionSpinner(List<SolutionDTO.PayLoad> loadList) {
        spinnerAdapter = new CustomeSpinnerAdapter(this, loadList, 1);
        spinnerSolution.setAdapter(spinnerAdapter);
    }

    private void setResponseCategeorySpinner(List<ResonseCategeoryDTO.PayLoad> loadList) {
        spinnerAdapter = new CustomeSpinnerAdapter(this, loadList, 0);
        spinnerCategory.setAdapter(spinnerAdapter);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LIST_REQUEST && resultCode == LIST_RESULT)

            try {

                btn_view.setVisibility(View.VISIBLE);

                str_jsonArray_foc = data.getStringExtra("jsonArrayfor_foc");
                str_jsonArray_charge = data.getStringExtra("jsonArrayfor_charge");
                jsonArray_image = FOC_CHARGE_Activity.jsonArray_for_images;

                Log.d("", "onActivityResult__jsonArray_image: "+ jsonArray_image);


//            jsonArray_foc = new JSONArray(str_jsonArray_foc);
                //           Log.d("str_jsonArray_foc", str_jsonArray_foc);

                jsonArray_charge = new JSONArray(str_jsonArray_charge);
                if(jsonArray_charge != null)
                {
                    if(jsonArray_charge.length() > 0)
                    {
                        btn_view_callClosour.setVisibility(View.VISIBLE);
                    }
                    Log.d("str_jsonArray_charge", jsonArray_charge.toString());
                }



           /* jsonArray_image = new JSONArray(str_jsonArray_image);
            Log.d("str_jsonArray_foc", str_jsonArray_image);*/


            } catch (Exception e) {
                e.printStackTrace();
            }


        try {
            key = data.getStringExtra("key");

            if (key != null || (list != null) || (list1 != null)) {


                if (key.equalsIgnoreCase("foc")) {
                    list = (ArrayList<ModelClassForSavedData>) data.getSerializableExtra("list");

                } else if (key.equalsIgnoreCase("charge")) {

                    //      btn_view_callClosour.setVisibility(View.VISIBLE);


                    list1 = (ArrayList<ModelClassForSavedData_Charge>) data.getSerializableExtra("list1");


                }
            } else {

            }
        } catch (Exception e) {

        }
    }


    //  new UserAsyncTask(Constants.SAVE_FOC_URL, "POST", RepairDetailsActivity.this ));execute();


    class Save_foc_list_asyntask extends CallManagerAsyncTask {



        public Save_foc_list_asyntask(String action, String reqType, Context context) {
            super(action, reqType, context);

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            JSONObject postParamData = new JSONObject();
            try {
                postParamData.put("ListFocPartRequestModel", FOC_CHARGE_Activity.jsonArrayfor_foc);


                Log.d("", "doInBackground: "+postParamData);

                //   postParamData.put("Password", password);
                return doWork(postParamData);
            } catch (JSONException e) {
                e.printStackTrace();
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
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if (json != null) {
                Utils.Log("JSON Response=========123456789" + json.toString());
                try {
                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")) {
                        progDailog.dismiss();

                        Utils.Log("response==== Save FOC" + json.toString());
                        Foc_errorMsg = json.getString("ErrorMessage").toString();
                        showAlertBox1("Follow up : "+followupError+"\n Foc Part : "+Foc_errorMsg ,RepairDetailsActivity.this);


                    } else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        progDailog.dismiss();

                        Foc_errorMsg = json.getString("ErrorMessage").toString();
                        showAlertBox1("Follow up : "+followupError+"\n Foc Part  : "+Foc_errorMsg ,RepairDetailsActivity.this);
                        //  Utils.showAlertBox(json.getString("ErrorMessage"), RepairDetailsActivity.this);
                    } else {
                        progDailog.dismiss();

                        Foc_errorMsg = json.getString("ErrorMessage").toString();
                        showAlertBox1("Follow up : "+followupError+"\n Foc Part  : "+Foc_errorMsg ,RepairDetailsActivity.this);
                        //   Utils.showAlertBox(json.getString("ErrorMessage"), RepairDetailsActivity.this);
                        //  showRepairCompleteAlert();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*finally {
                    showRepairCompleteAlert();
                }*/
            } else {
                try {
                    progDailog.dismiss();

                    Utils.showAlertBox(json.getString("ErrorMessage"), RepairDetailsActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }




    class Save_charge_list_asyntask extends CallManagerAsyncTask {


        public Save_charge_list_asyntask(String action, String reqType, Context context) {
            super(action, reqType, context);

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            JSONObject postParamData = new JSONObject();
            try {


                postParamData.put("DocketNo", docket_no);
                postParamData.put("ATMId", atm_id);
                postParamData.put("EngineerId", Prefs.with(RepairDetailsActivity.this).getString(UserId, ""));
                postParamData.put("Longitude", Prefs.with(RepairDetailsActivity.this).getString(Constant.LONGI, ""));
                postParamData.put("Latitude", Prefs.with(RepairDetailsActivity.this).getString(Constant.LATI, ""));
                postParamData.put("MobileDeviceId", Prefs.with(RepairDetailsActivity.this).getString(Constant.DEVICE_ID, ""));
                postParamData.put("ResponseType","Chargeable");
                postParamData.put("SubStatus",subStatusId);
                postParamData.put("PartRequestList", FOC_CHARGE_Activity.jsonArrayfor_charge);

                Log.d("", "doInBackgroundsubStatusId: "+subStatusId);
                Log.d("", "doInBackground12345: "+postParamData);

                //   postParamData.put("Password", password);
                return doWork(postParamData);
            } catch (JSONException e) {
                e.printStackTrace();
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
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if (json != null) {
                Utils.Log("JSON Response=========123456789" + json.toString());
                try {
                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")) {
                        Utils.Log("response==== Save FOC" + json.toString());
                        ChargableDataErrormsg = json.getString("ErrorMessage").toString();


                        if(FallInValidation.equalsIgnoreCase("Allow")) {
                            if(CallClloserActivity.ImagesJsonArray != null)
                            {
                            if( CallClloserActivity.ImagesJsonArray.length() > 0)
                            {
                                UploadFileCallClosourForCharge uploadFileCallClosour = new UploadFileCallClosourForCharge(Constants.SAVE_CALL_CLOSOUR_IMAGES + "?UserId="+userId , "POST", CallClloserActivity.ImagesJsonArray, RepairDetailsActivity.this);
                                uploadFileCallClosour.execute();
                            }

                        }

                        }else{

                            if( CallClloserActivity.ImagesJsonArray != null) {


                                Log.d("TAG", "onClick_ImagesJsonArray: "+CallClloserActivity.ImagesJsonArray);

                                ValidationPartRequest_UploadFileCallClosour uploadFileCallClosour = new ValidationPartRequest_UploadFileCallClosour(Constants.SAVE_CALL_CLOSOUR_IMAGES + "?UserId=" + userId, "POST", CallClloserActivity.ImagesJsonArray, RepairDetailsActivity.this);
                                uploadFileCallClosour.execute();

                            }else {


                            }

                        }



                        //   Utils.showAlertBox(json.getString("ErrorMessage"), RepairDetailsActivity.this);
                        //   Utils.showAlertBox("Follow up : "+followupError+"\n Chargable Image : "+ImageChargeErrormsg ,RepairDetailsActivity.this);
                       /* Log.d("", "onPostExecuteSUCCESS: "+json.getString("ErrorMessage"));
                        Utils.Log(json.getJSONArray("PayLoad").get(0).toString());
                        JSONObject user = (JSONObject) json.getJSONArray("PayLoad").get(0);*/




                        //  Toast.makeText(RepairDetailsActivity.this, "Save DATA", Toast.LENGTH_SHORT).show();

                        /*if( userId != null) {
                            new Save_charge_images_asyntask(Constants.SAVE_CHARGE_IMAGES_URL + " ?userId=" + userId, "POST"
                                    , RepairDetailsActivity.this).execute();
                        }else{

                        }*/

                        // store shared preference

                    } else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        ChargableDataErrormsg = json.getString("ErrorMessage").toString();
                        progDailog.dismiss();

                        showAlertBox1("Follow up : "+followupError+"\n Chargable Image : "+ImageChargeErrormsg+"\n Chargable part :"+ChargableDataErrormsg ,RepairDetailsActivity.this);
                    } else {
                        progDailog.dismiss();
                        ChargableDataErrormsg = json.getString("ErrorMessage").toString();
                        showAlertBox1("Follow up : "+followupError+"\n Chargable Image : "+ImageChargeErrormsg+"\n Chargable part :"+ChargableDataErrormsg ,RepairDetailsActivity.this);
                        //    Utils.showAlertBox(json.getString("ErrorMessage"), RepairDetailsActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    progDailog.dismiss();
                    Utils.showAlertBox(json.getString("ErrorMessage"), RepairDetailsActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }




/*
    class Save_charge_images_asyntask extends CallManagerAsyncTask {


        public Save_charge_images_asyntask(String action, String reqType, Context context) {
            super(action, reqType, context);

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            JSONObject postParamData = new JSONObject();
            try {
                postParamData.put("IMAGES", jsonArray_image);


                Log.d("", "doInBackgroundimagesssss: "+postParamData);

                //   postParamData.put("Password", password);
                return doWork(postParamData);
            } catch (JSONException e) {
                e.printStackTrace();
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
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            //   ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if (json != null) {
                Utils.Log("JSON Response=========12345678911111" + json.toString());
                try {
                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")) {

                        Utils.Log("response==== Save FOC" + json.toString());
                        Utils.Log(json.getJSONArray("PayLoad").get(0).toString());
                        JSONObject user = (JSONObject) json.getJSONArray("PayLoad").get(0);

                        //  Toast.makeText(RepairDetailsActivity.this, "Save DATA", Toast.LENGTH_SHORT).show();


                    } else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        Utils.showAlertBox(json.getString("ErrorMessage"), RepairDetailsActivity.this);
                    } else {
                        Utils.showAlertBox(json.getString("ErrorMessage"), RepairDetailsActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Utils.showAlertBox(json.getString("ErrorMessage"), RepairDetailsActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }
*/



    public class UploadFileChargeImage extends CallManagerAsyncTask {

        String comment,uploadedFile;
        JSONArray Charge_image;
        public UploadFileChargeImage(String action, String reqType, JSONArray charge_image, Context context){
            super(action, reqType, context);
            this.Charge_image = charge_image;
            // this.uploadedFile = uploadedFile;

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            JSONObject postParamData = new JSONObject();
            try {

                return doWorkJSONArray(Charge_image);
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
           /* ProgressUtil.showProgressBar(CallClloserActivity.this,
                    findViewById(R.id.root), R.id.progressBar);*/
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            // ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if(json != null){
                Utils.Log("JSON Response========="+json.toString());
                try {
                    if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")){

                        ImageChargeErrormsg = json.getString("ErrorMessage").toString();

                        //  Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);
                        new Save_charge_list_asyntask(Constants.SAVE_CHARGE_URL, "POST"
                                , RepairDetailsActivity.this).execute();
                        //  showRepairCompleteAlert();


                    }else if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        progDailog.dismiss();
                        ImageChargeErrormsg = json.getString("ErrorMessage").toString();
                        showAlertBox1("Follow up : "+followupError+"\n Chargable Image : "+ImageChargeErrormsg ,RepairDetailsActivity.this);
                        //  Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);
                    }else {
                        progDailog.dismiss();
                        ImageChargeErrormsg = json.getString("ErrorMessage").toString();
                        showAlertBox1("Follow up : "+followupError+"\n Chargable Image : "+ImageChargeErrormsg ,RepairDetailsActivity.this);
                        // Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*finally {
                    showRepairCompleteAlert();
                }*/
            }else {
                progDailog.dismiss();
                Utils.showAlertBox("Something Went wrong!!",RepairDetailsActivity.this);
               /* Intent i = new Intent(CallClloserActivity.this,CallClloserActivity.class);
                startActivity(i);*/
            }
        }
    }



    // Call closour aync task
    public class SaveCallClouser extends CallManagerAsyncTask {

        String comment,uploadedFile;
        JSONObject callClosour;


        public SaveCallClouser(String action, String reqType, JSONObject CallClosur, Context context){
            super(action, reqType, context);
            this.callClosour = CallClosur;

            // this.uploadedFile = uploadedFile;

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            JSONObject postParamData = new JSONObject();
            try {
              /*  postParamData.put("DocketNo",docketNo);
                postParamData.put("File",uploadedFile);
                postParamData.put("FileName",userId+String.valueOf(System.currentTimeMillis())+".jpg");
                //  postParamData.put("FileName","");

                postParamData.put("Comments",comment);*/
                return doWork(callClosour);
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
            ProgressUtil.showProgressBar(RepairDetailsActivity.this,
                    findViewById(R.id.root), R.id.progressBar);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if(json != null){
                Utils.Log("JSON Response========="+json.toString());
                try {
                    if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")){

                        // progDailog.dismiss();
                        saveCallErrorMsg = json.getString("ErrorMessage").toString();

                        if(CallClloserActivity.IdleHoursjson != null)
                        {
                            if(CallClloserActivity.IdleHoursjson != null)
                            {
                                if(CallClloserActivity.IdleHoursjson.length() > 0)
                                {
                                    SaveIdleHours saveIdleHours = new SaveIdleHours(Constants.SAVE_CALL_CLOSOUR_IdealHours + "?DocketNo="+docket_no , "POST", CallClloserActivity.IdleHoursjson, RepairDetailsActivity.this);
                                    saveIdleHours.execute();
                                }
                            }
                        }else  if(QuestionairActivity.QuestionJsonArray != null)
                        {
                            if( QuestionairActivity.QuestionJsonArray.length() > 0)
                            {
                                SaveQuestionCallClouser saveQuestionCallClouser = new SaveQuestionCallClouser(Constants.SAVE_CALL_CLOSOUR_QUESTION + "?DocketNo="+docket_no , "POST", QuestionairActivity.QuestionJsonArray, RepairDetailsActivity.this);
                                saveQuestionCallClouser.execute();
                            }
                        }else {
                            progDailog.dismiss();
                            showAlertBox1("Follow up : "+followupError+"\n Image Upload : "+ CallImageCallErrorMsg+"\n Save Call Closure :"+saveCallErrorMsg ,RepairDetailsActivity.this);


                        }



                    }else if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        saveCallErrorMsg = json.getString("ErrorMessage").toString();
                        progDailog.dismiss();

                        showAlertBox1("Follow up : "+followupError+"\n Image Upload :"+ CallImageCallErrorMsg +"\n Call Closure : "+saveCallErrorMsg ,RepairDetailsActivity.this);


                        //   Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);
                    }else {
                        saveCallErrorMsg = json.getString("ErrorMessage").toString();
                        progDailog.dismiss();

                        showAlertBox1("Follow up : "+followupError+"\n Image Upload :"+ CallImageCallErrorMsg +"\n Call Closure : "+saveCallErrorMsg ,RepairDetailsActivity.this);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else {
                progDailog.dismiss();

                Utils.showAlertBox("Something Went wrong!!",RepairDetailsActivity.this);
               /* Intent i = new Intent(CallClloserActivity.this,CallClloserActivity.class);
                startActivity(i);*/
            }
        }
    }

    // Call closour aync task
    public class SaveCallClouserForchargable extends CallManagerAsyncTask {

        String comment,uploadedFile;
        JSONObject callClosour;


        public SaveCallClouserForchargable(String action, String reqType, JSONObject CallClosur, Context context){
            super(action, reqType, context);
            this.callClosour = CallClosur;

            // this.uploadedFile = uploadedFile;

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            JSONObject postParamData = new JSONObject();
            try {
              /*  postParamData.put("DocketNo",docketNo);
                postParamData.put("File",uploadedFile);
                postParamData.put("FileName",userId+String.valueOf(System.currentTimeMillis())+".jpg");
                //  postParamData.put("FileName","");

                postParamData.put("Comments",comment);*/
                return doWork(callClosour);
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
            ProgressUtil.showProgressBar(RepairDetailsActivity.this,
                    findViewById(R.id.root), R.id.progressBar);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if(json != null){
                Utils.Log("JSON Response========="+json.toString());
                try {
                    if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")){

                        saveCallErrorMsg = json.getString("ErrorMessage").toString();

                        if(CallClloserActivity.IdleHoursjson != null)
                        {
                            if(CallClloserActivity.IdleHoursjson != null)
                            {
                                if(CallClloserActivity.IdleHoursjson.length() > 0)
                                {
                                    SaveIdleHoursForCharge saveIdleHours = new SaveIdleHoursForCharge(Constants.SAVE_CALL_CLOSOUR_IdealHours + "?DocketNo="+docket_no , "POST", CallClloserActivity.IdleHoursjson, RepairDetailsActivity.this);
                                    saveIdleHours.execute();
                                }
                            }
                        }else  if(QuestionairActivity.QuestionJsonArray != null)
                        {
                            if( QuestionairActivity.QuestionJsonArray.length() > 0)
                            {
                                SaveQuestionCallClouserForCharge saveQuestionCallClouser = new SaveQuestionCallClouserForCharge(Constants.SAVE_CALL_CLOSOUR_QUESTION + "?DocketNo="+docket_no , "POST", QuestionairActivity.QuestionJsonArray, RepairDetailsActivity.this);
                                saveQuestionCallClouser.execute();
                            }
                        }else {

                            progDailog.dismiss();


                            showAlertBox1("Follow up : "+followupError+"\n Chargable Image : "+ImageChargeErrormsg+"\n Chargable part :"+ChargableDataErrormsg +"\n Call closure Image Upload : "+CallImageCallErrorMsg+"\n Call closure: "+saveCallErrorMsg ,RepairDetailsActivity.this);

                            //  showAlertBox1("Follow up : "+followupError+"\n Chargable Image : "+ImageChargeErrormsg+"\n Chargable part :"+ChargableDataErrormsg +"\n Call closure Image Upload : "+CallImageCallErrorMsg,RepairDetailsActivity.this);


                        }





                    }else if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        progDailog.dismiss();

                        saveCallErrorMsg = json.getString("ErrorMessage").toString();
                        showAlertBox1("Follow up : "+followupError+"\n Chargable Image : "+ImageChargeErrormsg+"\n Chargable part :"+ChargableDataErrormsg +"\n Call closure Image Upload : "+CallImageCallErrorMsg+"\n Call closure: "+saveCallErrorMsg ,RepairDetailsActivity.this);
                        //  Utils.showAlertBox("Follow up : "+followupError+"\n Call Closure : "+saveCallErrorMsg ,RepairDetailsActivity.this);


                        //   Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);
                    }else {

                        progDailog.dismiss();

                        saveCallErrorMsg = json.getString("ErrorMessage").toString();
                        showAlertBox1("Follow up : "+followupError+"\n Chargable Image : "+ImageChargeErrormsg+"\n Chargable part :"+ChargableDataErrormsg +"\n Call closure Image Upload : "+CallImageCallErrorMsg+"\n Call closure: "+saveCallErrorMsg ,RepairDetailsActivity.this);
                        //  Utils.showAlertBox("Follow up : "+followupError+"\n Call Closure : "+saveCallErrorMsg ,RepairDetailsActivity.this);

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else {
                Utils.showAlertBox("Something Went wrong!!",RepairDetailsActivity.this);
               /* Intent i = new Intent(CallClloserActivity.this,CallClloserActivity.class);
                startActivity(i);*/
                progDailog.dismiss();

            }
        }
    }


    public class SaveIdleHours extends CallManagerAsyncTask {

        String comment,uploadedFile;
        JSONObject callClosour_idealHours;

        public SaveIdleHours(String action, String reqType, JSONObject CallClosur_ideal, Context context){
            super(action, reqType, context);
            this.callClosour_idealHours = CallClosur_ideal;

            // this.uploadedFile = uploadedFile;

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            try {

                return doWork(callClosour_idealHours);
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
            ProgressUtil.showProgressBar(RepairDetailsActivity.this,
                    findViewById(R.id.root), R.id.progressBar);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if(json != null){
                Utils.Log("JSON Response========="+json.toString());
                try {
                    if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")){
                        /*Intent i = new Intent(AttachImageActivity.this, AcknowledgementActivity.class);
                        i.putExtra("docketNo",docketNo);
                        startActivity(i);
                        finish();*/
                        //  progressDialog.dismiss();
                        //  Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);
                        // Toast.makeText(RepairDetailsActivity.this, json.getString("ErrorMessage").toString(), Toast.LENGTH_LONG).show();
                        IdealHrsErrorMsg = json.getString("ErrorMessage").toString();
                        if(QuestionairActivity.QuestionJsonArray != null)
                        {
                            if( QuestionairActivity.QuestionJsonArray.length() > 0)
                            {
                                SaveQuestionCallClouser saveQuestionCallClouser = new SaveQuestionCallClouser(Constants.SAVE_CALL_CLOSOUR_QUESTION + "?DocketNo="+docket_no , "POST", QuestionairActivity.QuestionJsonArray, RepairDetailsActivity.this);
                                saveQuestionCallClouser.execute();
                            }
                        }else
                        {
                            progDailog.dismiss();
                            showAlertBox1("Follow up : "+followupError+"\n Image Upload : "+ CallImageCallErrorMsg +"\n Call Closure : "+saveCallErrorMsg +"\n Ideal hours : "+IdealHrsErrorMsg,RepairDetailsActivity.this);
                        }


                    }else if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        //  progDailog.dismiss();
                        IdealHrsErrorMsg = json.getString("ErrorMessage").toString();
                        if(QuestionairActivity.QuestionJsonArray != null)
                        {
                            if( QuestionairActivity.QuestionJsonArray.length() > 0)
                            {
                                SaveQuestionCallClouser saveQuestionCallClouser = new SaveQuestionCallClouser(Constants.SAVE_CALL_CLOSOUR_QUESTION + "?DocketNo="+docket_no , "POST", QuestionairActivity.QuestionJsonArray, RepairDetailsActivity.this);
                                saveQuestionCallClouser.execute();
                            }
                        }else {
                            progDailog.dismiss();
                            // showAlertBox1("Follow up : "+followupError+"\n Call Closure : "+saveCallErrorMsg +"\n Image Upload : "+ CallImageCallErrorMsg,RepairDetailsActivity.this);
                            showAlertBox1("Follow up : "+followupError+"\n Image Upload : "+ CallImageCallErrorMsg +"\n Call Closure : "+saveCallErrorMsg +"\n Ideal hours : "+IdealHrsErrorMsg,RepairDetailsActivity.this);

                        }
                        //   showAlertBox1("Follow up : "+followupError+"\n Call Closure : "+saveCallErrorMsg +"\n Image Upload : "+ CallImageCallErrorMsg +"\n Ideal hours : "+IdealHrsErrorMsg,RepairDetailsActivity.this);
                        //    Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);
                    }else {
                        //  progDailog.dismiss();

                        IdealHrsErrorMsg = json.getString("ErrorMessage").toString();
                        if(QuestionairActivity.QuestionJsonArray != null)
                        {
                            if( QuestionairActivity.QuestionJsonArray.length() > 0)
                            {
                                SaveQuestionCallClouser saveQuestionCallClouser = new SaveQuestionCallClouser(Constants.SAVE_CALL_CLOSOUR_QUESTION + "?DocketNo="+docket_no , "POST", QuestionairActivity.QuestionJsonArray, RepairDetailsActivity.this);
                                saveQuestionCallClouser.execute();
                            }
                        }else {
                            progDailog.dismiss();
                            // showAlertBox1("Follow up : "+followupError+"\n Call Closure : "+saveCallErrorMsg +"\n Image Upload : "+ CallImageCallErrorMsg,RepairDetailsActivity.this);
                            showAlertBox1("Follow up : "+followupError+"\n Image Upload : "+ CallImageCallErrorMsg +"\n Call Closure : "+saveCallErrorMsg +"\n Ideal hours : "+IdealHrsErrorMsg,RepairDetailsActivity.this);

                        }
                        // showAlertBox1("Follow up : "+followupError+"\n Call Closure : "+saveCallErrorMsg +"\n Image Upload : "+ CallImageCallErrorMsg +"\n Ideal hours : "+IdealHrsErrorMsg,RepairDetailsActivity.this);

                        // Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
               /* finally {
                    showRepairCompleteAlert();
                }*/
            }else {
                progDailog.dismiss();
                Utils.showAlertBox("Something Went wrong!!",RepairDetailsActivity.this);
               /* Intent i = new Intent(CallClloserActivity.this,CallClloserActivity.class);
                startActivity(i);*/
            }
        }
    }

    public class SaveIdleHoursForCharge extends CallManagerAsyncTask {

        String comment,uploadedFile;
        JSONObject callClosour_idealHours;

        public SaveIdleHoursForCharge(String action, String reqType, JSONObject CallClosur_ideal, Context context){
            super(action, reqType, context);
            this.callClosour_idealHours = CallClosur_ideal;

            // this.uploadedFile = uploadedFile;

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            try {

                return doWork(callClosour_idealHours);
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
            ProgressUtil.showProgressBar(RepairDetailsActivity.this,
                    findViewById(R.id.root), R.id.progressBar);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if(json != null){
                Utils.Log("JSON Response========="+json.toString());
                try {
                    if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")){

                        //   progDailog.dismiss();

                        IdealHrsErrorMsg = json.getString("ErrorMessage").toString();
                        if(QuestionairActivity.QuestionJsonArray != null)
                        {
                            if( QuestionairActivity.QuestionJsonArray.length() > 0)
                            {
                                SaveQuestionCallClouserForCharge saveQuestionCallClouser = new SaveQuestionCallClouserForCharge(Constants.SAVE_CALL_CLOSOUR_QUESTION + "?DocketNo="+docket_no , "POST", QuestionairActivity.QuestionJsonArray, RepairDetailsActivity.this);
                                saveQuestionCallClouser.execute();
                            }
                        }else
                        {
                            progDailog.dismiss();
                            showAlertBox1("Follow up : "+followupError+"\n Chargable Image : "+ImageChargeErrormsg+"\n Chargable part :"+ChargableDataErrormsg +"\n Call closure Image Upload : "+CallImageCallErrorMsg+"\n Call closure: "+saveCallErrorMsg+"\n ideal Hours : "+IdealHrsErrorMsg ,RepairDetailsActivity.this);


                        }


                    }else if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        IdealHrsErrorMsg = json.getString("ErrorMessage").toString();
                        //   progDailog.dismiss();

                        if(QuestionairActivity.QuestionJsonArray != null)
                        {
                            if( QuestionairActivity.QuestionJsonArray.length() > 0)
                            {
                                SaveQuestionCallClouserForCharge saveQuestionCallClouser = new SaveQuestionCallClouserForCharge(Constants.SAVE_CALL_CLOSOUR_QUESTION + "?DocketNo="+docket_no , "POST", QuestionairActivity.QuestionJsonArray, RepairDetailsActivity.this);
                                saveQuestionCallClouser.execute();
                            }
                        }else
                        {
                            progDailog.dismiss();
                            showAlertBox1("Follow up : "+followupError+"\n Chargable Image : "+ImageChargeErrormsg+"\n Chargable part :"+ChargableDataErrormsg +"\n Call closure Image Upload : "+CallImageCallErrorMsg+"\n Call closure: "+saveCallErrorMsg+"\n ideal Hours : "+IdealHrsErrorMsg ,RepairDetailsActivity.this);


                        }

                        //   showAlertBox1("Follow up : "+followupError+"\n Chargable Image : "+ImageChargeErrormsg+"\n Chargable part :"+ChargableDataErrormsg +"\n Call closure:"+saveCallErrorMsg+"\n Call closure Image Upload : "+CallImageCallErrorMsg+"\n ideal Hours : "+IdealHrsErrorMsg,RepairDetailsActivity.this);


                    }else {
                        IdealHrsErrorMsg = json.getString("ErrorMessage").toString();
                        // progDailog.dismiss();
                        if(QuestionairActivity.QuestionJsonArray != null)
                        {
                            if( QuestionairActivity.QuestionJsonArray.length() > 0)
                            {
                                SaveQuestionCallClouserForCharge saveQuestionCallClouser = new SaveQuestionCallClouserForCharge(Constants.SAVE_CALL_CLOSOUR_QUESTION + "?DocketNo="+docket_no , "POST", QuestionairActivity.QuestionJsonArray, RepairDetailsActivity.this);
                                saveQuestionCallClouser.execute();
                            }
                        }else
                        {
                            progDailog.dismiss();
                            showAlertBox1("Follow up : "+followupError+"\n Chargable Image : "+ImageChargeErrormsg+"\n Chargable part :"+ChargableDataErrormsg +"\n Call closure:"+saveCallErrorMsg+"\n Call closure Image Upload : "+CallImageCallErrorMsg+"\n ideal Hours : "+IdealHrsErrorMsg,RepairDetailsActivity.this);


                        }

                        //   showAlertBox1("Follow up : "+followupError+"\n Chargable Image : "+ImageChargeErrormsg+"\n Chargable part :"+ChargableDataErrormsg +"\n Call closure:"+saveCallErrorMsg+"\n Call closure Image Upload : "+CallImageCallErrorMsg+"\n ideal Hours : "+IdealHrsErrorMsg,RepairDetailsActivity.this);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
               /* finally {
                    showRepairCompleteAlert();
                }*/
            }else {
                progDailog.dismiss();

                Utils.showAlertBox("Something Went wrong!!",RepairDetailsActivity.this);
               /* Intent i = new Intent(CallClloserActivity.this,CallClloserActivity.class);
                startActivity(i);*/
            }
        }
    }

    public class UploadFileCallClosour extends CallManagerAsyncTask {

        String comment,uploadedFile;
        JSONArray callClosour_images;

        public UploadFileCallClosour(String action, String reqType, JSONArray CallClosour_images, Context context){
            super(action, reqType, context);
            this.callClosour_images = CallClosour_images;

            // this.uploadedFile = uploadedFile;

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            JSONObject postParamData = new JSONObject();
            try {
              /*  postParamData.put("DocketNo",docketNo);
                postParamData.put("File",uploadedFile);
                postParamData.put("FileName",userId+String.valueOf(System.currentTimeMillis())+".jpg");
                //  postParamData.put("FileName","");

                postParamData.put("Comments",comment);*/
                return doWorkJSONArray(callClosour_images);
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
            ProgressUtil.showProgressBar(RepairDetailsActivity.this,
                    findViewById(R.id.root), R.id.progressBar);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if(json != null){
                Utils.Log("JSON Response========="+json.toString());
                try {
                    if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")){

                        CallImageCallErrorMsg = json.getString("ErrorMessage").toString();
                        //  Toast.makeText(RepairDetailsActivity.this, ""+FallInValidation, Toast.LENGTH_SHORT).show();


                        if(FallInValidation.equalsIgnoreCase("Allow")) {

                            if (CallClloserActivity.CallClosurejson != null) {
                                if (CallClloserActivity.CallClosurejson.length() > 0) {
                                    CallClloserActivity.CallClosurejson.put("ActionTaken", edtTxtComment.getText().toString());

                                    SaveCallClouser saveCallClouser = new SaveCallClouser(Constants.SAVE_CALL_CLOSOUR + "?DocketNo=" + docket_no, "POST", CallClloserActivity.CallClosurejson, RepairDetailsActivity.this);
                                    saveCallClouser.execute();
                                }
                            }
                        }/*else {
                            if( CallClloserActivity.ImagesJsonArray != null) {


                                Log.d("TAG", "onClick_ImagesJsonArray: "+CallClloserActivity.ImagesJsonArray);

                                ValidationPartRequest_UploadFileCallClosour uploadFileCallClosour = new ValidationPartRequest_UploadFileCallClosour(Constants.SAVE_CALL_CLOSOUR_IMAGES + "?UserId=" + userId, "POST", CallClloserActivity.ImagesJsonArray, RepairDetailsActivity.this);
                                uploadFileCallClosour.execute();

                            }else {


                            }
                        }*/









                    }else if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        //  Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);
                        showAlertBox1("Follow up : "+followupError+"\n  Image Upload : "+ CallImageCallErrorMsg,RepairDetailsActivity.this);
                        progDailog.dismiss();

                        // Utils.showAlertBox("Follow up : "+followupError+"\n Call Closure : "+saveCallErrorMsg +"\n Image Upload : "+ CallImageCallErrorMsg,RepairDetailsActivity.this);
                    }else {
                        CallImageCallErrorMsg = json.getString("ErrorMessage").toString();
                        progDailog.dismiss();
                        showAlertBox1("Follow up : "+followupError+" \n Image Upload : "+ CallImageCallErrorMsg,RepairDetailsActivity.this);

                        //  Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                progDailog.dismiss();
                Utils.showAlertBox("Something Went wrong!!",RepairDetailsActivity.this);
               /* Intent i = new Intent(CallClloserActivity.this,CallClloserActivity.class);
                startActivity(i);*/
            }
        }
    }

    public class UploadFileCallClosourForCharge extends CallManagerAsyncTask {

        String comment,uploadedFile;
        JSONArray callClosour_images;

        public UploadFileCallClosourForCharge(String action, String reqType, JSONArray CallClosour_images, Context context){
            super(action, reqType, context);
            this.callClosour_images = CallClosour_images;

            // this.uploadedFile = uploadedFile;

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            JSONObject postParamData = new JSONObject();
            try {
              /*  postParamData.put("DocketNo",docketNo);
                postParamData.put("File",uploadedFile);
                postParamData.put("FileName",userId+String.valueOf(System.currentTimeMillis())+".jpg");
                //  postParamData.put("FileName","");

                postParamData.put("Comments",comment);*/
                return doWorkJSONArray(callClosour_images);
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
            ProgressUtil.showProgressBar(RepairDetailsActivity.this,
                    findViewById(R.id.root), R.id.progressBar);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if(json != null){
                Utils.Log("JSON Response========="+json.toString());
                try {
                    if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")){


                        CallImageCallErrorMsg = json.getString("ErrorMessage").toString();


                        if(FallInValidation.equalsIgnoreCase("Allow")) {


                            if (CallClloserActivity.CallClosurejson != null) {
                                if (CallClloserActivity.CallClosurejson.length() > 0) {
                                    CallClloserActivity.CallClosurejson.put("ActionTaken", edtTxtComment.getText().toString());

                                    SaveCallClouserForchargable saveCallClouserforCharge = new SaveCallClouserForchargable(Constants.SAVE_CALL_CLOSOUR + "?DocketNo=" + docket_no, "POST", CallClloserActivity.CallClosurejson, RepairDetailsActivity.this);
                                    saveCallClouserforCharge.execute();
                                }
                            }

                        }

//\n Call closure:"+saveCallErrorMsg+"

                        showAlertBox1("Follow up : "+followupError+"\n Chargable Image : "+ImageChargeErrormsg+"\n Chargable part :"+ChargableDataErrormsg +"\n Call closure Image Upload : "+CallImageCallErrorMsg,RepairDetailsActivity.this);




                    }else if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        progDailog.dismiss();

                        CallImageCallErrorMsg = json.getString("ErrorMessage").toString();
                        showAlertBox1("Follow up : "+followupError+"\n Chargable Image : "+ImageChargeErrormsg+"\n Chargable part :"+ChargableDataErrormsg +"\n Call closure Image Upload : "+CallImageCallErrorMsg,RepairDetailsActivity.this);

                    }else {
                        progDailog.dismiss();

                        CallImageCallErrorMsg = json.getString("ErrorMessage").toString();
                        showAlertBox1("Follow up : "+followupError+"\n Chargable Image : "+ImageChargeErrormsg+"\n Chargable part :"+ChargableDataErrormsg +"\n Call closure Image Upload : "+CallImageCallErrorMsg,RepairDetailsActivity.this);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                progDailog.dismiss();

                Utils.showAlertBox("Something Went wrong!!",RepairDetailsActivity.this);
               /* Intent i = new Intent(CallClloserActivity.this,CallClloserActivity.class);
                startActivity(i);*/
            }
        }
    }

    public class SaveQuestionCallClouser extends CallManagerAsyncTask {

        String comment,uploadedFile;
        JSONArray callClosour_question;
        public SaveQuestionCallClouser(String action, String reqType, JSONArray CallClosour_question, Context context){
            super(action, reqType, context);
            this.callClosour_question = CallClosour_question;
            // this.uploadedFile = uploadedFile;

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            try {
                return doWorkJSONArray(callClosour_question);
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
            ProgressUtil.showProgressBar(RepairDetailsActivity.this,
                    findViewById(R.id.root), R.id.progressBar);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if(json != null){
                Utils.Log("JSON Response========="+json.toString());
                try {
                    if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")){

                        //   progressDialog.dismiss();
                        //  Toast.makeText(RepairDetailsActivity.this, json.getString("ErrorMessage").toString(), Toast.LENGTH_SHORT).show();
                        questionErrormsg = json.getString("ErrorMessage").toString();
                        progDailog.dismiss();
                        showAlertBox1("Follow up : "+followupError+ "\n Image Upload :" + CallImageCallErrorMsg + "\nCall Closure : "+saveCallErrorMsg +"\n Ideal hours : "+IdealHrsErrorMsg +"\n Questionair : "+questionErrormsg,RepairDetailsActivity.this);

                    }else if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        //  Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);
                        questionErrormsg = json.getString("ErrorMessage").toString();
                        progDailog.dismiss();
                        showAlertBox1("Follow up : "+followupError+ "\n Image Upload :" + CallImageCallErrorMsg + "\nCall Closure : "+saveCallErrorMsg +"\n Ideal hours : "+IdealHrsErrorMsg +"\n Questionair : "+questionErrormsg,RepairDetailsActivity.this);
                    }else {
                        questionErrormsg = json.getString("ErrorMessage").toString();
                        progDailog.dismiss();
                        showAlertBox1("Follow up : "+followupError+ "\n Image Upload :" + CallImageCallErrorMsg + "\nCall Closure : "+saveCallErrorMsg +"\n Ideal hours : "+IdealHrsErrorMsg +"\n Questionair : "+questionErrormsg,RepairDetailsActivity.this);

                        // Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                progDailog.dismiss();
                Utils.showAlertBox("Something Went wrong!!",RepairDetailsActivity.this);
               /* Intent i = new Intent(CallClloserActivity.this,CallClloserActivity.class);
                startActivity(i);*/
            }
        }
    }

    public class SaveQuestionCallClouserForCharge extends CallManagerAsyncTask {

        String comment,uploadedFile;
        JSONArray callClosour_question;
        public SaveQuestionCallClouserForCharge(String action, String reqType, JSONArray CallClosour_question, Context context){
            super(action, reqType, context);
            this.callClosour_question = CallClosour_question;
            // this.uploadedFile = uploadedFile;

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            try {
                return doWorkJSONArray(callClosour_question);
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
            ProgressUtil.showProgressBar(RepairDetailsActivity.this,
                    findViewById(R.id.root), R.id.progressBar);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if(json != null){
                Utils.Log("JSON Response========="+json.toString());
                try {
                    if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")){

                        progDailog.dismiss();
                        //  Toast.makeText(RepairDetailsActivity.this, json.getString("ErrorMessage").toString(), Toast.LENGTH_SHORT).show();
                        questionErrormsg = json.getString("ErrorMessage").toString();

                        showAlertBox1("Follow up : "+followupError+"\n Chargable Image : "+ImageChargeErrormsg+"\n Chargable part :"+ChargableDataErrormsg +"\n Call closure Image Upload : "+CallImageCallErrorMsg+"\n Call closure: "+saveCallErrorMsg+"\n ideal Hours : "+IdealHrsErrorMsg+"\n Questionairy : "+questionErrormsg ,RepairDetailsActivity.this);


                    }else if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        //  Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);
                        questionErrormsg = json.getString("ErrorMessage").toString();
                        progDailog.dismiss();

                        showAlertBox1("Follow up : "+followupError+"\n Chargable Image : "+ImageChargeErrormsg+"\n Chargable part :"+ChargableDataErrormsg +"\n Call closure Image Upload : "+CallImageCallErrorMsg+"\n Call closure: "+saveCallErrorMsg+"\n ideal Hours : "+IdealHrsErrorMsg+"\n Questionairy : "+questionErrormsg ,RepairDetailsActivity.this);

                        // Utils.showAlertBox("Call Closure : "+saveCallErrorMsg +"\n Image Upload : "+ CallImageCallErrorMsg +"\n Ideal hours : "+IdealHrsErrorMsg +"\n Questionair : "+questionErrormsg,RepairDetailsActivity.this);
                    }else {
                        progDailog.dismiss();

                        questionErrormsg = json.getString("ErrorMessage").toString();

                        showAlertBox1("Follow up : "+followupError+"\n Chargable Image : "+ImageChargeErrormsg+"\n Chargable part :"+ChargableDataErrormsg +"\n Call closure Image Upload : "+CallImageCallErrorMsg+"\n Call closure: "+saveCallErrorMsg+"\n ideal Hours : "+IdealHrsErrorMsg+"\n Questionairy : "+questionErrormsg ,RepairDetailsActivity.this);

                        // Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                progDailog.dismiss();

                Utils.showAlertBox("Something Went wrong!!",RepairDetailsActivity.this);
               /* Intent i = new Intent(CallClloserActivity.this,CallClloserActivity.class);
                startActivity(i);*/
            }
        }
    }

    public  void showAlertBox1(String message , Activity activity){

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);

        builder.setTitle("CMS");
        builder.setMessage(message).setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                showRepairCompleteAlert();
            }
        });
        builder.create().show();
    }


    class GetCallClosureImages extends CallManagerAsyncTask {
        JSONObject postParamData = new JSONObject();

        public GetCallClosureImages(String action, String reqType, Context context
        ) {
            super(action, reqType, context);

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            try {

                return doWork(postParamData);
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


            progDailog = new ProgressDialog(RepairDetailsActivity.this);
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            if (json != null) {
                Utils.Log("JSON Response121313121213131313213132=========" + json.toString());
                try {
                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")) {
                        if (json.getJSONArray("PayLoad").get(0).toString().equalsIgnoreCase("No record(s) to display.")) {
                            Utils.Log(json.getJSONArray("PayLoad").get(0).toString());
                            Utils.showAlertBox(json.getJSONArray("PayLoad").get(0).toString(), RepairDetailsActivity.this);
                        } else {
                            JSONArray respSubstatusList = json.getJSONArray("PayLoad");
                            for (int i = 0; i < respSubstatusList.length(); i++) {
                                JSONObject jsonData = (JSONObject) respSubstatusList.get(i);

                                ImageModel  model = new ImageModel();
                                model.setImage(jsonData.getString("Image"));
                                model.setDescription(jsonData.getString("Description"));

                                if(jsonData.getString("Description").equalsIgnoreCase("Call Closure FCR Attachment")){
                                    String image = jsonData.getString("Image");

                                    Prefs.with(RepairDetailsActivity.this).save(Constant.FCRAttachment, image);




                                }

                                if(jsonData.getString("Description").equalsIgnoreCase("Installation Certificate")){

                                    String image_string = jsonData.getString("Image");


                                    Prefs.with(RepairDetailsActivity.this).save(Constant.InstallationCertificate, image_string);


                                }

                                if(jsonData.getString("Description").equalsIgnoreCase("ATM Images1")){

                                    String image_string = jsonData.getString("Image");


                                    Prefs.with(RepairDetailsActivity.this).save(Constant.ATMImages1, image_string);


                                }

                                if(jsonData.getString("Description").equalsIgnoreCase("ATM Images2")){

                                    String image_string = jsonData.getString("Image");

                                    Prefs.with(RepairDetailsActivity.this).save(Constant.ATMImages2, image_string);



                                }

                                if(jsonData.getString("Description").equalsIgnoreCase("Transaction Image1")){

                                    String image_string = jsonData.getString("Image");

                                    Prefs.with(RepairDetailsActivity.this).save(Constant.TransactionImage1, image_string);



                                }

                                if(jsonData.getString("Description").equalsIgnoreCase("Transaction Image2")){

                                    String image_string = jsonData.getString("Image");


                                    Prefs.with(RepairDetailsActivity.this).save(Constant.TransactionImage2, image_string);



                                }
                                if(jsonData.getString("Description").equalsIgnoreCase("Error History")){

                                    String image_string = jsonData.getString("Image");

                                    Prefs.with(RepairDetailsActivity.this).save(Constant.ErrorHistory, image_string);


                                }



                                // image_Model.add(model);

                                //  Log.d("TAG", "onPostExecute_values: "+values);

                            }
                            progDailog.dismiss();

                        }


                    } else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        progDailog.dismiss();

                        // Utils.showAlertBox(json.getString("ErrorMessage").toString(), CallClloserActivity.this);
                    } else {

                        progDailog.dismiss();
// Utils.showAlertBox(json.getString("ErrorMessage").toString(), CallClloserActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.showAlertBox("Something Went wrong!!", RepairDetailsActivity.this);
                progDailog.dismiss();

            }
        }

    }




     class GetOpenCall extends CallManagerAsyncTaskArray {
        JSONArray postParamData = new JSONArray();
        APIListner apiListner;

        public GetOpenCall(String action, String reqType, Context context, APIListner apiListner) {
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
            if (json.length()> 0) {
                Utils.Log("JSON GetOpenCall1234=========" + json.toString());

                try {
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject dataobj = json.getJSONObject(i);


                        Open_Call_Details_model model = new Open_Call_Details_model();

                        model.setNoofCallsfortheATM(dataobj.getString("NoofCallsfortheATM"));
                        model.setATMId(dataobj.getString("ATMId"));
                        model.setDocketNo(dataobj.getString("DocketNo"));
                        model.setCallType(dataobj.getString("CallType"));
                        model.setSubStatus(dataobj.getString("SubStatus"));



                     String   NoofCallsfortheATM = (dataobj.getString("NoofCallsfortheATM"));

                        Open_Call.add(model);
                        Log.d("", "onPostExecuteDisplayMsg: "+NoofCallsfortheATM);

                        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv_rd);
                        Open_Call_Details_Adapter adapter = new Open_Call_Details_Adapter(Open_Call);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(RepairDetailsActivity.this));
                        recyclerView.setAdapter(adapter);



                    }

                    apiListner.onSuccess();
                    //  progDailog.dismiss();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
               // Utils.showAlertBox("Record Not Found!!", RepairDetailsActivity.this);
                open_call.setVisibility(View.GONE);
                // progDailog.dismiss();

            }
        }

    }


    class SendOpenCallDetail extends CallManagerAsyncTask {


        public SendOpenCallDetail(String action, String reqType, Context context) {
            super(action, reqType, context);

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            JSONObject postParamData = new JSONObject();
            try {


                postParamData.put("ATMId", atm_id);
                postParamData.put("EngineerID", Prefs.with(RepairDetailsActivity.this).getString(UserId, ""));
                postParamData.put("EngineerName",HomeActivity.UserName);
                postParamData.put("ListDetailsModel", Open_Call_Details_Adapter.jsonArrayfor_OpenCallDetail);

                Log.d("", "doInBackground_atm_id: "+atm_id);
                Log.d("", "doInBackgroundHomeActivity.UserName: "+HomeActivity.UserName);

                Log.d("", "doInBackground12345: "+postParamData);

                //   postParamData.put("Password", password);
                return doWork(postParamData);
            } catch (JSONException e) {
                e.printStackTrace();
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
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if (json != null) {
                Utils.Log("JSON Response=========123456789" + json.toString());
                try {
                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Done")) {
                        Utils.Log("response==== Save FOC" + json.toString());
                        String msg = json.getString("ErrorMessage").toString();
                       // Toast.makeText(RepairDetailsActivity.this, ""+msg, Toast.LENGTH_LONG).show();
                      //  showAlertBox1("OpenCallDetails : "+msg ,RepairDetailsActivity.this);



                    } else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        progDailog.dismiss();

                       // showAlertBox1("Follow up : "+followupError+"\n Chargable Image : "+ImageChargeErrormsg+"\n Chargable part :"+ChargableDataErrormsg ,RepairDetailsActivity.this);
                    } else {
                        progDailog.dismiss();
                        //    Utils.showAlertBox(json.getString("ErrorMessage"), RepairDetailsActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    progDailog.dismiss();
                    Utils.showAlertBox(json.getString("ErrorMessage"), RepairDetailsActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }



}
