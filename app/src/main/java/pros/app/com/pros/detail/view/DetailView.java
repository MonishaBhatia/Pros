package pros.app.com.pros.detail.view;

public interface DetailView {

    void onLikeSuccess();

    void onUnLikeSuccess();

    void onflagPostSuccess();

    void onFailure(int message);
}
