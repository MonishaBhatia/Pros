package pros.app.com.pros.profile.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.base.BaseActivity;
import pros.app.com.pros.base.CustomDialogFragment;
import pros.app.com.pros.base.CustomDialogListener;
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
import static pros.app.com.pros.base.ProsConstants.IMAGE_URL;
import static pros.app.com.pros.base.ProsConstants.NAME;
import static pros.app.com.pros.base.ProsConstants.PROFILE_ID;

public class AthleteProfileActivity extends BaseActivity implements ProfileView, CustomDialogListener,
        PostFragment.OnFragmentInteractionListener,  QuestionFragment.OnFragmentInteractionListener{

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


    @BindView(R.id.bsConfirm)
    View bsConfirm;

    @BindView(R.id.bsButtons)
    View bsButtons;

    @BindView(R.id.tvHeading)
    TextView tvHeading;

    @BindView(R.id.tvAction1)
    TextView tvAction1;

    @BindView(R.id.tvAction2)
    TextView tvAction2;

    @BindView(R.id.btn1)
    Button btn1;

    @BindView(R.id.btn2)
    Button btn2;

    @BindView(R.id.posts_underline)
    View postsUnderline;

    @BindView(R.id.questions_underline)
    View questionsUnderline;

    private static final String POSTS ="posts";
    private static final String QUESTIONS ="questions";

    private BottomSheetBehavior behavior;
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
        tvLikedVideos.setText(String.format(getString(R.string.label_posts), 0));
        tvLikedQuestions.setText(String.format(getString(R.string.label_questions), 0));

        profileId = getIntent().getIntExtra(PROFILE_ID, 0);
        imageUrl = getIntent().getStringExtra(IMAGE_URL);
        name = getIntent().getStringExtra(NAME);

        profilePresenter.getAthleteProfile(profileId);
        PostFragment postFragment = PostFragment.newInstance("postData", profileId);
        this.replaceFragment(postFragment);

    }

    @OnClick(R.id.ivBlock)
    public void onClickBlock() {
        behavior = BottomSheetBehavior.from(bsButtons);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {

                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;

                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    @OnClick(R.id.tvFollowing)
    public void onClickFollowing() {
        behavior = BottomSheetBehavior.from(bsConfirm);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {

                    case BottomSheetBehavior.STATE_EXPANDED:
                        tvHeading.setText(getString(R.string.confirm_unfollow, name));
                        break;

                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    @OnClick(R.id.tvLikedQuestions)
    void showQuestions(){
        updateUI(QUESTIONS);
        QuestionFragment questionFragment = QuestionFragment.newInstance("athlete_questions", profileId);
        this.replaceFragment(questionFragment);
    }

    @OnClick(R.id.tvLikedVideos)
    void showPosts(){
        updateUI(POSTS);
        PostFragment postFragment = PostFragment.newInstance("postData", profileId);
        this.replaceFragment(postFragment);
    }

    @OnClick(R.id.tvAction1)
    public void onClickAction1() {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        profilePresenter.unFollowAthlete(profileId);
    }

    @OnClick(R.id.tvAction2)
    public void onClickAction2() {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @OnClick(R.id.btn1)
    public void onClickbtn1() {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        confirmationDialog();
    }

    @OnClick(R.id.btn2)
    public void onClickbtn2() {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @OnClick(R.id.tvFollow)
    public void onClickFollow() {
        profilePresenter.followAthlete(profileId);
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


    void updateUI(String dataType){
        if(dataType.equalsIgnoreCase(POSTS)){
            postsUnderline.setVisibility(View.VISIBLE);
            questionsUnderline.setVisibility(View.GONE);

        } else if(dataType.equalsIgnoreCase(QUESTIONS)){
            postsUnderline.setVisibility(View.GONE);
            questionsUnderline.setVisibility(View.VISIBLE);
        }
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


    @Override
    public void onSuccessGetProfile(ProfileMainModel profileMainModel) {

        metaData = profileMainModel.getMetadata();

        Picasso.get().load(imageUrl).into(ivAvatar);
        tvName.setText(name);
        tvNumFollowing.setText(String.valueOf(metaData.getFollowCount()));
        tvNumFollowers.setText(String.valueOf(metaData.getFollowersCount()));
        tvLikedVideos.setText(String.format(getString(R.string.label_posts), metaData.getPostsCount()));
        tvLikedQuestions.setText(String.format(getString(R.string.label_questions), metaData.getQuestionsAskedCount()));
    }

    @Override
    public void updateLikedQuestions(ArrayList<PostModel> postsList) {

    }

    @Override
    public void updateLikedPosts(ArrayList<PostModel> postList) {

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

    @Override
    public void onSuccessBlock() {
        openDialog("Success", getString(R.string.blocked_user), "Close");
    }

    private void confirmationDialog() {
        CustomDialogFragment customDialogFragment = new CustomDialogFragment();
        customDialogFragment.registerCallbackListener(this);
        Bundle bundle = new Bundle();
        bundle.putString("Title", getString(R.string.block_title));
        bundle.putString("Content", getString(R.string.confirm_block));
        bundle.putString("Action1", "Block");
        bundle.putString("Action2", "Cancel");
        customDialogFragment.setArguments(bundle);
        customDialogFragment.show(this.getSupportFragmentManager(), CustomDialogFragment.TAG);
    }

    @Override
    public void handleYes() {
        profilePresenter.blockAthlete(profileId);
    }

    @Override
    public void handleNo() {
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
