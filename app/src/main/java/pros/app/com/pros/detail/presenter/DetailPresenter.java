package pros.app.com.pros.detail.presenter;

import pros.app.com.pros.R;
import pros.app.com.pros.base.ApiEndPoints;
import pros.app.com.pros.base.HttpServiceUtil;
import pros.app.com.pros.base.HttpServiceView;
import pros.app.com.pros.base.ProsConstants;
import pros.app.com.pros.detail.view.DetailView;

public class DetailPresenter implements HttpServiceView {

    private DetailView detailView;

    public DetailPresenter(DetailView detailView) {
        this.detailView = detailView;
    }

    public void likePost(int id) {
        new HttpServiceUtil(
                this,
                String.format(ApiEndPoints.like_post.getApi(), id),
                ProsConstants.POST_METHOD,
                null,
                ApiEndPoints.like_post.getTag()
        ).execute();

    }

    public void unlikePost(int id) {
        new HttpServiceUtil(
                this,
                String.format(ApiEndPoints.unlike_post.getApi(), id),
                ProsConstants.POST_METHOD,
                null,
                ApiEndPoints.unlike_post.getTag()
        ).execute();
    }

    public void likeQuestion(int id) {
        new HttpServiceUtil(
                this,
                String.format(ApiEndPoints.like_question.getApi(), id),
                ProsConstants.POST_METHOD,
                null,
                ApiEndPoints.like_question.getTag()
        ).execute();
    }

    public void unlikeQuestion(int id) {
        new HttpServiceUtil(
                this,
                String.format(ApiEndPoints.unlike_question.getApi(), id),
                ProsConstants.POST_METHOD,
                null,
                ApiEndPoints.unlike_question.getTag()
        ).execute();
    }

    public void flagPost(int id) {
        new HttpServiceUtil(
                this,
                String.format(ApiEndPoints.flag_post.getApi(), id),
                ProsConstants.POST_METHOD,
                null,
                ApiEndPoints.flag_post.getTag()
        ).execute();
    }


    @Override
    public void response(String response, int tag) {
        if (tag == ApiEndPoints.like_question.getTag() || tag == ApiEndPoints.like_post.getTag()) {
            detailView.onLikeSuccess();
        } else if (tag == ApiEndPoints.unlike_post.getTag() || tag == ApiEndPoints.unlike_question.getTag()) {
            detailView.onUnLikeSuccess();
        } else if (tag == ApiEndPoints.flag_post.getTag()) {
            detailView.onflagPostSuccess();
        }
    }

    @Override
    public void onError(int tag) {
        detailView.onFailure(R.string.internal_error);
    }
}
