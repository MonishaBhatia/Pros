package pros.app.com.pros.home.presenter;

import java.io.IOException;

import pros.app.com.pros.base.ApiEndPoints;
import pros.app.com.pros.base.HttpServiceUtil;
import pros.app.com.pros.base.HttpServiceView;
import pros.app.com.pros.base.JsonUtils;
import pros.app.com.pros.base.ProsConstants;
import pros.app.com.pros.home.model.HomeMainModel;
import pros.app.com.pros.home.view.HomeView;

public class HomePresenter implements HttpServiceView {


    private HomeView view;

    public HomePresenter(HomeView view) {
        this.view = view;
    }

    public void getPostData() {
        new HttpServiceUtil(
                this,
                ApiEndPoints.post_content.getApi(),
                ProsConstants.GET_METHOD,
                null,
                ApiEndPoints.post_content.getTag()
        ).execute();
    }


    @Override
    public void response(String response, int tag) {
        if (tag == ApiEndPoints.fans_profile_metadata.getTag()) {
            try {
                HomeMainModel homePostModel = JsonUtils.from(response, HomeMainModel.class);
                view.bindData(homePostModel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(int tag) {

    }
}
