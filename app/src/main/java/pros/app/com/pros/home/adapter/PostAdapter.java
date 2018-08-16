package pros.app.com.pros.home.adapter;

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

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private ArrayList<PostModel> postsArrayList;

    public  PostAdapter(ArrayList<PostModel> postsArrayList){
        this.postsArrayList = postsArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_posts, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Main logic of updating UI

        PostModel postModel =  postsArrayList.get(position);
        String contentType = postModel.getContentType();
        String dateDifference = DateUtils.getDateDifference(postModel.getCreatedAt());

        if(postModel.getQuestioner() != null ||
                (contentType != null &&
                (contentType.equalsIgnoreCase("image") || contentType.equalsIgnoreCase("video"))));
        {
            String athleteFullName = "";
            String thumbnailUrl = "";
            String athleteThumbnailUrl = "";

            holder.postLike.setText("" + postModel.getLikes().getCount());

            if(postModel.getQuestioner() != null){
                athleteFullName = postModel.getQuestioner().getName();
                thumbnailUrl = postModel.getQuestioner().getAvatar().getThumbnailUrl();
                athleteThumbnailUrl = postModel.getQuestioner().getAvatar().getThumbnailUrl();
                holder.questionContainer.setVisibility(View.VISIBLE);
                holder.questionContainer.setVisibility(View.VISIBLE);
                holder.questionText.setText(postModel.getText());
                holder.athleteName.setTextColor(Color.parseColor("#50e3c2"));
            } else {
                thumbnailUrl= postModel.getUrls().getThumbnailUrl();
                athleteThumbnailUrl =  postModel.getAthlete().getAvatar().getThumbnailUrl();
                athleteFullName = postModel.getAthlete().getFirstName() + " " + postModel.getAthlete().getLastName();
                holder.questionContainer.setVisibility(View.GONE);
                holder.questionContainer.setVisibility(View.GONE);
                holder.athleteName.setTextColor(Color.parseColor("#ffffff"));
            }


            Picasso.get().load(thumbnailUrl).into(holder.postImage);
            Picasso.get().load(athleteThumbnailUrl).into(holder.athleteThumb);
            holder.athleteName.setText(athleteFullName);
            holder.createdAt.setText(dateDifference);


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

        @BindView(R.id.created_at)
        TextView createdAt;

        @BindView(R.id.post_like)
        TextView postLike;

        @BindView(R.id.athlete_name)
        TextView athleteName;

        @BindView(R.id.question_container_background)
        View questionContainerBackground;

        @BindView(R.id.question_container)
        RelativeLayout questionContainer;

        @BindView(R.id.question_title)
        TextView questionTitle;

        @BindView(R.id.question_text)
        TextView questionText;

        @BindView(R.id.athlete_thumb)
        CircleImageView athleteThumb;


        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
