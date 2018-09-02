package pros.app.com.pros.create_post.presenter;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import pros.app.com.pros.base.ApiEndPoints;
import pros.app.com.pros.base.HttpServiceUtil;
import pros.app.com.pros.base.HttpServiceView;
import pros.app.com.pros.base.JsonUtils;
import pros.app.com.pros.base.LogUtils;
import pros.app.com.pros.base.ProsConstants;
import pros.app.com.pros.create_post.model.VideoPathModel;

public class CreatePostPresenter implements HttpServiceView {

    private String filePath;
    private VideoPathModel videoPathModel;

    public void getuploadPath(String url, String filePath) {

        this.filePath = filePath;

        new HttpServiceUtil(
                this,
                url,
                ProsConstants.GET_METHOD,
                null,
                ApiEndPoints.upload_video.getTag()
        ).execute();
    }


    @Override
    public void response(String response, int tag) {
        if (tag == ApiEndPoints.upload_video.getTag()){
            try {
                videoPathModel = JsonUtils.from(response, VideoPathModel.class);
                uploadVideo();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else{
            LogUtils.LOGD("Hurray", "file Uploaded");
        }
    }

    private void uploadVideo(){

        new HttpServiceUtil(
                this,
                videoPathModel.getUploadUrl(),
                ProsConstants.PUT_METHOD,
                filePath,
                123
        ).execute();
    }

    private void postVideo() {
        new HttpServiceUtil(
                this,
                ApiEndPoints.post_content.getApi(),
                ProsConstants.POST_METHOD,
                null,
                ApiEndPoints.post_content.getTag()
        ).execute();
    }

    @Override
    public void onError(int tag) {

    }
}
