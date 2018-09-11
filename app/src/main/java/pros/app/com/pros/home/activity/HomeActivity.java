package pros.app.com.pros.home.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.allattentionhere.autoplayvideos.AAH_CustomRecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import pros.app.com.pros.R;
import pros.app.com.pros.base.PrefUtils;
import pros.app.com.pros.create_post.activity.CreatePost;
import pros.app.com.pros.create_question.activity.AskQuestionActivity;
import pros.app.com.pros.home.adapter.PostAdapter;
import pros.app.com.pros.home.model.PostModel;
import pros.app.com.pros.home.presenter.HomePresenter;
import pros.app.com.pros.home.view.HomeView;
import pros.app.com.pros.profile.activity.AthleteActivity;
import pros.app.com.pros.profile.activity.ProfileActivity;
import pros.app.com.pros.search.activity.SearchActivity;

public class HomeActivity extends AppCompatActivity implements HomeView {

    @BindView(R.id.rvPosts)
    AAH_CustomRecyclerView rvPosts;

    @BindView(R.id.posts_progress_bar)
    RelativeLayout postsPrgressBar;

    @BindView(R.id.upload_post_button)
    ImageView uploadPostButton;

    @BindView(R.id.create_post_container)
    LinearLayout createPostContainer;

    @BindView(R.id.create_post_options)
    LinearLayout createPostOptions;

    @BindView(R.id.ivProfile)
    CircleImageView ivProfile;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private HomePresenter homePresenter;
    private PostAdapter postAdapter;
    private boolean togglePostListOptions;
    private static final int CAPTURE_MEDIA = 368;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ButterKnife.bind(this);
        homePresenter = new HomePresenter(this);
        if (!TextUtils.isEmpty(PrefUtils.getUser().getThumbUrl())) {
            Picasso.get().load(PrefUtils.getUser().getThumbUrl()).placeholder(R.drawable.profile).into(ivProfile);
        }
        homePresenter.getPostData(false);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                homePresenter.getPostData(true);
            }
        });
        // Configure the refreshing colors
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    @OnClick(R.id.ivProfile)
    public void onClickProfile() {
        if (PrefUtils.isAthlete()) {
            startActivity(new Intent(this, AthleteActivity.class));
        } else {
            startActivity(new Intent(this, ProfileActivity.class));
        }
    }

    @OnClick(R.id.ivSearch)
    public void onClickSearch() {
        startActivity(new Intent(this, SearchActivity.class));
    }

    @Override
    public void bindData(ArrayList<PostModel> postsList) {
        postsPrgressBar.setVisibility(View.GONE);
        if (PrefUtils.isAthlete()) {
            createPostContainer.setVisibility(View.VISIBLE);

        }
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        rvPosts.setVisibility(View.VISIBLE);
        rvPosts.setActivity(this);
        rvPosts.setDownloadPath(Environment.getExternalStorageDirectory() + "/MyVideo"); //optional
        rvPosts.setDownloadVideos(true);

        List<String> urls = new ArrayList<>();
        for (PostModel object : postsList) {
            if (null != object.getUrls() && object.getUrls().getIntroUrl() != null && object.getUrls().getIntroUrl().endsWith(".mp4"))
                urls.add(object.getUrls().getIntroUrl());
        }
        rvPosts.preDownload(urls);

        postAdapter = new PostAdapter(postsList, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvPosts.setLayoutManager(mLayoutManager);
        rvPosts.setItemAnimator(new DefaultItemAnimator());
        rvPosts.setVisiblePercent(50);
        rvPosts.setAdapter(postAdapter);

    }

    @Override
    public void updateHomeScreen(ArrayList<PostModel> postsList) {
        postAdapter.clear();
        List<String> urls = new ArrayList<>();
     /*   for (PostModel object : postsList) {
            if (null != object.getUrls() && object.getUrls().getIntroUrl() != null && object.getUrls().getIntroUrl().endsWith(".mp4"))
                urls.add(object.getUrls().getIntroUrl());
        }
        rvPosts.preDownload(urls);*/
        // ...the data has come back, add new items to your adapter...
        postAdapter.addAll(postsList);
        // Now we call setRefreshing(false) to signal refresh has finished
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        rvPosts.stopVideos();
    }

    @OnClick(R.id.upload_post_button)
    void onCreatePostButtonClick() {
        togglePostListOptions = !togglePostListOptions;
        if (togglePostListOptions) {
            createPostOptions.setVisibility(View.VISIBLE);
        } else {
            createPostOptions.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.create_post)
    void createPost() {
        togglePostListOptions = false;
        createPostOptions.setVisibility(View.GONE);
        startActivity(new Intent(getApplicationContext(), CreatePost.class));
    }

    @OnClick(R.id.ask_question)
    void createQuestion() {
        togglePostListOptions = false;
        createPostOptions.setVisibility(View.GONE);
        startActivity(new Intent(getApplicationContext(), AskQuestionActivity.class));
    }
}
