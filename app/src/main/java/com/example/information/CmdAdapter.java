package com.example.information;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class CmdAdapter extends RecyclerView.Adapter<CmdAdapter.ViewHolder> {
    private List<Cmd> cmds = new ArrayList<>();
    private OnItemRecyclerClick listener;

    CmdAdapter(OnItemRecyclerClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CmdAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_cmd, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CmdAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.setDataCmd(cmds.get(i));
        viewHolder.fullDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(i, cmds.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView description;
        private ImageButton fullDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.txt_name_cmd);
            description = itemView.findViewById(R.id.txt_description_cmd);
            fullDescription = itemView.findViewById(R.id.btn_full_description_cmd);
        }

        void setDataCmd(Cmd cmd){
            name.setText("Наименование команды" + cmd.getName());
            description.setText("Краткое описание" + cmd.getDescription());
        }
    }
}
