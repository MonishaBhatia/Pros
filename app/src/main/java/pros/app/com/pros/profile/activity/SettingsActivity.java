package pros.app.com.pros.profile.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.base.BaseActivity;
import pros.app.com.pros.base.CustomDialogFragment;
import pros.app.com.pros.base.CustomDialogListener;
import pros.app.com.pros.base.PrefUtils;
import pros.app.com.pros.launch_screen.LaunchActivity;
import pros.app.com.pros.profile.presenter.SettingsPresenter;
import pros.app.com.pros.profile.views.SettingsView;

import static pros.app.com.pros.base.ProsConstants.FOLLOWING_LIST;
import static pros.app.com.pros.base.ProsConstants.IS_FAN;
import static pros.app.com.pros.base.ProsConstants.PROFILE_ID;

public class SettingsActivity extends BaseActivity implements SettingsView, CustomDialogListener {

    @BindView(R.id.ivPic)
    ImageView ivPic;
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvNumFollowing)
    TextView tvNumFollowing;
    @BindView(R.id.tvContact)
    TextView tvContact;
    @BindView(R.id.tvNumFollower)
    TextView tvNumFollower;
    @BindView(R.id.viewFollower)
    View viewFollower;
    @BindView(R.id.viewFollowing)
    View viewFollowing;
    @BindView(R.id.separator)
    View separator;

    private SettingsPresenter settingsPresenter;
    private static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ButterKnife.bind(this);

        settingsPresenter = new SettingsPresenter(this);
        initializeViews();
    }

    private void initializeViews() {

        if (PrefUtils.isAthlete()) {
            viewFollower.setVisibility(View.VISIBLE);
            separator.setVisibility(View.VISIBLE);
            tvNumFollower.setText(String.valueOf(getIntent().getIntExtra("Follower_Count", 0)));
            tvContact.setText(getString(R.string.invite_a_pro));
        } else {
            viewFollower.setVisibility(View.GONE);
            separator.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(PrefUtils.getUser().getMediumUrl()))
            Picasso.get().load(PrefUtils.getUser().getMediumUrl()).into(ivPic);
        else if (!TextUtils.isEmpty(PrefUtils.getString("Image")))
            Picasso.get().load(PrefUtils.getString("Image")).into(ivPic);

        tvName.setText(String.format("%s %s", PrefUtils.getUser().getFirstName(), PrefUtils.getUser().getLastName()));
        tvNumFollowing.setText(String.valueOf(getIntent().getIntExtra("Follow_Count", 0)));
    }

    @OnClick(R.id.tvContact)
    public void onClickContactAdmin() {

        if (PrefUtils.isAthlete()) {
            InviteAProFragment.newInstance().show(this.getSupportFragmentManager(), InviteAProFragment.TAG);
        } else {
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
        Intent intent = new Intent(this, FollowingActivity.class);
        intent.putExtra(PROFILE_ID, PrefUtils.getUser().getId());
        intent.putExtra(FOLLOWING_LIST, true);
        intent.putExtra(IS_FAN, true);
        startActivity(intent);
    }

    @OnClick({R.id.tvNumFollower, R.id.labelFollower})
    public void onCLickFollower() {
        Intent intent = new Intent(this, FollowingActivity.class);
        intent.putExtra(PROFILE_ID, PrefUtils.getUser().getId());
        startActivity(intent);
    }

    @OnClick(R.id.ivAvatar)
    public void onClickAvatar() {
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
            ivPic.setImageBitmap(imageBitmap);



            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            File finalFile = new File(getRealPathFromURI(tempUri));

            byte[] byteArray = baos.toByteArray();
            imageBitmap.recycle();

            PrefUtils.putString("Image", Uri.fromFile(finalFile).toString());
            settingsPresenter.getUploadUrl(byteArray);
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
