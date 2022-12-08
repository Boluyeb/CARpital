package uk.ac.tees.a0174604.carpital;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.ViewHolder> {
    ArrayList<RecentDomain> recentDomains;

    public RecentAdapter(ArrayList<RecentDomain> recentDomains) {
        this.recentDomains = recentDomains;
    }

    @NonNull
    @Override
    public RecentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_recent,parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentAdapter.ViewHolder holder, int position) {
        holder.carMake.setText(recentDomains.get(position).getCarMake());
        holder.carModel.setText(recentDomains.get(position).getCarModel());
        holder.price.setText(recentDomains.get(position).getCost());
        holder.year.setText(recentDomains.get(position).getYear());
        holder.location.setText(recentDomains.get(position).getLocation());


        Glide.with(holder.itemView.getContext()).load(recentDomains.get(position).getPicture()).placeholder(R.drawable.vehicle_img_item)
                .error(R.drawable.vehicle_img_item).into(holder.carPicture);
    }

    @Override
    public int getItemCount() {
        return recentDomains.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView carMake;
        TextView carModel;
        TextView price;
        TextView year;
        TextView location;
        ImageView carPicture;
        ConstraintLayout recentCard;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            carMake = itemView.findViewById(R.id.car_make);
            carModel = itemView.findViewById(R.id.car_model);
            price = itemView.findViewById(R.id.car_price);
            year = itemView.findViewById(R.id.car_year);
            location = itemView.findViewById(R.id.car_location);
            carPicture = itemView.findViewById(R.id.car_picture);
            recentCard = itemView.findViewById(R.id.recent_card);

        }
    }
}

