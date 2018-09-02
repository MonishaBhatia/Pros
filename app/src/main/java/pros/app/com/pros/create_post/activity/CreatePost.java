package pros.app.com.pros.create_post.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.base.LogUtils;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class CreatePost extends AppCompatActivity {

    @BindView(R.id.camera)
    CameraView camera;

    private int cameraMethod = CameraKit.Constants.METHOD_STANDARD;
    private boolean cropOutput = false;

    private static final int IMAGE_PICKER_SELECT = 21;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_post);
        ButterKnife.bind(this);

        camera.setMethod(cameraMethod);
        camera.setCropOutput(cropOutput);

    }

    @Override
    protected void onResume() {
        super.onResume();
        camera.start();
    }

    @Override
    protected void onPause() {
        camera.stop();
        super.onPause();
    }

    @OnClick(R.id.close_button)
    void closeActivity(){
        this.finish();
    }

    @OnClick(R.id.gallery_picker_button)
    void openGallery(){
       /* Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*,video/*");
        startActivityForResult(intent, IMAGE_PICKER_SELECT);*/
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // Do something for lollipop and above versions
            Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
            photoPickerIntent.setType("*/*");
            photoPickerIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {"image/*", "video/*"});
            startActivityForResult(photoPickerIntent, IMAGE_PICKER_SELECT);
        } else{
            // do something for phones running an SDK before lollipop
            Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
            //comma-separated MIME types
            mediaChooser.setType("video/*, image/*");
            startActivityForResult(mediaChooser, IMAGE_PICKER_SELECT);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Uri selectedMediaUri = data.getData();
            LogUtils.LOGE("CreatePost", selectedMediaUri.toString());
            if (selectedMediaUri.toString().contains("image")) {
                //handle image
                Intent intent = new Intent(this, PreviewActivity.class);
                intent.putExtra("fromPicker", true);
                intent.putExtra("imageFileUri", selectedMediaUri.toString());
                startActivity(intent);

            } else if (selectedMediaUri.toString().contains("video")) {
                //handle video
            }
        }

    }
}
