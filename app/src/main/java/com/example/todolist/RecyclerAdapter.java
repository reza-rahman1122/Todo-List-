package com.example.todolist;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CustomAdapter> {
    Context context;
    ArrayList<Task> arrayList;

    public RecyclerAdapter(Context context,ArrayList<Task> arrayList) {
        this.context=context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerAdapter.CustomAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_content,parent,false);
         CustomAdapter customAdapter=new CustomAdapter(view);
         return customAdapter;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.CustomAdapter holder, final int position) {
         Task task=arrayList.get(position);
         holder.time_stamp.setText(task.getTimestamp());
         holder.des.setText(task.getDescription());

    }

    @Override
    public int getItemCount() {
        if (arrayList.isEmpty())
        {
            return 0;
        }
        else
        return arrayList.size();
    }

    public class CustomAdapter extends RecyclerView.ViewHolder  {
        TextView des;
        TextView time_stamp;
        public CustomAdapter(@NonNull View itemView) {
            super(itemView);
            des=itemView.findViewById(R.id.content_description);
            time_stamp=itemView.findViewById(R.id.content_time_stamp);
        }
    }
}
