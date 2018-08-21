package pros.app.com.pros.profile.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.base.PrefUtils;
import pros.app.com.pros.home.model.PostModel;
import pros.app.com.pros.profile.adapter.LikedQuestionsAdapter;
import pros.app.com.pros.profile.model.MetaDataModel;
import pros.app.com.pros.profile.model.ProfileMainModel;
import pros.app.com.pros.profile.presenter.ProfilePresenter;
import pros.app.com.pros.profile.views.ProfileView;

import static pros.app.com.pros.base.ProsConstants.FOLLOWING_LIST;
import static pros.app.com.pros.base.ProsConstants.IS_FAN;
import static pros.app.com.pros.base.ProsConstants.PROFILE_ID;


public class ProfileActivity extends AppCompatActivity implements ProfileView {

    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.tvNumFollowing)
    TextView tvNumFollowing;

    @BindView(R.id.tvLikedVideos)
    TextView tvLikedVideos;

    @BindView(R.id.tvLikedQuestions)
    TextView tvLikedQuestions;

    @BindView(R.id.label_nothing)
    TextView labelNothing;

    @BindView(R.id.liked_posts)
    RecyclerView likedPostsRecyclerView;

    @BindView(R.id.liked_questions)
    RecyclerView likedQuestionsRecyclerview;

    private ProfilePresenter profilePresenter;
    private LikedQuestionsAdapter likedQuestionsAdapter;
    private MetaDataModel metaData;

    private int followCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ButterKnife.bind(this);
        profilePresenter = new ProfilePresenter(this);
        likedQuestionsRecyclerview.setNestedScrollingEnabled(false);

        tvName.setText(String.format("%s %s", PrefUtils.getUser().getFirstName(), PrefUtils.getUser().getLastName()));
        tvNumFollowing.setText("-");
        tvLikedVideos.setText(String.format(getString(R.string.label_liked_posts), 0));
        tvLikedQuestions.setText(String.format(getString(R.string.label_liked_questions), 0));

        profilePresenter.getProfileData();
    }

    @OnClick(R.id.ivSettings)
    public void onClickSettings() {
        Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
        if(metaData != null)
            intent.putExtra("Follow_Count", metaData.getFollowCount());
        startActivity(intent);
    }

    @OnClick(R.id.ivGoBack)
    public void onClickBack() {
        finish();
    }

    @OnClick(R.id.tvLikedVideos)
    public void onCLickLikedPosts(){
        labelNothing.setVisibility(View.VISIBLE);
        labelNothing.setText(getString(R.string.no_liked_posts));
        likedQuestionsRecyclerview.setVisibility(View.GONE);
    }

    @OnClick(R.id.tvLikedQuestions)
    public void onCLickLikedQuestions(){
        labelNothing.setVisibility(View.VISIBLE);
        labelNothing.setText(getString(R.string.no_liked_questions));
        profilePresenter.getLikedQuestionsData();
        likedPostsRecyclerView.setVisibility(View.GONE);
    }

    @OnClick({R.id.tvNumFollowing, R.id.labelFollowing})
    public void onCLickFollow() {
        Intent intent = new Intent(this, FollowingActivity.class);
        intent.putExtra(PROFILE_ID, PrefUtils.getUser().getId());
        intent.putExtra(FOLLOWING_LIST, true);
        intent.putExtra(IS_FAN, true);
        startActivity(intent);
    }

    @Override
    public void onSuccessGetProfile(ProfileMainModel profileMainModel) {

        metaData = profileMainModel.getMetadata();

        //Picasso.get().load()
        tvName.setText(String.format("%s %s", PrefUtils.getUser().getFirstName(), PrefUtils.getUser().getLastName()));
        if (profileMainModel.getMetadata().getFollowCount() != 0)
            tvNumFollowing.setText(String.valueOf(metaData.getFollowCount()));
        tvLikedVideos.setText(String.format(getString(R.string.label_liked_posts), metaData.getLikedPostsCount()));
        tvLikedQuestions.setText(String.format(getString(R.string.label_liked_questions), metaData.getLikedQuestionsCount()));
    }

    @Override
    public void updateLikedQuestions(ArrayList<PostModel> postsList) {
        labelNothing.setVisibility(View.GONE);
        likedQuestionsRecyclerview.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        likedQuestionsRecyclerview.setLayoutManager(layoutManager);
        likedQuestionsAdapter = new LikedQuestionsAdapter(postsList);
        likedQuestionsRecyclerview.setAdapter(likedQuestionsAdapter);

    }

    @Override
    public void onsucessUnfollow() {

    }

    @Override
    public void onSuccessFollow() {

    }
}
