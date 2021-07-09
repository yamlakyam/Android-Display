package com.cnet.VisualAnalysis.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.Utils.UtilityFunctions;
import com.cnet.VisualAnalysis.Utils.VolleyHttp;

import org.json.JSONArray;
import org.json.JSONException;

public class VsmCardFragment extends Fragment implements VolleyHttp.GetRequest {

GridView vsmCardGridLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_vsm_card, container, false);
        vsmCardGridLayout=view.findViewById(R.id.vsmCardGridLayout);

        return view;
    }

    @Override
    public void onSuccess(JSONArray jsonArray) {
        try {
            UtilityFunctions.drawVSMCard(jsonArray,0,getContext(),vsmCardGridLayout);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(VolleyError error) {

    }
}