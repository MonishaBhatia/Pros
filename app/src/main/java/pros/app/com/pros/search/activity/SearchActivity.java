package pros.app.com.pros.search.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pros.app.com.pros.R;
import pros.app.com.pros.base.LogUtils;
import pros.app.com.pros.home.model.AthleteModel;
import pros.app.com.pros.search.adapter.TopProsAdapter;
import pros.app.com.pros.search.presenter.SearchPresenter;
import pros.app.com.pros.search.views.SearchView;


public class SearchActivity extends AppCompatActivity implements SearchView{

    @BindView(R.id.topPros)
    RecyclerView topProsRecyclerview;
    private SearchPresenter searchPresenter;
    private TopProsAdapter topProsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtils.LOGD("Search Activity:", "Called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        searchPresenter = new SearchPresenter(this);
        searchPresenter.getTopProsData();
    }


    @Override
    public void updateTopPros(ArrayList<AthleteModel> topProsList) {
        topProsAdapter = new TopProsAdapter(topProsList);

        topProsRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        topProsRecyclerview.setAdapter(topProsAdapter);

    }

    @Override
    public void updateTopPosts() {

    }
}
