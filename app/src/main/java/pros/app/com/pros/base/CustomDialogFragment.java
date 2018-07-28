package pros.app.com.pros.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import pros.app.com.pros.R;

public class CustomDialogFragment extends BaseDialogFragment {

    public static final String TAG = "CustomDialogFragment";

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvContent)
    TextView tvContent;
    @BindView(R.id.tvAction)
    TextView tvAction;

    public static CustomDialogFragment newInstance(Bundle bundle) {
        CustomDialogFragment fragment = new CustomDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getResourceId() {
        return R.layout.layout_custom_dialog;
    }

    @Override
    protected String getClassTag() {
        return TAG;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme);
        getDialog().setCanceledOnTouchOutside(false);
        Window window = getDialog().getWindow();
        // set "origin" to top left corner, so to speak
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.requestFeature(Window.FEATURE_NO_TITLE);
        // after that, setting values for x and y works "naturally"
        WindowManager.LayoutParams params = window.getAttributes();
        window.setAttributes(params);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @OnClick(R.id.tvAction)
    public void onClickGotIt() {
        dismiss();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            if(bundle.getString("Title")!=null && bundle.getString("Title").trim().length() > 0)
                tvTitle.setText(bundle.getString("Title"));

            tvContent.setText(bundle.getString("Content"));
            tvAction.setText(bundle.getString("Action"));
            tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}
