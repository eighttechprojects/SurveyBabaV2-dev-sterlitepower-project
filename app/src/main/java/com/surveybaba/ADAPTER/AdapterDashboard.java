package com.surveybaba.ADAPTER;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.surveybaba.R;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.model.DashBoardModule;

import java.util.ArrayList;


public class AdapterDashboard extends RecyclerView.Adapter<AdapterDashboard.ViewHolder> {
    // Context
    Context context;
    // List
    ArrayList<DashBoardModule> dashBoardModules;
    // Interface
    AdapterDashBoardListener adapterDashBoardListener;
    // MODULES
    public static final String SURVEY_MODULES = "Survey";
    public static final String PROJECT_MODULES= "project";
    public static final String GPS_TRACKING_MODULES = "gps_tracking";
    public static final String GIS_SURVEY_MODULES = "gis_survey";
    public static final String MANAGE_ORGANISATION_MODULES = "ManageOrganisation";
    public static final String CAMERA = "Camera";
    public static final String BLUETOOTH  = "Bluetooth";
    public static final String SETTING = "Setting";
    public static final String TIMELINE = "TimeLine";
    public static final String BUSINESS_HUB = "BusinessHub";
    public static final String Users = "Users";

    public static final String PROJECT_LIST = "Projects List";
    public static final String GIS_SURVEY_LIST = "GIS Survey List";
    public static final String RESURVEY = "Resurvey";


//------------------------------------------------------- Constructor -------------------------------------------------------------------------------------------------------------------------------------------------

    public AdapterDashboard(Context context, ArrayList<DashBoardModule> dashBoardModules, AdapterDashBoardListener adapterDashBoardListener){
        this.context = context;
        this.dashBoardModules = dashBoardModules;
        this.adapterDashBoardListener = adapterDashBoardListener;
    }

//------------------------------------------------------- on CreateViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_dashboard_adapter_layout, parent, false);
        return new ViewHolder(view);
    }


//-------------------------------------------------------  on BindViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String modulesName = dashBoardModules.get(position).getModule_name();
        holder.dashBoardItemName.setText(modulesName);
        String modules = dashBoardModules.get(position).getModule();

        // Set Modules Image
        switch (modules){

            case SURVEY_MODULES:
                holder.dashBoardItemImage.setImageResource(R.drawable.icon_survey);
                break;

            case PROJECT_MODULES:
                holder.dashBoardItemImage.setImageResource(R.drawable.icon_project_management);
                break;

            case GPS_TRACKING_MODULES:
                holder.dashBoardItemImage.setImageResource(R.drawable.icon_gps_tracking);
                break;

            case GIS_SURVEY_MODULES:
                holder.dashBoardItemImage.setImageResource(R.drawable.icon_gis_survey);
                break;

            case MANAGE_ORGANISATION_MODULES:
                holder.dashBoardItemImage.setImageResource(R.drawable.icon_manage_organisation);
                break;

            case CAMERA:
                holder.dashBoardItemImage.setImageResource(R.drawable.icon_camera);
                break;

            case BLUETOOTH:
                holder.dashBoardItemImage.setImageResource(R.drawable.icon_bluetooth);
                break;

            case SETTING:
                holder.dashBoardItemImage.setImageResource(R.drawable.icon_setting);
                break;

            case TIMELINE:
                holder.dashBoardItemImage.setImageResource(R.drawable.icon_timeline);
                break;

            case BUSINESS_HUB:
                holder.dashBoardItemImage.setImageResource(R.drawable.icon_business_hub);
                break;

            case Users:
                holder.dashBoardItemImage.setImageResource(R.drawable.icon_admin);
                break;

            case PROJECT_LIST:
                holder.dashBoardItemImage.setImageResource(R.drawable.icon_project_management);
                break;

            case GIS_SURVEY_LIST:
                holder.dashBoardItemImage.setImageResource(R.drawable.icon_project_management);
                break;

            case RESURVEY:
                holder.dashBoardItemImage.setImageResource(R.drawable.icon_gis_survey);
                break;



            default:
                holder.dashBoardItemImage.setImageResource(R.drawable.icon_project_management);
                break;
        }
    }

//------------------------------------------------------- ItemCount -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public int getItemCount() {
        return dashBoardModules.size();
    }

//------------------------------------------------------- ViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView dashBoardItemImage;
        TextView dashBoardItemName;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            dashBoardItemName = itemView.findViewById(R.id.dashBoardItemName);
            dashBoardItemImage = itemView.findViewById(R.id.dashBoardItemImage);
            // Item Click Listener
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    adapterDashBoardListener.OnItemClick(position);
                }
            });
        }
    }

//------------------------------------------------------- Interface -------------------------------------------------------------------------------------------------------------------------------------------------

    public interface AdapterDashBoardListener{
        void OnItemClick(int position);
    }

}
