package pros.app.com.pros.profile.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.base.BaseActivity;
import pros.app.com.pros.base.PrefUtils;
import pros.app.com.pros.home.model.PostModel;
import pros.app.com.pros.profile.model.MetaDataModel;
import pros.app.com.pros.profile.model.ProfileMainModel;
import pros.app.com.pros.profile.presenter.ProfilePresenter;
import pros.app.com.pros.profile.views.ProfileView;

import static pros.app.com.pros.base.ProsConstants.FOLLOWING_LIST;
import static pros.app.com.pros.base.ProsConstants.PROFILE_ID;

public class AthleteActivity extends BaseActivity implements ProfileView {

    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.tvInvite)
    TextView tvInvite;

    @BindView(R.id.tvNumFollowing)
    TextView tvNumFollowing;

    @BindView(R.id.tvNumFollowers)
    TextView tvNumFollowers;

    @BindView(R.id.tvVideos)
    TextView tvVideos;

    @BindView(R.id.tvQuestions)
    TextView tvQuestions;

    @BindView(R.id.tvAnswers)
    TextView tvAnswers;

    @BindView(R.id.tvReactions)
    TextView tvReactions;

    private BottomSheetBehavior behavior;
    private ProfilePresenter profilePresenter;
    private MetaDataModel metaData;

    private String name;
    private String imageUrl;
    private int profileId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athlete);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ButterKnife.bind(this);
        profilePresenter = new ProfilePresenter(this);

        tvNumFollowers.setText("-");
        tvNumFollowing.setText("-");
        tvVideos.setText(String.format(getString(R.string.label_posts), 0));
        tvQuestions.setText(String.format(getString(R.string.label_questions), 0));

        profileId = PrefUtils.getUser().getId();
        imageUrl = PrefUtils.getUser().getMediumUrl();
        name = PrefUtils.getUser().getFirstName() + PrefUtils.getUser().getLastName();

        profilePresenter.getAthleteProfile(profileId);

    }

    @OnClick(R.id.ivSettings)
    public void onClickSettings() {
        Intent intent = new Intent(AthleteActivity.this, SettingsActivity.class);
        if (metaData != null)
            intent.putExtra("Follow_Count", metaData.getFollowCount());
            intent.putExtra("Follower_Count", metaData.getFollowersCount());
        startActivity(intent);
    }


    @OnClick(R.id.ivGoBack)
    public void onClickBack() {
        finish();
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

    @OnClick(R.id.tvInvite)
    public void onCLickInvite(){
        InviteAProFragment.newInstance().show(this.getSupportFragmentManager(), InviteAProFragment.TAG);
    }

    @Override
    public void onSuccessGetProfile(ProfileMainModel profileMainModel) {

        metaData = profileMainModel.getMetadata();

        if(!TextUtils.isEmpty(imageUrl)){
            Picasso.get().load(imageUrl).into(ivAvatar);
        }
        tvName.setText(name);
        tvNumFollowing.setText(String.valueOf(metaData.getFollowCount()));
        tvNumFollowers.setText(String.valueOf(metaData.getFollowersCount()));
        tvVideos.setText(String.format(getString(R.string.label_posts), metaData.getLikedPostsCount()));
        tvQuestions.setText(String.format(getString(R.string.label_questions), metaData.getLikedQuestionsCount()));
    }

    @Override
    public void updateLikedQuestions(ArrayList<PostModel> postsList) {

    }

    @Override
    public void updateLikedPosts(ArrayList<PostModel> postList) {

    }

    @Override
    public void onsucessUnfollow() {

    }

    @Override
    public void onSuccessFollow() {

    }

    @Override
    public void onSuccessBlock() {

    }
}
