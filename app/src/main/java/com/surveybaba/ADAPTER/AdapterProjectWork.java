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

public class AdapterProjectWork extends RecyclerView.Adapter<AdapterProjectWork.ViewHolder> {

    // Context
    Context context;
    // List
    ArrayList<ProjectModule> projectWorkList;
    // Interface
    AdapterProjectWorkListener adapterProjectWorkListener;

//------------------------------------------------------- Constructor -------------------------------------------------------------------------------------------------------------------------------------------------

    public AdapterProjectWork(Context context, ArrayList<ProjectModule> projectWorkList, AdapterProjectWorkListener adapterProjectWorkListener){
        this.context = context;
        this.projectWorkList = projectWorkList;
        this.adapterProjectWorkListener = adapterProjectWorkListener;
    }

//------------------------------------------------------- on CreateViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public AdapterProjectWork.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_project_work_adapter_layout, parent, false);
        return new AdapterProjectWork.ViewHolder(view);
    }

//-------------------------------------------------------  on BindViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull AdapterProjectWork.ViewHolder holder, int position) {
        ProjectModule projectModule = projectWorkList.get(position);
        holder.projectWorkItemName.setText(projectModule.getName());
    }

//------------------------------------------------------- ItemCount -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public int getItemCount() {
        return projectWorkList.size();
    }

//------------------------------------------------------- ViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView projectWorkItemImage;
        TextView projectWorkItemName;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            projectWorkItemName = itemView.findViewById(R.id.projectWorkItemName);
            projectWorkItemImage = itemView.findViewById(R.id.projectWorkItemImage);
            // Item Click Listener
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    adapterProjectWorkListener.OnItemClick(position);
                }
            });
        }
    }

//------------------------------------------------------- Interface -------------------------------------------------------------------------------------------------------------------------------------------------

    public interface AdapterProjectWorkListener{
        void OnItemClick(int position);
    }

}
