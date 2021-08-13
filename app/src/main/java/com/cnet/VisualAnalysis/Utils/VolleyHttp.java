package com.cnet.VisualAnalysis.Utils;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class VolleyHttp {

    private Context context;

    public interface GetRequest {
        void onSuccess(JSONObject jsonObject) throws JSONException;

        void onFailure(VolleyError error);
    }

    public VolleyHttp(Context context) {
        this.context = context;
    }

    public void makeGetRequest(String url, GetRequest request) {
        RequestQueue requestQueue = null;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            //                Request.Method.POST, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    request.onSuccess(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        request.onFailure(error);
                    }

                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(jsonObjectRequest);
        }

    }
}
