package pros.app.com.pros.profile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pros.app.com.pros.R;
import pros.app.com.pros.home.model.AthleteModel;
import pros.app.com.pros.profile.activity.FollowingActivity;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder> implements Filterable {
    private final List<AthleteModel> athleteModelList;
    private Context context;
    private FollowingActivity followingActivity;
    private List<AthleteModel> model = new ArrayList<>();
    private List<AthleteModel> modelFiltered = new ArrayList<>();

    @Override
    public FollowingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_following, parent, false);
        return new FollowingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowingViewHolder holder, int position) {
        AthleteModel currentItem = athleteModelList.get(position);

        Picasso.get().load(currentItem.getAvatar().getThumbnailUrl()).into(holder.ivIcon);
        holder.tvName.setText(String.format("%s %s", currentItem.getFirstName(),currentItem.getLastName()));
    }

    public FollowingAdapter(Context context, List<AthleteModel> athleteModelList, FollowingActivity followingActivity) {
        this.context = context;
        this.athleteModelList = athleteModelList;

        this.model.clear();
        this.model.addAll(model);
        this.modelFiltered.clear();
        this.modelFiltered.addAll(model);
        this.followingActivity = followingActivity;
    }

    @Override
    public int getItemCount() {
        return athleteModelList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchString = charSequence.toString();
                if (searchString.isEmpty()) {
                    modelFiltered.clear();
                    modelFiltered.addAll(model);
                } else {
                    modelFiltered.clear();
                    for (int i = 0; i < model.size(); i++) {
                        if (model.get(i).getFirstName().toLowerCase().startsWith(searchString.toLowerCase())) {
                            modelFiltered.add(model.get(i));
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = modelFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                modelFiltered = (List<AthleteModel>) filterResults.values;
                notifyDataSetChanged();
                if (followingActivity != null)
                    followingActivity.setScrollPositionToSearch();
            }
        };
    }

    public class FollowingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.ivIcon)
        ImageView ivIcon;
        @BindView(R.id.tvName)
        TextView tvName;

        public FollowingViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        public void onClick(View v) {
            //TODO:navigate to athlete profile page
        }
    }
}