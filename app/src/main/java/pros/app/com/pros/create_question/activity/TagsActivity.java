package pros.app.com.pros.create_question.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
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
import pros.app.com.pros.create_question.adapter.TagsAdapter;
import pros.app.com.pros.create_question.view.TagsView;
import pros.app.com.pros.home.model.AthleteModel;

public class TagsActivity extends AppCompatActivity implements TagsView {

    private List<AthleteModel> athleteModelList;

    @BindView(R.id.rv_athletesList)
    RecyclerView athletesRecyclerview;

    @BindView(R.id.viewSearch)
    View viewSearch;

    @BindView(R.id.edtSearch)
    EditText edtSearch;

    @BindView(R.id.tvCancel)
    TextView tvCancel;


    @BindView(R.id.ivClose)
    ImageView ivClose;


    private TagsAdapter tagsAdapter;
    private ArrayList<AthleteModel> userSelectedList = new ArrayList<>();

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
            tagsAdapter.getFilter().filter(editable.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);
        ButterKnife.bind(this);
        Intent i = getIntent();
        ArrayList<AthleteModel> athleteArrayList = i.getParcelableArrayListExtra("athletesList");
        if (i.hasExtra("userSelectedList")) {
            userSelectedList = i.getParcelableArrayListExtra("userSelectedList");
        }


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        athletesRecyclerview.setLayoutManager(layoutManager);
        tagsAdapter = new TagsAdapter(this, athleteArrayList, userSelectedList, this);
        athletesRecyclerview.setAdapter(tagsAdapter);

        edtSearch.addTextChangedListener(watcher);
        edtSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                tvCancel.setVisibility(View.VISIBLE);
                InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(edtSearch, 0);
                edtSearch.requestFocus();
                return false;
            }
        });
    }


    @OnClick(R.id.ivBack)
    public void onClickBack() {
        sendDataBack();

    }

    private void sendDataBack() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("userSelectedList", userSelectedList);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            sendDataBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.ivClose)
    public void onClickClose() {
        ivClose.setVisibility(View.GONE);
        edtSearch.setText("");
        edtSearch.setHint(getString(R.string.label_search));
        KeyboardAction.hideSoftKeyboard(this, edtSearch);
    }

    @OnClick(R.id.tvCancel)
    public void onClickCancel() {
        tvCancel.setVisibility(View.GONE);
        ivClose.setVisibility(View.GONE);
        KeyboardAction.hideSoftKeyboard(this, edtSearch);
    }

    @Override
    public void updateUserSelectedList(ArrayList<AthleteModel> userSelectedList) {
        this.userSelectedList = userSelectedList;
    }
}
