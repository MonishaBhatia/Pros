package pros.app.com.pros.create_post.presenter;

import android.os.Environment;
import android.text.TextUtils;

import com.vincent.videocompressor.VideoCompress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import pros.app.com.pros.base.ApiEndPoints;
import pros.app.com.pros.base.HttpServiceUtil;
import pros.app.com.pros.base.HttpServiceView;
import pros.app.com.pros.base.JsonUtils;
import pros.app.com.pros.base.LogUtils;
import pros.app.com.pros.base.PrefUtils;
import pros.app.com.pros.base.ProsConstants;
import pros.app.com.pros.create_post.model.VideoPathModel;
import pros.app.com.pros.create_post.model.VideoUploadModel;
import pros.app.com.pros.home.model.AthleteModel;
import pros.app.com.pros.profile.model.UploadUrlModel;

public class CreatePostPresenter implements HttpServiceView {

    private VideoPathModel videoPathModel;
    private UploadUrlModel uploadUrlModel;
    private byte[] byteArray;
    private ArrayList<AthleteModel> userSelectedList = new ArrayList<>();
    private boolean isImage;

    public static final String APP_DIR = "VideoCompressor";
    public static final String COMPRESSED_VIDEOS_DIR = "/Compressed Videos/";

    private static void try2CreateCompressDir() {
        File f = new File(Environment.getExternalStorageDirectory(), File.separator + APP_DIR + COMPRESSED_VIDEOS_DIR);
        f.mkdirs();
    }

    public void compressVideo(final String videoPath, ArrayList<AthleteModel> userSelectedList) {
        this.userSelectedList = userSelectedList;
        try2CreateCompressDir();
        final String outPath = Environment.getExternalStorageDirectory()
                + File.separator
                + APP_DIR
                + COMPRESSED_VIDEOS_DIR
                + "video.mp4";

        VideoCompress.compressVideoMedium(videoPath, outPath, new VideoCompress.CompressListener() {
            @Override
            public void onStart() {
                //Start Compress
                LogUtils.LOGD("Compress", "its started");
            }

            @Override
            public void onSuccess() {
                LogUtils.LOGD("Compress", "its done");
                File file = new File(outPath);
                long length = file.length();
                length = length / 1024;

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(new File(outPath));

                    byte[] buf = new byte[1024];
                    int n;
                    while (-1 != (n = fis.read(buf)))
                        baos.write(buf, 0, n);

                    byteArray = baos.toByteArray();

                    getVideoUploadPath(ApiEndPoints.upload_video.getApi() + "?file_name=movie.m4v&file_size=" + length);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail() {
                //Failed
                LogUtils.LOGD("Compress", "its failed");
            }

            @Override
            public void onProgress(float percent) {
                //Progress
                LogUtils.LOGD("Compress", "in progress");
            }
        });
    }

    private void getVideoUploadPath(String url) {
        isImage = false;

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
            JSONObject jsonRequest = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            JSONArray array = new JSONArray();

            String content = PrefUtils.getString("CONTENT_TYPE");
            int parentId = PrefUtils.getInt("PARENT_ID");

            if (userSelectedList != null && !userSelectedList.isEmpty()) {

                for (int i = 0; i < userSelectedList.size(); i++) {
                    array.put(String.valueOf(userSelectedList.get(i).getId()));
                }
            }
            if (isImage) {
                try {
                    if (TextUtils.isEmpty(content))
                        jsonObject.put("content_type", "image");
                    else
                        jsonObject.put("content_type", content);
                    jsonObject.put("image_guid", uploadUrlModel.getGuid());
                    jsonObject.put("tags", array);
                    if (parentId != 0)
                        jsonObject.put("parent_id", parentId);

                    jsonRequest.put("post", jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    VideoUploadModel videoUploadModel = JsonUtils.from(response, VideoUploadModel.class);

                    if (TextUtils.isEmpty(content))
                        jsonObject.put("content_type", "video");
                    else
                        jsonObject.put("content_type", content);

                    jsonObject.put("video_guid", videoUploadModel.getPath());
                    jsonObject.put("panda_video_id", videoUploadModel.getId());
                    jsonObject.put("tags", array);

                    if (parentId != 0) {
                        jsonObject.put("parent_id", parentId);

                        if(content.equals("answer")){
                            jsonObject.put("parent_type", "Question");
                        } else {
                            jsonObject.put("parent_type", "Post");
                        }
                    }

                    jsonRequest.put("post", jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            postContent(jsonRequest.toString());
            PrefUtils.putString("CONTENT_TYPE", "");
            PrefUtils.putInt("PARENT_ID", 0);
        } else {
            LogUtils.LOGD("Hurray", "file Uploaded");
        }
    }

    private void postContent(String jsonRequest) {
        new HttpServiceUtil(
                this,
                ApiEndPoints.post_content.getApi(),
                ProsConstants.POST_METHOD,
                jsonRequest,
                ApiEndPoints.post_content.getTag()
        ).execute();
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

    public void getImageUploadUrl(byte[] byteArray, ArrayList<AthleteModel> userSelectedList) {
        this.userSelectedList = userSelectedList;
        this.byteArray = byteArray;
        isImage = true;

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
