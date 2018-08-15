package pros.app.com.pros.profile.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.base.BaseView;
import pros.app.com.pros.base.PrefUtils;
import pros.app.com.pros.profile.model.MetaDataModel;
import pros.app.com.pros.profile.model.ProfileMainModel;
import pros.app.com.pros.profile.presenter.ProfilePresenter;
import pros.app.com.pros.profile.views.ProfileView;


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

    @BindView(R.id.rvPosts)
    RecyclerView rvPosts;

    private ProfilePresenter profilePresenter;
    private MetaDataModel metaData;

    private int followCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);
        profilePresenter = new ProfilePresenter(this);

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
        labelNothing.setText(getString(R.string.no_liked_posts));
    }

    @OnClick(R.id.tvLikedQuestions)
    public void onCLickLikedQuestions(){
        labelNothing.setText(getString(R.string.no_liked_questions));
    }

    @OnClick({R.id.tvNumFollowing, R.id.labelFollowing})
    public void onCLickFollow() {
        startActivity(new Intent(this, FollowingActivity.class));
    }

    @Override
    public void onSuccessGetProfile(ProfileMainModel profileMainModel) {

        metaData = profileMainModel.getMetadata();

        tvName.setText(String.format("%s %s", PrefUtils.getUser().getFirstName(), PrefUtils.getUser().getLastName()));
        if (profileMainModel.getMetadata().getFollowCount() != 0)
            tvNumFollowing.setText(String.valueOf(metaData.getFollowCount()));
        tvLikedVideos.setText(String.format(getString(R.string.label_liked_posts), metaData.getLikedPostsCount()));
        tvLikedQuestions.setText(String.format(getString(R.string.label_liked_questions), metaData.getLikedQuestionsCount()));
    }
}
