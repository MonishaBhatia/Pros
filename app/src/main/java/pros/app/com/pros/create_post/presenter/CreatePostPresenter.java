package pros.app.com.pros.create_post.presenter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

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
import pros.app.com.pros.profile.model.UploadUrlModel;

public class CreatePostPresenter implements HttpServiceView {

    private VideoPathModel videoPathModel;
    private UploadUrlModel uploadUrlModel;
    private byte[] byteArray;

    public void getVideoUploadPath(String url, byte[] byteArray) {

        this.byteArray = byteArray;

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
        if (tag == ApiEndPoints.upload_video.getTag()) {
            try {
                videoPathModel = JsonUtils.from(response, VideoPathModel.class);
                uploadVideoToDb();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (tag == ApiEndPoints.upload_image.getTag()) {
            try {
                uploadUrlModel = JsonUtils.from(response, UploadUrlModel.class);

                if (!TextUtils.isEmpty(uploadUrlModel.getImageUploadUrl())) {
                    uploadUrlToDb();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (tag == ApiEndPoints.upload_url_to_db.getTag()) {
            //TODO:
        } else {
            LogUtils.LOGD("Hurray", "file Uploaded");
        }
    }

    private void uploadUrlToDb() {
        new HttpServiceUtil(
                this,
                uploadUrlModel.getImageUploadUrl(),
                ProsConstants.PUT_METHOD,
                "",
                byteArray,
                ApiEndPoints.upload_url_to_db.getTag()
        ).execute();
    }

    private void uploadVideoToDb() {
        new HttpServiceUtil(
                this,
                videoPathModel.getUploadUrl(),
                ProsConstants.PUT_METHOD,
                "video",
                byteArray,
                ApiEndPoints.upload_url_to_db.getTag()
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

    public void getImageUploadUrl(byte[] byteArray) {
        this.byteArray = byteArray;
        new HttpServiceUtil(
                this,
                ApiEndPoints.upload_image.getApi(),
                ProsConstants.GET_METHOD,
                null,
                ApiEndPoints.upload_image.getTag()
        ).execute();
    }


    @Override
    public void onError(int tag) {

    }
}
