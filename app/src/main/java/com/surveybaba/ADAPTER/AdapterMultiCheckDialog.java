package com.surveybaba.ADAPTER;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.surveybaba.ProfileActivitySettingsDetails;
import com.surveybaba.R;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.model.BinTopPanel;
import com.surveybaba.setting.model.BinRecordProfile;

import java.util.List;

/**
 * Created by developer on 21/12/2017.
 */

public class AdapterMultiCheckDialog extends RecyclerView.Adapter<AdapterMultiCheckDialog.ViewHolder> {

    private Activity context;
    private List<BinTopPanel> list;
    private AlertDialog alert;

    public AdapterMultiCheckDialog(Activity context, List<BinTopPanel> list, AlertDialog alert) {
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
        View itemView = LayoutInflater.from(context).inflate(R.layout.custom_row_top_panel, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BinTopPanel bin = list.get(position);
        holder.txtTitle.setText(bin.getName());
        holder.imgTickUntick.setImageResource(bin.isChecked()?R.drawable.tick:R.drawable.untick);
        holder.imgTickUntick.setTag(position);

        holder.imgTickUntick.setOnClickListener(v -> {
            int pos = (int) v.getTag();
            if(list.get(pos).getName().equalsIgnoreCase(context.getString(R.string.measurement))) {
                list.get(pos).setChecked(!list.get(pos).isChecked());
                Utility.saveData(context, Utility.TOP_PANEL.MEASURE, list.get(pos).isChecked());
                notifyDataSetChanged();
            }
            else if(list.get(pos).getName().equalsIgnoreCase(context.getString(R.string.layers))) {
                list.get(pos).setChecked(!list.get(pos).isChecked());
                Utility.saveData(context, Utility.TOP_PANEL.LAYERS, list.get(pos).isChecked());
                notifyDataSetChanged();
            }
            else if(list.get(pos).getName().equalsIgnoreCase(context.getString(R.string.gps_compass))) {
                list.get(pos).setChecked(!list.get(pos).isChecked());
                Utility.saveData(context, Utility.TOP_PANEL.GPS_COMPASS, list.get(pos).isChecked());
                notifyDataSetChanged();
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTitle;
        private LinearLayout llParent;
        private ImageView imgTickUntick;

        ViewHolder(View itemView) {
            super(itemView);

            llParent = itemView.findViewById(R.id.llParent);
            imgTickUntick = itemView.findViewById(R.id.imgTickUntick);
            txtTitle = itemView.findViewById(R.id.txtTitle);
        }

    }

}
