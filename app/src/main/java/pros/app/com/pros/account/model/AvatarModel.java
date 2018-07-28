package pros.app.com.pros.account.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AvatarModel {

    @JsonProperty("thumbnail_url")
    private Object thumbnailUrl;
    @JsonProperty("medium_url")
    private Object mediumUrl;
    @JsonProperty("original_url")
    private Object originalUrl;

    public Object getThumbnailUrl() {
        return thumbnailUrl;
    }

    public Object getMediumUrl() {
        return mediumUrl;
    }

    public Object getOriginalUrl() {
        return originalUrl;
    }
}
