package com.surveybaba.ADAPTER;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.surveybaba.R;
import java.util.ArrayList;

public class AdapterBusinessHub extends RecyclerView.Adapter<AdapterBusinessHub.ViewHolder> {
    // Context
    Context context;
    // List
    ArrayList<String> dashBoardModules;
    // Interface
    AdapterBusinessHub.AdapterBusinessHubListener adapterBusinessHubListener;
    // Business Hub
    public static final String SEARCH_SURVEYORS        = "Search Surveyors";
    public static final String SEARCH_SURVEYING_AGENCY = "Search Surveyors Agency";
    public static final String POST_SURVEY_WORK        = "Post Survey Work";
    public static final String SEARCH_SURVEY_WORK      = "Search Survey Work";
    public static final String RENT_YOUR_EQUIPMENT     = "Rent Your Equipment";
    public static final String HIRE_EQUIPMENT          = "Hire Equipment";


//------------------------------------------------------- Constructor -------------------------------------------------------------------------------------------------------------------------------------------------

    public AdapterBusinessHub(Context context, ArrayList<String> dashBoardModules, AdapterBusinessHub.AdapterBusinessHubListener adapterBusinessHubListener){
        this.context = context;
        this.dashBoardModules = dashBoardModules;
        this.adapterBusinessHubListener = adapterBusinessHubListener;
    }

//------------------------------------------------------- on CreateViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public AdapterBusinessHub.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_dashboard_adapter_layout, parent, false);
        return new AdapterBusinessHub.ViewHolder(view);
    }

//-------------------------------------------------------  on BindViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull AdapterBusinessHub.ViewHolder holder, int position) {

        String modulesName = dashBoardModules.get(position);
        holder.dashBoardItemName.setText(modulesName);

        // Set Modules Image
        switch (modulesName){


            case SEARCH_SURVEYORS:
                holder.dashBoardItemImage.setImageResource(R.drawable.icon_search_surveyor);
                break;

            case SEARCH_SURVEYING_AGENCY:
                holder.dashBoardItemImage.setImageResource(R.drawable.icon_search_surveying);
                break;

            case POST_SURVEY_WORK:
                holder.dashBoardItemImage.setImageResource(R.drawable.icon_post_survey);
                break;

            case SEARCH_SURVEY_WORK:
                holder.dashBoardItemImage.setImageResource(R.drawable.icon_search_survey);
                break;

            case RENT_YOUR_EQUIPMENT:
                holder.dashBoardItemImage.setImageResource(R.drawable.icon_rent_equipment);
                break;

            case HIRE_EQUIPMENT:
                holder.dashBoardItemImage.setImageResource(R.drawable.icon_hire_equipment);
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
                    adapterBusinessHubListener.OnItemClick(position);
                }
            });
        }
    }

//------------------------------------------------------- Interface -------------------------------------------------------------------------------------------------------------------------------------------------

    public interface AdapterBusinessHubListener{
        void OnItemClick(int position);
    }

}

