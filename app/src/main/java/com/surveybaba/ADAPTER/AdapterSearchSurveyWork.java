package com.surveybaba.ADAPTER;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.surveybaba.R;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.model.UserSearchModel;
import com.surveybaba.model.UserSearchWorkModel;

import java.util.ArrayList;

public class AdapterSearchSurveyWork extends RecyclerView.Adapter<AdapterSearchSurveyWork.ViewHolder> {

    // Context
    Context context;
    // List
    ArrayList<UserSearchWorkModel> list;

//------------------------------------------------------- Constructor -------------------------------------------------------------------------------------------------------------------------------------------------

    public AdapterSearchSurveyWork(Context context, ArrayList<UserSearchWorkModel> list) {
        this.context = context;
        this.list = list;
    }
//------------------------------------------------------- on CreateViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public AdapterSearchSurveyWork.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_search_survey_work_adapter, parent, false);
        return new ViewHolder(view);
    }

//-------------------------------------------------------  on BindViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull AdapterSearchSurveyWork.ViewHolder holder, int position) {

        UserSearchWorkModel data = list.get(position);
        holder.userName.setText(!Utility.isEmptyString(data.getName()) ? data.getName() : "" );
        holder.userLocation.setText(!Utility.isEmptyString(data.getLocation()) ? data.getLocation() : "" );
        holder.userPosted.setText(!Utility.isEmptyString(data.getPostedBy()) ? data.getPostedBy() : "" );
        holder.userDate.setText(!Utility.isEmptyString(data.getDate()) ? data.getDate() : "" );

        // Description Not Null then
        if(!Utility.isEmptyString(data.getDescription())){
            holder.descriptionLayout.setVisibility(View.VISIBLE);
            holder.userDescription.setText(data.getDescription());
        }
        // Description Null
        else{
            holder.descriptionLayout.setVisibility(View.GONE);
        }
        // ButtonClick
        holder.viewButton.setOnClickListener(view -> {
            showPopUp(data);
        });

    }

//------------------------------------------------------- ItemCount -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public int getItemCount() {
        return list.size();
    }

//------------------------------------------------------- ViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView userName,userDescription,userLocation,userPosted,userDate;
        Button viewButton;
        LinearLayout descriptionLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Text View
            userName           = itemView.findViewById(R.id.userName);
            userLocation       = itemView.findViewById(R.id.userLocation);
            userPosted         = itemView.findViewById(R.id.userPosted);
            userDate           = itemView.findViewById(R.id.userDate);
            userDescription    = itemView.findViewById(R.id.userDescription);
            // Button
            viewButton         = itemView.findViewById(R.id.viewButton);
            // Layout
            descriptionLayout  = itemView.findViewById(R.id.descriptionLayout);
        }
    }

//------------------------------------------------------- show User Details PopUp -------------------------------------------------------------------------------------------------------------------------------------------------

    private void showPopUp(UserSearchWorkModel data){
        // Dialog Box
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_searchwork_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        // Close
        ImageView userCloseDialog            = dialog.findViewById(R.id.userCloseDialog);
        userCloseDialog.setOnClickListener(view -> dialog.dismiss());

        TextView userNameDialog          = dialog.findViewById(R.id.userNameDialog);
        TextView userDescriptionDialog   = dialog.findViewById(R.id.userDescriptionDialog);
        TextView userLocationDialog      = dialog.findViewById(R.id.userLocationDialog);
        TextView userPostedDialog        = dialog.findViewById(R.id.userPostedDialog);
        TextView userDateDialog          = dialog.findViewById(R.id.userDateDialog);

        LinearLayout userNameLayoutDialog        = dialog.findViewById(R.id.userNameLayoutDialog);
        LinearLayout userLocationLayoutDialog    = dialog.findViewById(R.id.userLocationLayoutDialog);
        LinearLayout userDescriptionLayoutDialog = dialog.findViewById(R.id.userDescriptionLayoutDialog);
        LinearLayout userPostedLayoutDialog      = dialog.findViewById(R.id.userPostedLayoutDialog);
        LinearLayout userDateLayoutDialog        = dialog.findViewById(R.id.userDateLayoutDialog);

        // Work Name
        userNameDialog.setText(!Utility.isEmptyString(data.getName()) ? data.getName() : "" );
        // Location
        userLocationDialog.setText(!Utility.isEmptyString(data.getLocation()) ? data.getLocation() : "");
        // Posted By
        userPostedDialog.setText(!Utility.isEmptyString(data.getPostedBy()) ? data.getPostedBy() : "" );
        // Date
        userDateDialog.setText(!Utility.isEmptyString(data.getDate()) ? data.getDate() : "" );
        // Description
        if(!Utility.isEmptyString(data.getDescription())){
            userDescriptionLayoutDialog.setVisibility(View.VISIBLE);
            userDescriptionDialog.setText(data.getDescription());
        }
        else{
            userDescriptionLayoutDialog.setVisibility(View.GONE);
        }
        dialog.show();
    }

}
