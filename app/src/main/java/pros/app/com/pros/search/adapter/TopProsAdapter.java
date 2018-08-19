package pros.app.com.pros.search.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import pros.app.com.pros.R;
import pros.app.com.pros.home.adapter.PostAdapter;
import pros.app.com.pros.home.model.AthleteModel;

public class TopProsAdapter extends RecyclerView.Adapter<TopProsAdapter.ViewHolder> {

    private ArrayList<AthleteModel> athleteModelArrayList;


    public TopProsAdapter(ArrayList<AthleteModel> athleteModels){
        this.athleteModelArrayList = athleteModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_top_pros, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AthleteModel athleteModel = athleteModelArrayList.get(position);

        holder.prosFirstName.setText(athleteModel.getFirstName());
        holder.prosLastName.setText(athleteModel.getLastName());
        Picasso.get().load(athleteModel.getAvatar().getThumbnailUrl()).into(holder.prosThumb);
    }

    @Override
    public int getItemCount() {
        return athleteModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.pros_thumb)
        CircleImageView prosThumb;

        @BindView(R.id.pros_first_name)
        TextView prosFirstName;

        @BindView(R.id.pros_last_name)
        TextView prosLastName;


        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
