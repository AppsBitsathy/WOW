package in.bittechpro.apps.wow;


import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class RegisterFragment extends Fragment {

    public RegisterFragment() {
        // Required empty public constructor
    }

    View view;
    FloatingActionButton btn_register;
    String name,number,otp,pass,repass;
    EditText u_name,u_num,u_otp,u_pass,u_repass;
    Button btn_otp;
    Snackbar snackbar;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_register, container, false);

        u_name = view.findViewById(R.id.reg_name);
        u_num = view.findViewById(R.id.reg_num);
        u_otp = view.findViewById(R.id.reg_otp);
        u_pass = view.findViewById(R.id.reg_pass);
        u_repass = view.findViewById(R.id.reg_repass);
        btn_register = view.findViewById(R.id.submit_register);
        btn_otp = view.findViewById(R.id.otp_but);

        u_name.requestFocus();




        btn_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  generateOTP();
                u_otp.setText("1234");
                btn_otp.setVisibility(View.GONE);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        return view;
    }

    private void validate() {

        name = u_name.getText().toString().trim().toUpperCase();
        number = u_num.getText().toString().trim();
        otp = u_otp.getText().toString().trim();
        pass = u_pass.getText().toString().trim();
        repass = u_repass.getText().toString().trim();

        if(name.isEmpty()){

            snackbar = Snackbar.make(view, "Enter Your Name", Snackbar.LENGTH_LONG);
        }
        else if(number.length()!=10){

            snackbar = Snackbar.make(view, "Enter 10 digit mobile number", Snackbar.LENGTH_LONG);
        }
        else if (otp.length()!=4 && otp.isEmpty()){

            snackbar = Snackbar.make(view, "Enter 4 digit OTP", Snackbar.LENGTH_LONG);
        }
        else if (pass.isEmpty() || repass.isEmpty()){
            snackbar = Snackbar.make(view, "PIN cannot be empty", Snackbar.LENGTH_LONG);
        }
        else if (!pass.equals(repass)){
            snackbar = Snackbar.make(view, "PIN not matched", Snackbar.LENGTH_LONG);
        }
        else{
            snackbar = Snackbar.make(view, "Registering User, Please wait", Snackbar.LENGTH_LONG);
            snackbar.show();
            RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());

            final HashMap<String, String> params = new HashMap<>();
            params.put("u_id",number);
            params.put("u_name",name);
            params.put("pass",pass);
            params.put("otp",otp);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlManager.REGISTER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("VOLLEY1", response);
                            try {
                                JSONObject result = new JSONObject(response);
                                if(result.getInt("status")==0){
                                    snackbar = Snackbar.make(view, "Registered Successfully", Snackbar.LENGTH_SHORT);
                                    SharedPreferences.Editor editor = view.getContext().getSharedPreferences(SPrefManager.PREF_NAME, Context.MODE_PRIVATE).edit();
                                    editor.putInt(SPrefManager.LOGGED,1);
                                    editor.putString(SPrefManager.USER_ID,number);
                                    editor.putString(SPrefManager.USER_NAME,name);
                                    editor.putString(SPrefManager.ROLE, "1");
                                    editor.apply();

                                    getActivity().finish();
                                    startActivity(new Intent(getActivity(),MainActivity.class));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                snackbar = Snackbar.make(view, "Unknown error occured, Please try again", Snackbar.LENGTH_LONG);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VOLLEY2", error.toString());
                        }
                    }) {
                @Override
                public String getBodyContentType() {
                   return "application/x-www-form-urlencoded; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        Log.d("VOLLEY3", "getBody: ");
                        RequestHandler requestHandler = new RequestHandler();
                        return requestHandler.sendPostRequest(params).getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        Log.d("VOLLEY4", "getwef: ");
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",params, "utf-8");
                        return null;
                    }
                }
            };

            requestQueue.add(stringRequest);
        }

        snackbar.show();

    }



}
