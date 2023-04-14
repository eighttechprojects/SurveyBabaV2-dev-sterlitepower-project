package com.surveybaba.ADAPTER.ViewLayerPropertiesAdapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.surveybaba.R;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.model.LabelTextModel;
import java.util.ArrayList;

public class AdapterViewLayerProperties extends RecyclerView.Adapter<AdapterViewLayerProperties.ViewHolder> {

    Activity activity;
    ArrayList<LabelTextModel> list;

//------------------------------------------------------- Constructor -------------------------------------------------------------------------------------------------------------------------------------------------

    public AdapterViewLayerProperties(Activity activity, ArrayList<LabelTextModel> list) {
        this.activity = activity;
        this.list = list;
    }

//------------------------------------------------------- onCreate ViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_text_edit_view, parent, false));
    }

//------------------------------------------------------- onBind ViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LabelTextModel bin = list.get(position);
        // Label
        if(!Utility.isEmptyString(bin.getLabel())){
            holder.txtLabel.setText(bin.getLabel());
        }
        else{
            holder.txtLabel.setText("");
        }
        // Value
        if(!Utility.isEmptyString(bin.getValue())){
            holder.txtValue.setText(bin.getValue());
        }
        else{
            holder.txtValue.setText("");
        }
    }

//------------------------------------------------------- Get Item Count -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public int getItemCount() {
        return list.size();
    }

//------------------------------------------------------- View Holder -------------------------------------------------------------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtLabel,txtValue;
        ViewHolder(@NonNull View v) {
            super(v);
            txtLabel = v.findViewById(R.id.txtLabel);
            txtValue = v.findViewById(R.id.txtValue);
        }
    }

}
