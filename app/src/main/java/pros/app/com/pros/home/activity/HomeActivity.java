package pros.app.com.pros.home.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.allattentionhere.autoplayvideos.AAH_CustomRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.base.PrefUtils;
import pros.app.com.pros.create_post.activity.CreatePost;
import pros.app.com.pros.home.adapter.PostAdapter;
import pros.app.com.pros.home.model.PostModel;
import pros.app.com.pros.home.presenter.HomePresenter;
import pros.app.com.pros.home.view.HomeView;
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
        homePresenter.getPostData();
    }

    @OnClick(R.id.ivProfile)
    public void onClickProfile() {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    @OnClick(R.id.ivSearch)
    public void onClickSearch() {
        startActivity(new Intent(this, SearchActivity.class));
    }

    @Override
    public void bindData(ArrayList<PostModel> postsList) {
        postsPrgressBar.setVisibility(View.GONE);
        if(PrefUtils.isAthlete()){
            createPostContainer.setVisibility(View.VISIBLE);


        }
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
    protected void onStop() {
        super.onStop();
        rvPosts.stopVideos();
    }

    @OnClick(R.id.upload_post_button)
    void onCreatePostButtonClick(){
        togglePostListOptions = !togglePostListOptions;
        if(togglePostListOptions) {
            createPostOptions.setVisibility(View.VISIBLE);
        }else {
            createPostOptions.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.create_post)
    void createPost(){
        startActivity(new Intent(getApplicationContext(), CreatePost.class));
    }
}
