package pros.app.com.pros.home.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.home.adapter.PostAdapter;
import pros.app.com.pros.home.model.HomeMainModel;
import pros.app.com.pros.home.model.PostModel;
import pros.app.com.pros.home.presenter.HomePresenter;
import pros.app.com.pros.home.view.HomeView;
import pros.app.com.pros.profile.activity.ProfileActivity;
import pros.app.com.pros.search.activity.SearchActivity;

public class HomeActivity extends AppCompatActivity implements HomeView {

    @BindView(R.id.rvPosts)
    RecyclerView rvPosts;

    @BindView(R.id.posts_progress_bar)
    RelativeLayout postsPrgressBar;

    private HomePresenter homePresenter;
    private PostAdapter postAdapter;

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
        rvPosts.setVisibility(View.VISIBLE);
        postAdapter = new PostAdapter(postsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvPosts.setLayoutManager(mLayoutManager);
        rvPosts.setItemAnimator(new DefaultItemAnimator());
        rvPosts.setAdapter(postAdapter);

    }
}
