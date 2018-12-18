package in.bittechpro.apps.wow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

public class CorporateFragment extends Fragment {

    public CorporateFragment() {
        // Required empty public constructor
    }

    View view;
    ListView listView;

    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_corporate, container, false);

        listView = view.findViewById(R.id.cor_list);

        getMachine();

        return view;
    }

    private void getMachine() {

        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());

        sharedPreferences =view.getContext().getSharedPreferences(SPrefManager.PREF_NAME,Context.MODE_PRIVATE);

        final HashMap<String, String> params = new HashMap<>();
        params.put("u_id",sharedPreferences.getString(SPrefManager.USER_ID,"1"));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlManager.GET_COR_MACHINE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("VOLLEY1", response);
                        try {
                            JSONObject result = new JSONObject(response);
                            if(result.getInt("status")==0) {

                                int total = result.getInt("total");

                                if (total > 0) {

                                    listView.setVisibility(View.VISIBLE);

                                    String[] m_name = new String[total];
                                    String[] m_id = new String[total];
                                    String[] lati = new String[total];
                                    String[] longi = new String[total];
                                    int[] full_cap= new int[total];
                                    int[] cur_cap = new int[total];
                                    double[] capacity = new double[total];


                                    JSONArray res_m_name = result.getJSONArray("m_name");
                                    JSONArray res_m_id = result.getJSONArray("m_id");
                                    JSONArray res_lati = result.getJSONArray("lat");
                                    JSONArray res_longi = result.getJSONArray("long");
                                    JSONArray res_full_cap = result.getJSONArray("full_cap");
                                    JSONArray res_cur_cap = result.getJSONArray("cur_cap");


                                    for (int r = 0; r < total; r++) {

                                        m_name[r] = res_m_name.get(r).toString();
                                        m_id[r] = res_m_id.get(r).toString();
                                        lati[r] = res_lati.get(r).toString();
                                        longi[r] = res_longi.get(r).toString();
                                        full_cap[r] = (int)res_full_cap.get(r);
                                        cur_cap[r] = (int)res_cur_cap.get(r);

                                        capacity[r]  = ((double) cur_cap[r] / (double)full_cap[r]) * 100;

                                        Log.d("ooooo", capacity[r] + "="+cur_cap[r]+"/"+full_cap[r]);


                                    }

                                    listView.setAdapter(new CorporateListAdapter(getActivity(),m_name,full_cap,cur_cap,capacity));
                                    Snackbar.make(view, "ok", Snackbar.LENGTH_LONG);
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

}
