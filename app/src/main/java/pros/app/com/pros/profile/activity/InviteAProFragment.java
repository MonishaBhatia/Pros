package pros.app.com.pros.profile.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.base.BaseDialogFragment;
import pros.app.com.pros.base.BaseView;
import pros.app.com.pros.base.CustomDialogFragment;
import pros.app.com.pros.profile.presenter.ChangePasswordPresenter;

public class InviteAProFragment extends BaseDialogFragment implements BaseView {

    private static final int READ_CONTACT_REQUEST = 101;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvMessage)
    TextView tvMessage;
    @BindView(R.id.tvContacts)
    TextView tvContacts;
    @BindView(R.id.edtName)
    EditText edtName;
    @BindView(R.id.edtNumber)
    EditText edtNumber;
    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.tvSend)
    TextView tvSend;

    private ChangePasswordPresenter changePasswordPresenter;
    static final int PICK_CONTACT = 1;

    public static final String TAG = "InviteAProFragment";

    @Override
    protected int getResourceId() {
        return R.layout.layout_invite_pro;
    }

    public static InviteAProFragment newInstance() {
        InviteAProFragment fragment = new InviteAProFragment();
        return fragment;
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String val = charSequence.toString();
            String a = "";
            String b = "";
            String c = "";
            if (val != null && val.length() > 0) {
                val = val.replace("-", "");
                if (val.length() >= 3) {
                    a = val.substring(0, 3);
                } else if (val.length() < 3) {
                    a = val.substring(0, val.length());
                }
                if (val.length() >= 6) {
                    b = val.substring(3, 6);
                    c = val.substring(6, val.length());
                } else if (val.length() > 3 && val.length() < 6) {
                    b = val.substring(3, val.length());
                }
                StringBuffer stringBuffer = new StringBuffer();
                if (a != null && a.length() > 0) {
                    stringBuffer.append(a);
                    if (a.length() == 3 && b.length() > 0) {
                        stringBuffer.append("-");
                    }
                }
                if (b != null && b.length() > 0) {
                    stringBuffer.append(b);
                    if (b.length() == 3 && c.length() > 0) {
                        stringBuffer.append("-");
                    }
                }
                if (c != null && c.length() > 0) {
                    stringBuffer.append(c);
                }
                edtNumber.removeTextChangedListener(this);
                edtNumber.setText(stringBuffer.toString());
                edtNumber.setSelection(edtNumber.getText().toString().length());
                edtNumber.addTextChangedListener(this);
            } else {
                edtNumber.removeTextChangedListener(this);
                edtNumber.setText("");
                edtNumber.addTextChangedListener(this);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        changePasswordPresenter = new ChangePasswordPresenter(this);

        setStyle(STYLE_NO_TITLE, R.style.DialogTheme);
        Window window = getDialog().getWindow();
        // set "origin" to top left corner, so to speak
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.requestFeature(Window.FEATURE_NO_TITLE);
        // after that, setting values for x and y works "naturally"
        WindowManager.LayoutParams params = window.getAttributes();
        window.setAttributes(params);

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    protected String getClassTag() {
        return TAG;
    }


    @OnClick(R.id.tvEmail)
    public void onClickEmail() {
        changeLayout();
        edtEmail.setVisibility(View.VISIBLE);
        edtNumber.setVisibility(View.GONE);
    }


    @OnClick(R.id.tvMessage)
    public void onClickMsg() {
        edtNumber.addTextChangedListener(watcher);
        changeLayout();
        edtEmail.setVisibility(View.GONE);
        edtNumber.setVisibility(View.VISIBLE);


    }

    private void changeLayout() {
        tvEmail.setVisibility(View.GONE);
        tvMessage.setVisibility(View.GONE);
        tvContacts.setVisibility(View.GONE);
        edtName.setVisibility(View.VISIBLE);
        tvSend.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tvContacts)
    public void onClickContact() {

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACT_REQUEST);
        } else {
            openContacts();
        }
    }

    private void openContacts() {
        dismiss();
        Intent intent = new Intent(getActivity(), ContactListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_CONTACT_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    openContacts();

                } else {

                    if (ActivityCompat.shouldShowRequestPermissionRationale
                            (requireActivity(), Manifest.permission.CAMERA)) {

                        openDialog("", "Change your Settings to allow Pros to access Contacts", "Ok");

                    }
                }
                return;
            }
        }
    }

    @OnClick(R.id.tvSend)
    public void onClickSend() {
        JSONObject jsonRequest = new JSONObject();
        try {

            if (!TextUtils.isEmpty(edtEmail.getText().toString())) {
                jsonRequest.put("email", edtEmail.getText().toString());
            }

            if (!TextUtils.isEmpty(edtNumber.getText().toString()) && edtNumber.getText().toString().length() == 12) {
                jsonRequest.put("phone_number", edtNumber.getText().toString().replace("-", ""));
            } else {
                openDialog("", "Enter 10 Digit Mobile Number", "CLOSE");
                return;
            }

            if (!TextUtils.isEmpty(edtName.getText().toString())) {
                jsonRequest.put("name", edtName.getText().toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        changePasswordPresenter.sendInvite(jsonRequest.toString());
    }

    @Override
    public void onSuccess() {
        dismiss();
        openDialog("Success", "Your invite has been sent!", "Ok");
    }

    private void openDialog(String title, String message, String action) {
        Bundle bundle = new Bundle();
        bundle.putString("Title", title);
        bundle.putString("Content", message);
        bundle.putString("Action2", action);
        CustomDialogFragment.newInstance(bundle).show(getActivity().getSupportFragmentManager(), CustomDialogFragment.TAG);
    }

    @Override
    public void onFailure(int message) {
        dismiss();
        openDialog("Pending Invite", "This Athlete has a pending invitation to join Pros", "Close");
    }
}
