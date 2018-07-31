package pros.app.com.pros.profile.presenter;

import java.io.IOException;

import pros.app.com.pros.base.ApiEndPoints;
import pros.app.com.pros.base.HttpServiceUtil;
import pros.app.com.pros.base.HttpServiceView;
import pros.app.com.pros.base.JsonUtils;
import pros.app.com.pros.base.PrefUtils;
import pros.app.com.pros.base.ProsConstants;
import pros.app.com.pros.profile.model.ProfileMainModel;
import pros.app.com.pros.profile.views.ProfileView;

public class ProfilePresenter implements HttpServiceView {


    private ProfileView view;
    private ProfileMainModel profileMainModel;

    public ProfilePresenter(ProfileView view) {
        this.view = view;
    }

    @Override
    public void response(String response, int tag) {
        if (tag == ApiEndPoints.fans_profile_metadata.getTag()) {
            try {
                profileMainModel = JsonUtils.from(response, ProfileMainModel.class);
                view.onSuccessGetProfile(profileMainModel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onError(int tag) {

    }

    public void getProfileData() {
        new HttpServiceUtil(
                this,
                String.format(ApiEndPoints.fans_profile_metadata.getApi(), PrefUtils.getUser().getId()),
                ProsConstants.GET_METHOD,
                null,
                ApiEndPoints.fans_profile_metadata.getTag()
        ).execute();
    }
}
