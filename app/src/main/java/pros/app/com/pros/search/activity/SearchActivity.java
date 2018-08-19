package pros.app.com.pros.search.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.base.KeyboardAction;
import pros.app.com.pros.home.model.AthleteModel;
import pros.app.com.pros.home.model.PostModel;
import pros.app.com.pros.profile.activity.ProfileActivity;
import pros.app.com.pros.search.adapter.AllAthleteAdapter;
import pros.app.com.pros.search.adapter.TopPostsAdapter;
import pros.app.com.pros.search.adapter.TopProsAdapter;
import pros.app.com.pros.search.presenter.SearchPresenter;
import pros.app.com.pros.search.views.SearchView;


public class SearchActivity extends AppCompatActivity implements SearchView{

    @BindView(R.id.topPros)
    RecyclerView topProsRecyclerview;

    @BindView(R.id.topPosts)
    RecyclerView topPostsRecyclerview;

    @BindView(R.id.all_athletes_list)
    RecyclerView allAthletesListRecyclerview;

    @BindView(R.id.viewSearch)
    View viewSearch;

    @BindView(R.id.edtSearch)
    EditText edtSearch;

    @BindView(R.id.tvCancel)
    TextView tvCancel;


    @BindView(R.id.ivClose)
    ImageView ivClose;

    private SearchPresenter searchPresenter;
    private TopProsAdapter topProsAdapter;
    private TopPostsAdapter topPostsAdapter;
    private AllAthleteAdapter allAthleteAdapter;

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            ivClose.setVisibility(View.VISIBLE);
        }

        @Override
        public void afterTextChanged(Editable editable) {
            allAthleteAdapter.getFilter().filter(editable.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        searchPresenter = new SearchPresenter(this);
        searchPresenter.getSearchData();
        topPostsRecyclerview.setNestedScrollingEnabled(false);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        edtSearch.addTextChangedListener(watcher);
        edtSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                allAthletesListRecyclerview.setVisibility(View.VISIBLE);
                tvCancel.setVisibility(View.VISIBLE);
                InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(edtSearch, 0);
                edtSearch.requestFocus();
                return false;
            }
        });
    }

    @OnClick(R.id.ivProfile)
    public void onClickProfile() {
        startActivity(new Intent(this, ProfileActivity.class));
    }


    @Override
    public void updateTopPros(ArrayList<AthleteModel> topProsList) {
        topProsAdapter = new TopProsAdapter(topProsList);

        topProsRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        topProsRecyclerview.setAdapter(topProsAdapter);

    }

    @Override
    public void updateTopPosts(ArrayList<PostModel> topPostsList) {
        topPostsAdapter = new TopPostsAdapter(topPostsList);

        topPostsRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        topPostsRecyclerview.setAdapter(topPostsAdapter);
    }

    @Override
    public void updateAllAthletes(List<AthleteModel> allAthleteList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        allAthletesListRecyclerview.setLayoutManager(layoutManager);
        allAthleteAdapter = new AllAthleteAdapter(this, allAthleteList);
        allAthletesListRecyclerview.setAdapter(allAthleteAdapter);
    }

   /* @OnClick(R.id.ivBack)
    public void onClickBack() {
        finish();
    }*/

    @OnClick(R.id.ivClose)
    public void onClickClose() {
        ivClose.setVisibility(View.GONE);
        edtSearch.setText("");
        edtSearch.setHint(getString(R.string.label_search));
        KeyboardAction.hideSoftKeyboard(this, edtSearch);
    }

    @OnClick(R.id.tvCancel)
    public void onClickCancel() {
        allAthletesListRecyclerview.setVisibility(View.GONE);
        tvCancel.setVisibility(View.GONE);
        ivClose.setVisibility(View.GONE);
        KeyboardAction.hideSoftKeyboard(this, edtSearch);
    }
}
