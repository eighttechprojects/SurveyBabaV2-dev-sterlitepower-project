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
import com.surveybaba.model.ProjectModule;

import java.util.ArrayList;

public class AdapterProjectStage extends RecyclerView.Adapter<AdapterProjectStage.ViewHolder> {

    // Context
    Context context;
    // List
    ArrayList<ProjectModule> projectStageList;
    // Interface
    AdapterProjectStageListener adapterProjectStageListener;


//------------------------------------------------------- Constructor -------------------------------------------------------------------------------------------------------------------------------------------------

    public AdapterProjectStage(Context context, ArrayList<ProjectModule> projectStageList, AdapterProjectStageListener adapterProjectStageListener){
        this.context = context;
        this.projectStageList = projectStageList;
        this.adapterProjectStageListener = adapterProjectStageListener;
    }

//------------------------------------------------------- on CreateViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public AdapterProjectStage.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_project_stage_adapter_layout, parent, false);
        return new AdapterProjectStage.ViewHolder(view);
    }

//-------------------------------------------------------  on BindViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull AdapterProjectStage.ViewHolder holder, int position) {
        ProjectModule projectModule = projectStageList.get(position);
        holder.projectStageItemName.setText(projectModule.getName());
    }

//------------------------------------------------------- ItemCount -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public int getItemCount() {
        return projectStageList.size();
    }

//------------------------------------------------------- ViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView projectStageItemImage;
        TextView projectStageItemName;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            projectStageItemName = itemView.findViewById(R.id.projectStageItemName);
            projectStageItemImage = itemView.findViewById(R.id.projectStageItemImage);
            // Item Click Listener
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    adapterProjectStageListener.OnItemClick(position);
                }
            });
        }
    }

//------------------------------------------------------- Interface -------------------------------------------------------------------------------------------------------------------------------------------------

    public interface AdapterProjectStageListener{
        void OnItemClick(int position);
    }


}
