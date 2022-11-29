//allow us to adapt ot the recycler view
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

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewHolder> {
    ArrayList<BrandDomain> brandDomains;

    public BrandAdapter(ArrayList<BrandDomain> brandDomains) {
        this.brandDomains = brandDomains;
    }

    @NonNull
    @Override
    public BrandAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_brand,parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandAdapter.ViewHolder holder, int position) {
        holder.brandName.setText(brandDomains.get(position).getTitle());
        String picUrl="";
        switch (position) {
            case 0:
                picUrl="benz_log";
                break;
            case 1:
                picUrl="bmw_log";
                break;
            case 2:
                picUrl="toyota_log";
                break;
            case 3:
                picUrl="peugeot_log";
                break;
            case 4:
                picUrl="audi_log";
                break;
        }

        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(picUrl, "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext()).load(drawableResourceId).into(holder.brandPicture);
    }

    @Override
    public int getItemCount() {
        return brandDomains.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView brandName;
        ImageView brandPicture;
        ConstraintLayout mainLayout;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            brandName = itemView.findViewById(R.id.image_desc);
            brandPicture = itemView.findViewById(R.id.brand_logo);
            mainLayout = itemView.findViewById(R.id.main_layout);
        }
    }
}
