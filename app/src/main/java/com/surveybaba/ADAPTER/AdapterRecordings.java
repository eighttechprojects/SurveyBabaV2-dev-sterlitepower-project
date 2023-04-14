package com.surveybaba.ADAPTER;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.surveybaba.R;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.setting.model.BinRecordProfile;

import java.util.List;

/**
 * Created by developer on 21/12/2017.
 */

public class AdapterRecordings extends RecyclerView.Adapter<AdapterRecordings.ViewHolder> {

    private Activity context;
    private List<BinRecordProfile> list;
    private int indexGpsAltitudeUnit;
    private String unitGpsAltitude;
    onCustomRowListener onCustomRowListener;

    public interface onCustomRowListener
    {
        void onSetProfile(int position);
        void onRedirectToSetting(int position);
    }

    public AdapterRecordings(Activity context, List<BinRecordProfile> list, onCustomRowListener onCustomRowListener) {
        this.context = context;
        this.list = list;
        this.onCustomRowListener = onCustomRowListener;
        indexGpsAltitudeUnit = Integer.parseInt(Utility.getSavedData(context, Utility.UNIT_GPS_ALTITUDE_DATA));
        unitGpsAltitude = Utility.getUNIT_ALTITUDE(context)[indexGpsAltitudeUnit-1];
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.custom_row_recording_profile, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BinRecordProfile bin = list.get(position);

        String distance = Utility.convertDistanceFromMeter(context, bin.getDistanceInterval());
        String gpsAccuracy = ""+Utility.convertGpsAltitudeFromMeter(bin.getGpsAccuracy(), indexGpsAltitudeUnit);
        holder.txtTitle.setText(bin.getName());
        holder.txtDetails.setText(distance + " | "+ bin.getTimeInterval() + " s | " + Utility.convert2decimalPoints(Double.parseDouble(gpsAccuracy)) + " " + unitGpsAltitude);

        // Set on Profile
        holder.llSetProfile.setTag(position);
        holder.llSetProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                onCustomRowListener.onSetProfile(pos);
            }
        });

        // When user Set on Setting
        holder.imgSettings.setTag(position);
        holder.imgSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                onCustomRowListener.onRedirectToSetting(pos);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTitle;
        private TextView txtDetails;
        private LinearLayout llSetProfile;
        private ImageView imgSettings;

        ViewHolder(View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDetails = itemView.findViewById(R.id.txtDetails);
            llSetProfile = itemView.findViewById(R.id.llSetProfile);
            imgSettings = itemView.findViewById(R.id.imgSettings);
        }

    }

}
