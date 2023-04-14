package com.surveybaba.ADAPTER;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.surveybaba.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class AdapterSetting  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<String> systemTypes;
    // Activity
    Activity activity;
    // Setting Type
    public static final int SETTING_TYPE_SWITCH = 1;
    public static final int SETTING_TYPE_TEXT   = 2;
    // Setting Tag
    public static final String SETTING_TYPE_SWITCH_STR  = "switch";
    public static final String SETTING_TYPE_TEXT_STR    = "text";


//------------------------------------------------------- Constructor ----------------------------------------------------------------------------------------------------------------------

    public AdapterSetting(Activity activity, ArrayList<String> systemTypes) {
        this.activity =  activity;
        this.systemTypes = systemTypes;
    }

//------------------------------------------------------- Get item View Type ----------------------------------------------------------------------------------------------------------------------

    @Override
    public int getItemViewType(int position) {
        // Switch
        if (systemTypes.get(position).equalsIgnoreCase(SETTING_TYPE_SWITCH_STR))
            return SETTING_TYPE_SWITCH;
        // Text
        if (systemTypes.get(position).equalsIgnoreCase(SETTING_TYPE_TEXT_STR))
            return SETTING_TYPE_TEXT;
        return 0;
    }

//------------------------------------------------------- on Create View Holder ----------------------------------------------------------------------------------------------------------------------

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case SETTING_TYPE_SWITCH:
                return new SwitchSettingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.switch_setting_layout, parent, false));
            case SETTING_TYPE_TEXT:
                return new TextSettingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.text_setting_layout, parent, false));
            default:
                return new TempHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, parent, false));
        }
    }

//------------------------------------------------------- on Bind View Holder ----------------------------------------------------------------------------------------------------------------------

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String bin = systemTypes.get(position);
        switch (getItemViewType(position)) {

//------------------------------------------------------- Text  ----------------------------------------------------------------------------------------------------------------------

            case SETTING_TYPE_SWITCH:
                if (holder instanceof SwitchSettingViewHolder) {
                    SwitchSettingViewHolder  switchSettingViewHolder = (SwitchSettingViewHolder) holder;

                    switchSettingViewHolder.switchButton.setOnClickListener(view -> {
                        if(switchSettingViewHolder.switchButton.isChecked()){
                            Toast.makeText(activity, "on", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(activity, "off", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;

            case SETTING_TYPE_TEXT:
                if(holder instanceof TextSettingViewHolder){
                    TextSettingViewHolder textSettingViewHolder = (TextSettingViewHolder) holder;
                }
                break;
        }
    }

//------------------------------------------------------- Get Item Count ----------------------------------------------------------------------------------------------------------------------

    @Override
    public int getItemCount() {
        return systemTypes.size();
    }

//------------------------------------------------------- View Holder ----------------------------------------------------------------------------------------------------------------------

    private static class SwitchSettingViewHolder extends RecyclerView.ViewHolder {

        TextView txtSettingName;
        SwitchCompat switchButton;
        SwitchSettingViewHolder(@NonNull View v) {
            super(v);
            txtSettingName = v.findViewById(R.id.txtSettingName);
            switchButton   = v.findViewById(R.id.switchButton);
        }
    }

    private static class TextSettingViewHolder extends RecyclerView.ViewHolder {

        LinearLayout textLayout;
        TextView txtLarge,txtSmall;
        ImageView settingIcon;
        TextSettingViewHolder(@NonNull View v) {
            super(v);
            textLayout  = v.findViewById(R.id.textLayout);  // -> main layout user click
            txtLarge    = v.findViewById(R.id.txtLarge);    // -> Large txt name
            txtSmall    = v.findViewById(R.id.txtSmall);    // -> small txt name
            settingIcon = v.findViewById(R.id.settingIcon); // -> setting icon
        }
    }

    private static class TempHolder extends RecyclerView.ViewHolder {

        TextView txtSpinner;
        TempHolder(@NonNull View itemView) {
            super(itemView);
            txtSpinner = itemView.findViewById(R.id.text1);
        }
    }



}
