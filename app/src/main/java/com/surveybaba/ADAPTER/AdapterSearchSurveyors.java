package com.surveybaba.ADAPTER;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.surveybaba.R;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.model.UserSearchModel;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

public class AdapterSearchSurveyors extends RecyclerView.Adapter<AdapterSearchSurveyors.ViewHolder> {

    // Context
    Context context;
    // List
    ArrayList<UserSearchModel> list;

//------------------------------------------------------- Constructor -------------------------------------------------------------------------------------------------------------------------------------------------

    public AdapterSearchSurveyors(Context context, ArrayList<UserSearchModel> list) {
        this.context = context;
        this.list = list;
    }
//------------------------------------------------------- on CreateViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public AdapterSearchSurveyors.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_search_survey_adapter_view, parent, false);
        return new ViewHolder(view);
    }

//-------------------------------------------------------  on BindViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull AdapterSearchSurveyors.ViewHolder holder, int position) {

        UserSearchModel data = list.get(position);
        holder.userName.setText(!Utility.isEmptyString(data.getName()) ? data.getName() : "" );
        holder.userEmailID.setText(!Utility.isEmptyString(data.getEmailId()) ? data.getEmailId() : "");
        holder.userPhoneNo.setText(!Utility.isEmptyString(data.getPhone()) ? data.getPhone() : "");
        holder.userDesignation.setText(!Utility.isEmptyString(data.getDesignation()) ? data.getDesignation() : "");
        // Description Not Null then
        if(!Utility.isEmptyString(data.getDescription())){
            holder.descriptionLayout.setVisibility(View.VISIBLE);
            holder.userDescription.setText(data.getDescription());
        }
        // Description Null
        else{
            holder.descriptionLayout.setVisibility(View.GONE);
        }
        // Photo
        Glide.with(context).load(Utility.decodeBase64Image(data.getPhoto())).centerCrop().placeholder(R.drawable.image_load_bar).error(R.drawable.icon_user_profile).into(holder.userImageView);

        // Click on Phone Number
        holder.userPhoneNo.setOnClickListener(view -> {
            if(!Utility.isEmptyString(data.getPhone())){
                Utility.openCallDial(context,data.getPhone());
            }
        });

        // Click on Email ID
        holder.userEmailID.setOnClickListener(view -> {
            if(!Utility.isEmptyString(data.getEmailId())){
                Utility.openEmail(context,data.getEmailId());
            }
        });

        holder.userImageView.setOnClickListener(view -> {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.profileimage_zoom_layout);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ImageView imageView = dialog.findViewById(R.id.dialogbox_image);
            Glide.with(context).load(Utility.decodeBase64Image(data.getPhoto())).error(R.drawable.icon_user_profile).into(imageView);
            dialog.show();
        });

    }

//------------------------------------------------------- ItemCount -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public int getItemCount() {
        return list.size();
    }

//------------------------------------------------------- ViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ShapeableImageView userImageView;
        TextView userName,userEmailID,userPhoneNo,userDesignation,userDescription,userLocationDialog;
        Button viewButton;
        LinearLayout descriptionLayout,viewButtonLayout,userLocationLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImageView     = itemView.findViewById(R.id.userImageView);
            userName          = itemView.findViewById(R.id.userName);
            userEmailID       = itemView.findViewById(R.id.userEmailID);
            userPhoneNo       = itemView.findViewById(R.id.userPhoneNo);
            userDesignation   = itemView.findViewById(R.id.userDesignation);
            userDescription   = itemView.findViewById(R.id.userDescription);
            userLocationDialog = itemView.findViewById(R.id.userLocation);
            viewButton        = itemView.findViewById(R.id.viewButton);
            descriptionLayout = itemView.findViewById(R.id.descriptionLayout);

            viewButtonLayout  = itemView.findViewById(R.id.viewButtonLayout);
            userLocationLayout = itemView.findViewById(R.id.userLocationLayout);

            viewButtonLayout.setVisibility(View.GONE);
            userLocationLayout.setVisibility(View.GONE);


        }
    }

}
