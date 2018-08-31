package pros.app.com.pros.detail.fragment;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import pros.app.com.pros.R;
import pros.app.com.pros.base.DateUtils;
import pros.app.com.pros.detail.adapter.MentionsAdapter;
import pros.app.com.pros.detail.adapter.ReactionAthlete;
import pros.app.com.pros.home.model.AthleteModel;
import pros.app.com.pros.home.model.PostModel;
import pros.app.com.pros.search.adapter.TopProsAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment  {
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

    @BindView(R.id.athlete_thumb)
    CircleImageView athleteThumb;

    @BindView(R.id.likes_count)
    TextView likesCount;

    @BindView(R.id.comment_count)
    TextView commentsCount;

    @BindView(R.id.comments_iv)
    ImageView commentsIcon;

    @BindView(R.id.question_container)
    ConstraintLayout questionContainer;

    @BindView(R.id.question_text)
    TextView questionText;

    @BindView(R.id.question_athlete_name)
    TextView questionAthleteName;

    @BindView(R.id.videoView)
    VideoView videoView;

    @BindView(R.id.athlete_list)
    RecyclerView athleteRecyclerview;

    @BindView(R.id.mentions_list)
    RecyclerView mentionsListRecyclerview;

    @BindView(R.id.tags_iv)
    ImageView mentionsIcon;

    boolean videoVisibleToUser = false;

    private int reactionVideoIndex = 0;
    private ArrayList<String> totalReactionsVideos = new ArrayList<>();
    private boolean toggleMentions = false;

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
            commentsCount.setVisibility(View.VISIBLE);
            commentsIcon.setVisibility(View.VISIBLE);
            commentsCount.setText(""+receivedPostModel.getComments().size());

        } else if(receivedPostModel.getQuestioner() != null){
            athleteFullName = receivedPostModel.getQuestioner().getName();
            thumbnailUrl = receivedPostModel.getQuestioner().getAvatar().getThumbnailUrl();
            athleteThumbnailUrl = receivedPostModel.getQuestioner().getAvatar().getThumbnailUrl();
            questionContainer.setVisibility(View.VISIBLE);
            questionText.setText(receivedPostModel.getText());
            questionAthleteName.setText(receivedPostModel.getQuestioner().getName());

            List<PostModel> reactionsList = receivedPostModel.getReactions();
            if(reactionsList.size() >0){
                for(int i=0; i< reactionsList.size(); i++) {
                    totalReactionsVideos.add(reactionsList.get(i).getUrls().getMobileUrl());
                }

               playAllVideos();


            }

        }

        String dateDifference = DateUtils.getDateDifference(receivedPostModel.getCreatedAt(), true);

        athleteName.setText(athleteFullName);
        Picasso.get().load(thumbnailUrl).into(thumbnailBackground);
        Picasso.get().load(athleteThumbnailUrl).into(athleteThumb);
        createdAt.setText(dateDifference);
        likesCount.setText("" + receivedPostModel.getLikes().getCount());

        List<PostModel> reactionsList = receivedPostModel.getReactions();
        List<AthleteModel> mentionsList = receivedPostModel.getMentions();

        if(reactionsList.size() >0){

            ArrayList<AthleteModel> athleteModels = new ArrayList<>();

            for(int i=0; i<reactionsList.size(); i++){
                athleteModels.add(reactionsList.get(i).getAthlete());
            }

            ReactionAthlete reactionAthleteAdapter = new ReactionAthlete(getActivity(), athleteModels);

            athleteRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            athleteRecyclerview.setAdapter(reactionAthleteAdapter);
        }


        if(mentionsList.size() >0){
            mentionsIcon.setVisibility(View.VISIBLE);
            MentionsAdapter mentionsAdapter = new MentionsAdapter(mentionsList);

            mentionsListRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mentionsListRecyclerview.setAdapter(mentionsAdapter);
        }

        if(contentType != null && contentType.equalsIgnoreCase("video")){
            videoView.setVideoPath(receivedPostModel.getUrls().getMobileUrl());
            if(videoVisibleToUser){
                videoView.start();
            }
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    thumbnailBackground.setVisibility(View.GONE);
                }
            });
        }

    }

    private void playAllVideos() {
        if(reactionVideoIndex < totalReactionsVideos.size()) {
            videoView.setVideoPath(totalReactionsVideos.get(reactionVideoIndex));
            reactionVideoIndex++;
            if(videoVisibleToUser){
                videoView.start();
            }
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    thumbnailBackground.setVisibility(View.GONE);
                    questionContainer.setVisibility(View.GONE);
                }
            });
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        videoView.stopPlayback();
                        videoView.suspend();
                        playAllVideos();
                    }
                });
            }
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
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

            if (!isVisibleToUser)   // If we are becoming invisible, then...
            {
                //pause or stop vide
                if(videoView != null) {
                    videoView.stopPlayback();
                }
                videoVisibleToUser = false;
            }

            if (isVisibleToUser) // If we are becoming visible, then...
            {
                videoVisibleToUser = true;
                if(receivedPostModel !=null) {
                    String contentType = receivedPostModel.getContentType();

                    //play your video
                    if (contentType != null && contentType.equalsIgnoreCase("video") && videoView != null) {
                        videoView.setVideoPath(receivedPostModel.getUrls().getMobileUrl());
                        videoView.start();
                    }
                    else if(receivedPostModel.getQuestioner() != null){
                        List<PostModel> reactionsList = receivedPostModel.getReactions();
                        if(reactionsList.size() >0){
                            for(int i=0; i< reactionsList.size(); i++) {
                                totalReactionsVideos.add(reactionsList.get(i).getUrls().getMobileUrl());
                            }

                            playAllVideos();

                        }

                    }

                }

            }
    }

    @OnClick(R.id.tags_iv)
    void onMentionIconClick(){
        toggleMentions = !toggleMentions;
        if(toggleMentions) {
            mentionsListRecyclerview.setVisibility(View.VISIBLE);
        }else {
            mentionsListRecyclerview.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.share_iv)
    void shareDetails(){
        if(receivedPostModel != null) {

            String shareText = receivedPostModel.getShareText()+ " " + receivedPostModel.getShareUrl();

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            sendIntent.setType("text/plain");
            getActivity().startActivity(Intent.createChooser(sendIntent, "Send To"));
        }
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
