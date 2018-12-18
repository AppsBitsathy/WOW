package in.bittechpro.apps.wow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;


public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }

    View view;
    FloatingActionButton btn_select;
    String name, number , pass;
    EditText u_num,u_pass;
    Snackbar snackbar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_login, container, false);

        btn_select = view.findViewById(R.id.submit_select);

        u_num = view.findViewById(R.id.login_num);
        u_pass = view.findViewById(R.id.login_pass);

        u_num.requestFocus();

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        return view;
    }

    private void validate() {

        number = u_num.getText().toString().trim();
        pass = u_pass.getText().toString().trim();

        if (number.length() != 10) {

            snackbar = Snackbar.make(view, "Enter 10 digit mobile number", Snackbar.LENGTH_LONG);
        }
        else if (pass.length() !=4) {

            snackbar = Snackbar.make(view, "PIN must be 4 digit ", Snackbar.LENGTH_LONG);
        }
        else {
            snackbar = Snackbar.make(view, "Logging User, Please wait", Snackbar.LENGTH_LONG);
            snackbar.show();

            RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());

            final HashMap<String, String> params = new HashMap<>();
            params.put("u_id",number);
            params.put("pass",pass);


            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlManager.LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("VOLLEY1", response);
                            try {
                                JSONObject result = new JSONObject(response);
                                if(result.getInt("status")==0){

                                    if (result.getBoolean("pass")){

                                        snackbar = Snackbar.make(view, "Login Successfully", Snackbar.LENGTH_SHORT);

                                        SharedPreferences.Editor editor = getContext().getSharedPreferences(SPrefManager.PREF_NAME, Context.MODE_PRIVATE).edit();
                                        editor.putInt(SPrefManager.LOGGED,1);
                                        editor.putString(SPrefManager.USER_ID,number);
                                        editor.putString(SPrefManager.USER_NAME,result.getString("name"));
                                        editor.putString(SPrefManager.ROLE, String.valueOf(result.getInt("role")));
                                        editor.apply();

                                        getActivity().finish();
                                        startActivity(new Intent(getActivity(),MainActivity.class));
                                    }
                                    else {
                                        snackbar = Snackbar.make(view, "Wrong Password", Snackbar.LENGTH_SHORT);
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                snackbar = Snackbar.make(view, "Unknown error occured, Please try again", Snackbar.LENGTH_LONG);
                            }

                            snackbar.show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Snackbar.make(view,"Network Error",BaseTransientBottomBar.LENGTH_SHORT);
                            Log.e("VOLLEY2", error.toString());
                        }
                    })
            {
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
