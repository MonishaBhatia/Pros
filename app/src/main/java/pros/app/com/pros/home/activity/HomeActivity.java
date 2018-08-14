package pros.app.com.pros.home.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.home.model.HomeMainModel;
import pros.app.com.pros.home.presenter.HomePresenter;
import pros.app.com.pros.home.view.HomeView;
import pros.app.com.pros.profile.activity.ProfileActivity;
import pros.app.com.pros.search.activity.SearchActivity;

public class HomeActivity extends AppCompatActivity implements HomeView {

    @BindView(R.id.rvPosts)
    RecyclerView rvPosts;
    private HomePresenter homePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
    public void bindData(HomeMainModel homePostModel) {

    }
}
