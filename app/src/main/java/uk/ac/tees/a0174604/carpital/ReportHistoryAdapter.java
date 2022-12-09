package uk.ac.tees.a0174604.carpital;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReportHistoryAdapter extends RecyclerView.Adapter<ReportHistoryAdapter.viewHolder> {
    Context context;
    List<ReportModel> list;

    public ReportHistoryAdapter(Context context, List<ReportModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_report_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.vin.setText(list.get(position).getVin());
        holder.make.setText(list.get(position).getCarMake());
        holder.model.setText(list.get(position).getCarModel());
        holder.year.setText(list.get(position).getCarYear());
        holder.manu.setText(list.get(position).getCarManu());
        holder.trans.setText(list.get(position).getCarTrans());
        holder.trim.setText(list.get(position).getCarTrim());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{
        TextView vin, make, model, year, manu, trans, trim;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            vin = itemView.findViewById(R.id.vin);
            make = itemView.findViewById(R.id.carMake);
            model = itemView.findViewById(R.id.carModel);
            year = itemView.findViewById(R.id.carYear);
            manu = itemView.findViewById(R.id.carManu);
            trans = itemView.findViewById(R.id.carTrans);
            trim = itemView.findViewById(R.id.carTrim);
        }
    }
}
