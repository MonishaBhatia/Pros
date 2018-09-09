package pros.app.com.pros.profile.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
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
import pros.app.com.pros.profile.fragment.PostFragment;
import pros.app.com.pros.profile.fragment.QuestionFragment;
import pros.app.com.pros.profile.model.MetaDataModel;
import pros.app.com.pros.profile.model.ProfileMainModel;
import pros.app.com.pros.profile.presenter.AthleteProfilePresenter;
import pros.app.com.pros.profile.presenter.ProfilePresenter;
import pros.app.com.pros.profile.views.ProfileView;

import static pros.app.com.pros.base.ProsConstants.FOLLOWING_LIST;
import static pros.app.com.pros.base.ProsConstants.PROFILE_ID;

public class AthleteActivity extends BaseActivity implements ProfileView,
        PostFragment.OnFragmentInteractionListener,  QuestionFragment.OnFragmentInteractionListener{

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

    @BindView(R.id.tvLikedVideos)
    TextView tvVideos;

    @BindView(R.id.tvLikedQuestions)
    TextView tvQuestions;

    @BindView(R.id.tv_answers)
    TextView tvAnswers;

    @BindView(R.id.tvReactions)
    TextView tvReactions;

    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;

    @BindView(R.id.posts_underline)
    View postsUnderline;

    @BindView(R.id.questions_underline)
    View questionsUnderline;

    @BindView(R.id.reactions_underline)
    View reactionsUnderline;

    @BindView(R.id.answers_underline)
    View answersUnderline;

    private ProfilePresenter profilePresenter;
    private MetaDataModel metaData;

    private String name;
    private String imageUrl;
    private int profileId;

    private static final String POSTS ="posts";
    private static final String REACTIONS ="reactions";
    private static final String ANSWERS ="answers";
    private static final String QUESTIONS ="questions";



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
        tvReactions.setText(String.format(getString(R.string.label_reactions), 0));
        tvAnswers.setText(String.format(getString(R.string.label_answers), 0));

        profileId = PrefUtils.getUser().getId();
        imageUrl = PrefUtils.getUser().getMediumUrl();
        name = PrefUtils.getUser().getFirstName() + PrefUtils.getUser().getLastName();

        profilePresenter.getAthleteProfile(profileId);
        PostFragment postFragment = PostFragment.newInstance("postData", PrefUtils.getUser().getId());
        this.replaceFragment(postFragment);

    }

    @OnClick(R.id.ivSettings)
    public void onClickSetting() {
        Intent intent = new Intent(AthleteActivity.this, SettingsActivity.class);
        if (metaData != null) {
            intent.putExtra("Follow_Count", metaData.getFollowCount());
            intent.putExtra("Follower_Count", metaData.getFollowersCount());
        }
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
    public void onCLickInvite() {
        InviteAProFragment.newInstance().show(this.getSupportFragmentManager(), InviteAProFragment.TAG);
    }

    @Override
    public void onSuccessGetProfile(ProfileMainModel profileMainModel) {

        metaData = profileMainModel.getMetadata();

        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.get().load(imageUrl).into(ivAvatar);
        }
        tvName.setText(name);
        tvNumFollowing.setText(String.valueOf(metaData.getFollowCount()));
        tvNumFollowers.setText(String.valueOf(metaData.getFollowersCount()));
        tvVideos.setText(String.format(getString(R.string.label_posts), metaData.getLikedPostsCount()));
        tvQuestions.setText(String.format(getString(R.string.label_questions), metaData.getLikedQuestionsCount()));
        tvReactions.setText(String.format(getString(R.string.label_reactions), metaData.getReactionsCount()));
        tvAnswers.setText(String.format(getString(R.string.label_answers), metaData.getQuestionsAnsweredCount()));
    }

    @OnClick(R.id.tvReactions)
    void showReactions(){
        updateUI(REACTIONS);
        PostFragment postFragment = PostFragment.newInstance("reactionsData", PrefUtils.getUser().getId());
        this.replaceFragment(postFragment);
    }


    @OnClick(R.id.tvLikedVideos)
    void showPosts(){
        updateUI(POSTS);
        PostFragment postFragment = PostFragment.newInstance("postData", PrefUtils.getUser().getId());
        this.replaceFragment(postFragment);
    }

    @OnClick(R.id.tvLikedQuestions)
    void showQuestions(){
        updateUI(QUESTIONS);
        QuestionFragment questionFragment = QuestionFragment.newInstance("athlete_questions", PrefUtils.getUser().getId());
        this.replaceFragment(questionFragment);
    }

    @OnClick(R.id.tv_answers)
    void showAnswers(){
        updateUI(ANSWERS);
        PostFragment answersFragment = PostFragment.newInstance("athleteAnswers", PrefUtils.getUser().getId());
        this.replaceFragment(answersFragment);
    }


    // Replace current Fragment with the destination Fragment.
    public void replaceFragment(Fragment destFragment)
    {
        // First get FragmentManager object.
        FragmentManager fragmentManager = this.getSupportFragmentManager();

        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.fragment_container, destFragment);

        // Commit the Fragment replace action.
        fragmentTransaction.commit();
    }

    void updateUI(String dataType){
        if(dataType.equalsIgnoreCase(POSTS)){
            postsUnderline.setVisibility(View.VISIBLE);
            questionsUnderline.setVisibility(View.GONE);
            reactionsUnderline.setVisibility(View.GONE);
            answersUnderline.setVisibility(View.GONE);

        } else if(dataType.equalsIgnoreCase(REACTIONS)){
            postsUnderline.setVisibility(View.GONE);
            questionsUnderline.setVisibility(View.GONE);
            reactionsUnderline.setVisibility(View.VISIBLE);
            answersUnderline.setVisibility(View.GONE);
        } else if(dataType.equalsIgnoreCase(ANSWERS)){
            postsUnderline.setVisibility(View.GONE);
            questionsUnderline.setVisibility(View.GONE);
            reactionsUnderline.setVisibility(View.GONE);
            answersUnderline.setVisibility(View.VISIBLE);

        } else if(dataType.equalsIgnoreCase(QUESTIONS)){
            postsUnderline.setVisibility(View.GONE);
            questionsUnderline.setVisibility(View.VISIBLE);
            reactionsUnderline.setVisibility(View.GONE);
            answersUnderline.setVisibility(View.GONE);
        }
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
