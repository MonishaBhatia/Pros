package pros.app.com.pros.profile.presenter;

import java.io.IOException;

import pros.app.com.pros.ProsApplication;
import pros.app.com.pros.R;
import pros.app.com.pros.base.ApiEndPoints;
import pros.app.com.pros.base.HttpServiceUtil;
import pros.app.com.pros.base.HttpServiceView;
import pros.app.com.pros.base.JsonUtils;
import pros.app.com.pros.base.PrefUtils;
import pros.app.com.pros.base.ProsConstants;
import pros.app.com.pros.profile.model.FollowingModel;
import pros.app.com.pros.profile.views.FollowingView;

public class FollowingPresenter implements HttpServiceView {

    private final FollowingView view;

    public FollowingPresenter(FollowingView view) {
        this.view = view;
    }


    @Override
    public void response(String response, int tag) {
        if (tag == ApiEndPoints.fan_following.getTag()) {
            try {
                FollowingModel followingModel = JsonUtils.from(response, FollowingModel.class);
                view.bindData(followingModel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(int tag) {
        view.showToast(ProsApplication.getInstance().getApplicationContext().getString(R.string.internal_error));
    }

    public void getFollowingList() {
        new HttpServiceUtil(
                this,
                String.format(ApiEndPoints.fan_following.getApi(), PrefUtils.getUser().getId()),
                ProsConstants.GET_METHOD,
                null,
                ApiEndPoints.fan_following.getTag()
        ).execute();
    }
}
