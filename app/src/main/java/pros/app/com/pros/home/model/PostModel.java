package pros.app.com.pros.home.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostModel {

    @JsonProperty("id")
    private int id;

    @JsonProperty("text")
    private String text;

    @JsonProperty("content_type")
    private String contentType;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("share_url")
    private String shareUrl;

    @JsonProperty("share_text")
    private String shareText;

    @JsonProperty("urls")
    private UrlModel urls;

    @JsonProperty("share_count")
    private int shareCount;

    @JsonProperty("athlete")
    private AthleteModel athlete;

    @JsonProperty("questioner")
    private AthleteModel questioner;

    @JsonProperty("likes")
    private LikeModel likes;

    @JsonProperty("hashtags")
    private List<HashtagModel> hashtags;

    @JsonProperty("mentions")
    private List<AthleteModel> mentions;

    @JsonProperty("reactions")
    private List<PostModel> reactions;

    @JsonProperty("comments")
    private List<PostModel> comments;


    public int getId() {
        return id;
    }

    public String getContentType() {
        return contentType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public String getShareText() {
        return shareText;
    }

    public UrlModel getUrls() {
        return urls;
    }

    public int getShareCount() {
        return shareCount;
    }

    public AthleteModel getAthlete() {
        return athlete;
    }

    public LikeModel getLikes() {
        return likes;
    }

    public List<HashtagModel> getHashtags() {
        return hashtags;
    }

    public List<AthleteModel> getMentions() {
        return mentions;
    }

    public List<PostModel> getReactions() {
        return reactions;
    }

    public List<PostModel> getComments() {
        return comments;
    }

    public String getText() {
        return text;
    }

    public AthleteModel getQuestioner() {
        return questioner;
    }
}
