package pros.app.com.pros.profile.views;

import pros.app.com.pros.base.BaseView;
import pros.app.com.pros.profile.model.FollowingModel;

public interface FollowingView {

    void bindData(FollowingModel followingModel);
    void showToast(String message);
}
