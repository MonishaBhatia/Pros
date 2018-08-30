package pros.app.com.pros.detail.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pros.app.com.pros.R;
import pros.app.com.pros.home.model.PostModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_POST_MODEL = "post_model";

    // TODO: Rename and change types of parameters
    private PostModel receivedPostModel;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.athlete_name)
    TextView athleteName;

    @BindView((R.id.created_at))
    TextView createdAt;

    @BindView(R.id.thumbnail_background)
    ImageView thumbnailBackground;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param postModel Parameter 1.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(PostModel postModel) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_POST_MODEL, postModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            receivedPostModel = getArguments().getParcelable(ARG_POST_MODEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_layout, container, false);
        ButterKnife.bind(this, view);
        setupUI();
        // TODO Use fields...
        return view;
    }

    private void setupUI() {
        String contentType = receivedPostModel.getContentType();
        String athleteFullName = "";
        String thumbnailUrl = "";
        String athleteThumbnailUrl = "";

        if(contentType != null &&
                (contentType.equalsIgnoreCase("image") || contentType.equalsIgnoreCase("video"))) {
            thumbnailUrl= receivedPostModel.getUrls().getThumbnailUrl();
            athleteThumbnailUrl =  receivedPostModel.getAthlete().getAvatar().getThumbnailUrl();
            athleteFullName = receivedPostModel.getAthlete().getFirstName() + " " + receivedPostModel.getAthlete().getLastName();
        } else if(receivedPostModel.getQuestioner() != null){
            athleteFullName = receivedPostModel.getQuestioner().getName();
            thumbnailUrl = receivedPostModel.getQuestioner().getAvatar().getThumbnailUrl();
            athleteThumbnailUrl = receivedPostModel.getQuestioner().getAvatar().getThumbnailUrl();
        }

        athleteName.setText(athleteFullName);
        Picasso.get().load(thumbnailUrl).into(thumbnailBackground);


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
