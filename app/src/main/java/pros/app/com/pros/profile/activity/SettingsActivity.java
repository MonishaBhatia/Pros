package pros.app.com.pros.profile.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.base.BaseActivity;
import pros.app.com.pros.base.CustomDialogFragment;
import pros.app.com.pros.base.CustomDialogListener;
import pros.app.com.pros.base.PrefUtils;
import pros.app.com.pros.launch_screen.LaunchActivity;
import pros.app.com.pros.profile.views.SettingsView;
import pros.app.com.pros.profile.presenter.SettingsPresenter;

public class SettingsActivity extends BaseActivity implements SettingsView, CustomDialogListener {

    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvNumFollowing)
    TextView tvNumFollowing;

    private SettingsPresenter settingsPresenter;
    private static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);

        settingsPresenter = new SettingsPresenter(this);
        initializeViews();
    }

    private void initializeViews() {

//        Picasso.get().load(PrefUtils.getUser().getAvatar().getMediumUrl()).error(R.drawable.ic_account).into(ivAvatar);

        tvName.setText(String.format("%s %s", PrefUtils.getUser().getFirstName(), PrefUtils.getUser().getLastName()));
        tvNumFollowing.setText(String.valueOf(getIntent().getIntExtra("Follow_Count", 0)));
    }

    @OnClick(R.id.tvContact)
    public void onClickContactAdmin() {

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.setType("message/rfc822");
        emailIntent.setData(Uri.parse("mailto:hello@theprosapp.com"));
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
        } catch (ActivityNotFoundException e) {
            openDialog(getString(R.string.email_error_title), getString(R.string.email_error_content), "Close");
        }
    }

    @OnClick(R.id.ivBack)
    public void onClickBack() {
        finish();
    }

    @OnClick(R.id.tvChangePswd)
    public void onClickChangePswd() {
        ChangePasswordFragment.newInstance().show(this.getSupportFragmentManager(), ChangePasswordFragment.TAG);
    }

    @OnClick(R.id.tvLogout)
    public void onClickLogout() {
        settingsPresenter.onLogout();
    }

    @OnClick(R.id.tvDeactivate)
    public void onClickDeactivate() {
        CustomDialogFragment customDialogFragment = new CustomDialogFragment();
        customDialogFragment.registerCallbackListener(this);
        Bundle bundle = new Bundle();
        bundle.putString("Title", getString(R.string.deactivate_title));
        bundle.putString("Content", getString(R.string.deactivate_account_content));
        bundle.putString("Action1", "Yes");
        bundle.putString("Action2", "Cancel");
        customDialogFragment.setArguments(bundle);
        customDialogFragment.show(this.getSupportFragmentManager(), CustomDialogFragment.TAG);
    }


    @OnClick({R.id.tvNumFollowing, R.id.labelFollowing})
    public void onCLickFollow() {
        //TODO:Call Following fragment
    }

    @OnClick(R.id.ivAvatar)
    public void onClickAvatar() {
        //Todo: Open Camera and set image

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivAvatar.setImageBitmap(imageBitmap);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            File finalFile = new File(getRealPathFromURI(tempUri));

            settingsPresenter.getUploadUrl(Uri.fromFile(finalFile));

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    public void onSucessLogout() {
        PrefUtils.clearAllSharedPref();
        Intent intent = new Intent(this, LaunchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onFailure(String message) {
        openDialog("Sorry!", message, "Close");
    }

    @Override
    public void handleYes() {
        settingsPresenter.onDeactivate();
    }

    @Override
    public void handleNo() {

    }
}
