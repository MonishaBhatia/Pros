package pros.app.com.pros.search.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import pros.app.com.pros.R;
import pros.app.com.pros.base.DateUtils;
import pros.app.com.pros.home.model.PostModel;

public class TopPostsAdapter extends RecyclerView.Adapter<TopPostsAdapter.ViewHolder> {

    private ArrayList<PostModel> postsArrayList;

    public  TopPostsAdapter(ArrayList<PostModel> postsArrayList){
        this.postsArrayList = postsArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_top_posts, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Main logic of updating UI

        PostModel postModel =  postsArrayList.get(position);
        String contentType = postModel.getContentType();

        if(contentType != null &&
                        (contentType.equalsIgnoreCase("image") || contentType.equalsIgnoreCase("video")));
        {

            String thumbnailUrl= postModel.getUrls().getThumbnailUrl();
            String athleteThumbnailUrl =  postModel.getAthlete().getAvatar().getThumbnailUrl();
            String athleteFullName = postModel.getAthlete().getFirstName() + " " + postModel.getAthlete().getLastName();
            int reactionsCount = postModel.getReactions().size();

            holder.postLikeCount.setText("" + postModel.getLikes().getCount());
            holder.postReactionCount.setText("" +reactionsCount);
            Picasso.get().load(thumbnailUrl).into(holder.postImage);
            Picasso.get().load(athleteThumbnailUrl).into(holder.athleteThumb);
            holder.athleteName.setText(athleteFullName);


        }

    }

    @Override
    public int getItemCount() {
        return postsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        //Bind the view
        @BindView(R.id.post_image)
        ImageView postImage;

        @BindView(R.id.post_reaction_count)
        TextView postReactionCount;

        @BindView(R.id.post_likes_count)
        TextView postLikeCount;

        @BindView(R.id.athlete_name)
        TextView athleteName;

        @BindView(R.id.athlete_thumb)
        CircleImageView athleteThumb;


        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
