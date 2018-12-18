package in.bittechpro.apps.wow;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class RewardFragment extends Fragment {


    public RewardFragment() {
        // Required empty public constructor
    }

    View view;

    TextView main_name,main_num,main_count;

    SharedPreferences sharedpreferences;

    ListView reward_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_reward, container, false);
        main_name = view.findViewById(R.id.main_name);
        main_num = view.findViewById(R.id.main_number);
        main_count = view.findViewById(R.id.main_count);
        reward_list = view.findViewById(R.id.reward_list);

        sharedpreferences = view.getContext().getSharedPreferences(SPrefManager.PREF_NAME, Context.MODE_PRIVATE);

        main_name.setText(sharedpreferences.getString(SPrefManager.USER_NAME,"Unknown Name"));
        main_num.setText(sharedpreferences.getString(SPrefManager.USER_ID,"Unknown Mobile Number"));


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getRewards();
    }

    private void getRewards() {

        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());

        final HashMap<String, String> params = new HashMap<>();
        params.put("u_id",sharedpreferences.getString(SPrefManager.USER_ID,"0"));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlManager.GET_REWARD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("VOLLEY1", response);
                        try {
                            JSONObject result = new JSONObject(response);
                            if(result.getInt("status")==0){

                                int total = result.getInt("total");

                                if (total>0){

                                    reward_list.setVisibility(View.VISIBLE);

                                    SharedPreferences.Editor editor = view.getContext().getSharedPreferences(SPrefManager.PREF_NAME, Context.MODE_PRIVATE).edit();


                                    String[] m_name = new String[total];
                                    String[] date = new String [total];
                                    int[] reward = new int[total];
                                    int[] bottle = new int[total];
                                    int total_reward=0;

                                    JSONArray res_m_name = result.getJSONArray("machine");
                                    JSONArray res_date = result.getJSONArray("stamp");
                                    JSONArray res_bottle = result.getJSONArray("bottle");
                                    JSONArray res_reward = result.getJSONArray("reward");


                                    for (int r=0;r<total;r++){

                                        m_name[r] = res_m_name.get(r).toString();
                                        date[r] = res_date.get(r).toString();
                                        reward[r] = (int) res_reward.get(r);
                                        total_reward+=reward[r];
                                        bottle[r] = (int) res_bottle.get(r);

                                    }

                                    editor.putInt(SPrefManager.TOTAL_REWARD,total_reward);
                                    editor.apply();
                                    main_count.setText(String.valueOf(total_reward));
                                    reward_list.setAdapter(new RewardListAdapter(getActivity(),m_name,date,reward,bottle));

                                }else {

                                    main_count.setText("0");
                                }




                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Snackbar.make(view, "Unknown error occured, Please try again", Snackbar.LENGTH_LONG);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(view,"Network Error",BaseTransientBottomBar.LENGTH_SHORT);
                        Log.e("VOLLEY2", error.toString());
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
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

}
