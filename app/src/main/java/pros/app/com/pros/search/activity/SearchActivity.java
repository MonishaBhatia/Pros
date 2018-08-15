package pros.app.com.pros.search.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pros.app.com.pros.R;
import pros.app.com.pros.base.LogUtils;


public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.topPros)
    RecyclerView topProsRecyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtils.LOGD("Search Activity:", "Called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
    }
}
