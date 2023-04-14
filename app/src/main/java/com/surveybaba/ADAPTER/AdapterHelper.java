package com.surveybaba.ADAPTER;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.surveybaba.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class AdapterHelper  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<String> helpType;
    // Activity
    Activity activity;
    // Setting Type
    public static final int HELP_TYPE_TEXTVIEW  = 1;
    public static final int HELP_TYPE_IMAGEVIEW = 2;
    // Setting Tag
    public static final String HELP_TYPE_TEXTVIEW_STR  = "textview";
    public static final String HELP_TYPE_IMAGEVIEW_STR    = "imageview";


//------------------------------------------------------- Constructor ----------------------------------------------------------------------------------------------------------------------

    public AdapterHelper(Activity activity, ArrayList<String> helpType) {
        this.activity = activity;
        this.helpType = helpType;
    }

//------------------------------------------------------- Get item View Type ----------------------------------------------------------------------------------------------------------------------

    @Override
    public int getItemViewType(int position) {
        // Switch
        if (helpType.get(position).equalsIgnoreCase(HELP_TYPE_TEXTVIEW_STR))
            return HELP_TYPE_TEXTVIEW;
        // Text
        if (helpType.get(position).equalsIgnoreCase(HELP_TYPE_IMAGEVIEW_STR))
            return HELP_TYPE_IMAGEVIEW;
        return 0;
    }

//------------------------------------------------------- on Create View Holder ----------------------------------------------------------------------------------------------------------------------

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case HELP_TYPE_TEXTVIEW:
                return new AdapterHelper.TextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.switch_setting_layout, parent, false));
            case HELP_TYPE_IMAGEVIEW:
                return new AdapterHelper.ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.text_setting_layout, parent, false));
            default:
                return new AdapterHelper.TempHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, parent, false));
        }
    }

//------------------------------------------------------- on Bind View Holder ----------------------------------------------------------------------------------------------------------------------

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String bin = helpType.get(position);
        switch (getItemViewType(position)) {

//------------------------------------------------------- Text  ----------------------------------------------------------------------------------------------------------------------

            case HELP_TYPE_TEXTVIEW:
                if (holder instanceof AdapterHelper.TextViewHolder) {
                    AdapterHelper.TextViewHolder textViewHolder = (AdapterHelper.TextViewHolder) holder;
                    textViewHolder.tv.setText(bin);
                }
                break;

            case HELP_TYPE_IMAGEVIEW:
                if(holder instanceof AdapterHelper.ImageViewHolder){
                    AdapterHelper.ImageViewHolder imageViewHolder = (AdapterHelper.ImageViewHolder) holder;
                }
                break;
        }
    }

//------------------------------------------------------- Get Item Count ----------------------------------------------------------------------------------------------------------------------

    @Override
    public int getItemCount() {
        return helpType.size();
    }

//------------------------------------------------------- View Holder ----------------------------------------------------------------------------------------------------------------------

    private static class TextViewHolder extends RecyclerView.ViewHolder {

        TextView tv;
        TextViewHolder(@NonNull View v) {
            super(v);
            tv = v.findViewById(R.id.txtSettingName);
        }
    }

    private static class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        ImageViewHolder(@NonNull View v) {
            super(v);
            image = v.findViewById(R.id.txtSettingName);
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
