package pros.app.com.pros.detail.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import pros.app.com.pros.R;
import pros.app.com.pros.detail.view.DetailView;
import pros.app.com.pros.home.model.AthleteModel;

public class ReactionAthlete extends RecyclerView.Adapter<ReactionAthlete.ViewHolder> {


    private ArrayList<AthleteModel> athleteModelArrayList;
    private Context context;
    private DetailView detailView;
    private ArrayList<String> reactionsUrlList;


    public ReactionAthlete(Context context, ArrayList<AthleteModel> athleteModels, ArrayList<String> reactionUrlList, DetailView detailView) {
        this.context = context;
        this.athleteModelArrayList = athleteModels;
        this.detailView = detailView;
        this.reactionsUrlList = reactionUrlList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reaction_athlete, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AthleteModel athleteModel = athleteModelArrayList.get(position);
        Picasso.get().load(athleteModel.getAvatar().getThumbnailUrl()).into(holder.prosThumb);
    }

    @Override
    public int getItemCount() {
        return athleteModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.athlete_thumb)
        CircleImageView prosThumb;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            prosThumb.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.athlete_thumb:
                    /*Intent intent = new Intent(context, AthleteProfileActivity.class);
                    intent.putExtra(PROFILE_ID, athleteModelArrayList.get(getAdapterPosition()).getId());
                    intent.putExtra(IMAGE_URL, athleteModelArrayList.get(getAdapterPosition()).getAvatar().getMediumUrl());
                    intent.putExtra(NAME, String.format("%s %s", athleteModelArrayList.get(getAdapterPosition()).getFirstName(), athleteModelArrayList.get(getAdapterPosition()).getLastName()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);*/
                    if(detailView != null ) {
                        detailView.playVideo(reactionsUrlList.get(getAdapterPosition()));
                    }
                    break;
            }
        }
    }
}
