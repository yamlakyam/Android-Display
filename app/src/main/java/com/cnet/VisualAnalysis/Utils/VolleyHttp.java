package com.cnet.VisualAnalysis.Utils;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.cnet.VisualAnalysis.SecondActivity;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class VolleyHttp {

    private Context context;

    public interface GetRequest {
        void onSuccess(JSONArray jsonArray);

        void onFailure(VolleyError error);
    }

    public VolleyHttp(Context context) {
        this.context = context;
    }

    public void makeGetRequest(String url, GetRequest request) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
//                Request.Method.POST, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                request.onSuccess(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        request.onFailure(error);
                    }

                })
       /* {

            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("imei", SecondActivity.myAndroidDeviceId);
                return super.getParams();
            }
        }
        */
        ;

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonArrayRequest);
    }
}
