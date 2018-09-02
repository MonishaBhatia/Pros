package pros.app.com.pros.create_question.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.create_question.view.CreateQuestionView;
import pros.app.com.pros.home.model.AthleteModel;

public class AskQuestionActivity extends AppCompatActivity implements CreateQuestionView{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.close_button)
    void closeApp(){
        this.finish();
    }

    @Override
    public void updateAthletesData(List<AthleteModel> athletes) {

    }

    @Override
    public void closeActivity() {
        this.finish();
    }

    @Override
    public void showLoader() {

    }

    @Override
    public void showPostErrorMessage(String message) {

    }
}
