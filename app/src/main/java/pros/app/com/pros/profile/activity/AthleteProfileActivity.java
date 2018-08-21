package pros.app.com.pros.profile.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.base.PrefUtils;
import pros.app.com.pros.home.model.PostModel;
import pros.app.com.pros.profile.model.MetaDataModel;
import pros.app.com.pros.profile.model.ProfileMainModel;
import pros.app.com.pros.profile.presenter.ProfilePresenter;
import pros.app.com.pros.profile.views.ProfileView;

import static pros.app.com.pros.base.ProsConstants.FOLLOWING_LIST;
import static pros.app.com.pros.base.ProsConstants.IMAGE_URL;
import static pros.app.com.pros.base.ProsConstants.NAME;
import static pros.app.com.pros.base.ProsConstants.PROFILE_ID;

public class AthleteProfileActivity extends AppCompatActivity implements ProfileView {

    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.tvFollow)
    TextView tvFollow;

    @BindView(R.id.tvFollowing)
    TextView tvFollowing;

    @BindView(R.id.tvNumFollowing)
    TextView tvNumFollowing;

    @BindView(R.id.tvNumFollowers)
    TextView tvNumFollowers;

    @BindView(R.id.tvLikedVideos)
    TextView tvLikedVideos;

    @BindView(R.id.tvLikedQuestions)
    TextView tvLikedQuestions;

    @BindView(R.id.label_nothing)
    TextView labelNothing;

    @BindView(R.id.rvPosts)
    RecyclerView rvPosts;

    private ProfilePresenter profilePresenter;
    private MetaDataModel metaData;

    private String name;
    private String imageUrl;
    private int profileId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athlete_profile);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ButterKnife.bind(this);
        profilePresenter = new ProfilePresenter(this);

        tvNumFollowers.setText("-");
        tvNumFollowing.setText("-");
        tvLikedVideos.setText(String.format(getString(R.string.label_liked_posts), 0));
        tvLikedQuestions.setText(String.format(getString(R.string.label_liked_questions), 0));

        profileId = getIntent().getIntExtra(PROFILE_ID, 0);
        imageUrl = getIntent().getStringExtra(IMAGE_URL);
        name = getIntent().getStringExtra(NAME);

        profilePresenter.getAthleteProfile(profileId);

    }

    @OnClick(R.id.ivBlock)
    public void onClickBlock() {
        //TODO:bottomsheet
    }

    @OnClick(R.id.tvFollowing)
    public void onClickFollowing() {
        profilePresenter.unFollowAthlete(profileId);
    }

    @OnClick(R.id.tvFollow)
    public void onClickFollow() {
        profilePresenter.followAthlete(profileId);
    }

    @OnClick(R.id.ivGoBack)
    public void onClickBack() {
        finish();
    }

    @OnClick(R.id.tvLikedVideos)
    public void onCLickLikedPosts(){
        labelNothing.setText(getString(R.string.no_liked_posts));
    }

    @OnClick(R.id.tvLikedQuestions)
    public void onCLickLikedQuestions(){
        labelNothing.setText(getString(R.string.no_liked_questions));
    }

    @OnClick({R.id.tvNumFollowing, R.id.labelFollowing})
    public void onCLickFollowing() {
        Intent intent = new Intent(this, FollowingActivity.class);
        intent.putExtra(PROFILE_ID, profileId);
        intent.putExtra(FOLLOWING_LIST, true);
        startActivity(intent);
    }

    @OnClick({R.id.tvNumFollowers, R.id.labelFollowers})
    public void onCLickFollowers() {
        Intent intent = new Intent(this, FollowingActivity.class);
        intent.putExtra(PROFILE_ID, profileId);
        startActivity(intent);
    }

    @Override
    public void onSuccessGetProfile(ProfileMainModel profileMainModel) {

        metaData = profileMainModel.getMetadata();

        Picasso.get().load(imageUrl).into(ivAvatar);
        tvName.setText(name);
        tvNumFollowing.setText(String.valueOf(metaData.getFollowCount()));
        tvNumFollowers.setText(String.valueOf(metaData.getFollowersCount()));
        tvLikedVideos.setText(String.format(getString(R.string.label_posts), metaData.getLikedPostsCount()));
        tvLikedQuestions.setText(String.format(getString(R.string.label_questions), metaData.getLikedQuestionsCount()));
    }

    @Override
    public void updateLikedQuestions(ArrayList<PostModel> postsList) {

    }

    @Override
    public void onsucessUnfollow() {
        tvFollowing.setVisibility(View.GONE);
        tvFollow.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccessFollow() {
        tvFollowing.setVisibility(View.VISIBLE);
        tvFollow.setVisibility(View.GONE);
    }
}
