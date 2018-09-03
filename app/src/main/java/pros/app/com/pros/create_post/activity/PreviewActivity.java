package pros.app.com.pros.create_post.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.vincent.videocompressor.VideoCompress;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.base.ApiEndPoints;
import pros.app.com.pros.base.LogUtils;
import pros.app.com.pros.create_post.presenter.CreatePostPresenter;

public class PreviewActivity extends AppCompatActivity {

    @BindView(R.id.image)
    ImageView imageView;

    @BindView(R.id.video)
    VideoView videoView;

    private CreatePostPresenter createPostPresenter;

    public static final String APP_DIR = "VideoCompressor";

    public static final String COMPRESSED_VIDEOS_DIR = "/Compressed Videos/";

    public static final String TEMP_DIR = "/Temp/";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);

        setupToolbar();

        if (getIntent().getBooleanExtra("fromPicker", false)) {

            if (getIntent().hasExtra("videoFileUri")) {
                String fileUri = getIntent().getStringExtra("videoFileUri");
                playVideo(fileUri);
            } else if (getIntent().hasExtra("imageFileUri")) {
                String fileUri = getIntent().getStringExtra("imageFileUri");
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageURI(Uri.parse(fileUri));
            }


        } else if (getIntent().getBooleanExtra("fromCamera", false)) {

            byte[] jpeg = ResultHolder.getImage();
            File video = ResultHolder.getVideo();
            createPostPresenter = new CreatePostPresenter();

            if (jpeg != null) {
                Log.e("Image Path:", "Yes");
                imageView.setVisibility(View.VISIBLE);

                Bitmap bitmap = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length);

                if (bitmap == null) {
                    finish();
                    return;
                }
                imageView.setImageBitmap(bitmap);

            } else if (video != null) {
                Log.e("Vode Path:", video.getPath());
                playVideo(video.getAbsolutePath());
            }

        } else {
            finish();
            return;
        }
    }

    void playVideo(final String uri) {
        videoView.setVisibility(View.VISIBLE);
        videoView.setVideoURI(Uri.parse(uri));
        MediaController mediaController = new MediaController(this);
        mediaController.setVisibility(View.GONE);
        videoView.setMediaController(mediaController);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(false);
                mp.start();
                float multiplier = (float) videoView.getWidth() / (float) mp.getVideoWidth();
                videoView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (mp.getVideoHeight() * multiplier)));

            try2CreateCompressDir();
            final String outPath = Environment.getExternalStorageDirectory()
                    + File.separator
                    + APP_DIR
                    + COMPRESSED_VIDEOS_DIR;

                VideoCompress.compressVideoLow(uri, outPath, new VideoCompress.CompressListener() {
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

                        createPostPresenter.getuploadPath(ApiEndPoints.upload_video.getApi() + "?file_name=movie.m4v&file_size=" + length, uri);
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

        });
}


    public static void try2CreateCompressDir() {
        File f = new File(Environment.getExternalStorageDirectory(), File.separator + APP_DIR + COMPRESSED_VIDEOS_DIR);
        f.mkdirs();
    }


    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            View toolbarView = getLayoutInflater().inflate(R.layout.action_bar, null, false);
            TextView titleView = toolbarView.findViewById(R.id.toolbar_title);
            titleView.setText(Html.fromHtml("<b>Camera</b>Kit"));

            getSupportActionBar().setCustomView(toolbarView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private static float getApproximateFileMegabytes(Bitmap bitmap) {
        return (bitmap.getRowBytes() * bitmap.getHeight()) / 1024 / 1024;
    }

    @OnClick(R.id.close_button)
    void closeActivity() {
        this.finish();
    }

}
