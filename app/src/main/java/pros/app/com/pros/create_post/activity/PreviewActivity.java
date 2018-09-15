package pros.app.com.pros.create_post.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.vincent.videocompressor.VideoCompress;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.base.LogUtils;
import pros.app.com.pros.create_question.activity.TagsActivity;

public class PreviewActivity extends AppCompatActivity {

    @BindView(R.id.image)
    ImageView imageView;

    @BindView(R.id.video)
    VideoView videoView;

    public static final String APP_DIR = "VideoCompressor";

    public static final String COMPRESSED_VIDEOS_DIR = "/Compressed Videos/";

    public static final String TEMP_DIR = "/Temp/";
    private String fileUri;
    private byte[] imageByteArray;
    private byte[] videoBytes;
    private long length;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);

        setupToolbar();

        if (getIntent().getBooleanExtra("fromPicker", false)) {

            if (getIntent().hasExtra("videoFileUri")) {
                fileUri = getIntent().getStringExtra("videoFileUri");
                playVideo(fileUri);

            } else if (getIntent().hasExtra("imageFileUri")) {
                fileUri = getIntent().getStringExtra("imageFileUri");
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageURI(Uri.parse(fileUri));
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(fileUri));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                convertImageToByteArray(bitmap);
            }

        } else if (getIntent().getBooleanExtra("fromCamera", false)) {

            imageByteArray = ResultHolder.getImage();
            File video = ResultHolder.getVideo();

            if (imageByteArray != null) {
                imageView.setVisibility(View.VISIBLE);

                Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);

                if (bitmap == null) {
                    finish();
                    return;
                }
                imageView.setImageBitmap(bitmap);
                convertImageToByteArray(bitmap);

            } else if (video != null) {
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
                        length = file.length();
                        length = length / 1024;

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        FileInputStream fis = null;
                        try {
                            fis = new FileInputStream(new File(uri));

                            byte[] buf = new byte[1024];
                            int n;
                            while (-1 != (n = fis.read(buf)))
                                baos.write(buf, 0, n);

                            videoBytes = baos.toByteArray();

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

        });
    }

    private void convertImageToByteArray(Bitmap bitmap) {

        Bitmap converetdImage = getResizedBitmap(bitmap, 500);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        converetdImage.compress(Bitmap.CompressFormat.PNG, 80, stream);
        imageByteArray = stream.toByteArray();
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
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

    @OnClick(R.id.close_button)
    void closeActivity() {
        this.finish();
    }

    @OnClick(R.id.post_content_button)
    public void moveForward() {
        Intent intents = new Intent(PreviewActivity.this, TagsActivity.class);
        intents.putExtra("ImageByte", imageByteArray);
        intents.putExtra("VideoByte", videoBytes);
        intents.putExtra("VideoLength", length);
        startActivity(intents);
    }
}
