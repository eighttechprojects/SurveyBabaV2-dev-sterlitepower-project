package com.surveybaba.ADAPTER;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.surveybaba.ProfileActivitySettingsDetails;
import com.surveybaba.R;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.setting.model.BinRecordProfile;

import java.util.List;

/**
 * Created by developer on 21/12/2017.
 */

public class AdapterRecordingsDialog extends RecyclerView.Adapter<AdapterRecordingsDialog.ViewHolder> {

    private Activity context;
    private List<BinRecordProfile> list;
    private AlertDialog alert;

    public AdapterRecordingsDialog(Activity context, List<BinRecordProfile> list, AlertDialog alert) {
        this.context = context;
        this.list = list;
        this.alert = alert;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.custom_row_recording_dialog, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BinRecordProfile bin = list.get(position);
        holder.txtTitle.setText(bin.getName());
        holder.txtCategory.setText(bin.getCategory());
        holder.txtCategory.setVisibility(bin.isHeader()?View.VISIBLE:View.GONE);
        holder.llProfileActivity.setTag(position);
        holder.llProfileActivity.setOnClickListener(v -> {
            if(alert!=null)
                alert.dismiss();
            int position1 = (int) v.getTag();
            Intent intent = new Intent(context, ProfileActivitySettingsDetails.class);
            intent.putExtra(Utility.key_profile_activity_data, new Gson().toJson(list.get(position1)));
            intent.putExtra(Utility.key_profile_is_add, true);
            context.startActivityForResult(intent, Utility.REQUEST_CODE_PROFILE_ADD);
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTitle, txtCategory;
        private LinearLayout llProfileActivity;

        ViewHolder(View itemView) {
            super(itemView);

            llProfileActivity = itemView.findViewById(R.id.llProfileActivity);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtCategory = itemView.findViewById(R.id.txtCategory);
        }

    }

}
