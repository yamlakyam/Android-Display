package com.cnet.VisualAnalysis.Utils;

import android.content.Context;
import android.nfc.tech.NfcF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cnet.VisualAnalysis.Data.VSMCard;
import com.cnet.VisualAnalysis.R;

import java.text.NumberFormat;
import java.util.ArrayList;

public class VSMCardGVAdapter extends ArrayAdapter<VSMCard> {

    public VSMCardGVAdapter(@NonNull Context context, ArrayList<VSMCard> vsmCardArrayList) {
        super(context, 0, vsmCardArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listedView = convertView;

        if (listedView == null) {
            listedView = LayoutInflater.from(getContext()).inflate(R.layout.vsm_card_element, parent, false);
        }

        VSMCard vsmCard = getItem(position);

        TextView vsmName = listedView.findViewById(R.id.vsmCardNameTxt);
        TextView vsmOutlet = listedView.findViewById(R.id.vsmCardoutletTxt);
        TextView vsmLastActive = listedView.findViewById(R.id.vsmCardlastActive);
        TextView vsmVcount = listedView.findViewById(R.id.vsmCardVcount);
        TextView vsmTsale = listedView.findViewById(R.id.vsmCardTotalSale);
        TextView vsmDistributor = listedView.findViewById(R.id.distributorVsmCard);


        String distributorName = vsmCard.getDistributorName();
        String preciseOrgName;
        if (distributorName.length() > 30)
            preciseOrgName = distributorName.substring(0, 20) + "...";
        else
            preciseOrgName = distributorName;

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);


        vsmName.setText(vsmCard.getVsi());
        vsmOutlet.setText(String.valueOf(vsmCard.getSalesOutLateCount()));
        vsmLastActive.setText(UtilityFunctions.formatTimeToString(vsmCard.getLastSeen()));
        vsmVcount.setText(String.valueOf(vsmCard.getvCount()));
        vsmTsale.setText(numberFormat.format(vsmCard.getTotalSales()));
        vsmDistributor.setText(preciseOrgName);

        return listedView;
    }
}
