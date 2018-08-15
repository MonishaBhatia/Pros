package pros.app.com.pros.home.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LikeModel {

    @JsonProperty("count")
    private int count;
    @JsonProperty("liked_by_current_user")
    private boolean likedByCurrentUser;

    public int getCount() {
        return count;
    }

    public boolean isLikedByCurrentUser() {
        return likedByCurrentUser;
    }
}
