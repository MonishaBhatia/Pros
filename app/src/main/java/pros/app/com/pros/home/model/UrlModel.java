package pros.app.com.pros.home.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UrlModel {

    @JsonProperty("mobile_url")
    private String mobileUrl;

    @JsonProperty("intro_url")
    private String introUrl;

    @JsonProperty("share_url")
    private String shareUrl;

    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;

    @JsonProperty("large_url")
    private String largeUrl;

    @JsonProperty("x_large_url")
    private String xLargeUrl;

    @JsonProperty("original_url")
    private String originalUrl;


    public String getMobileUrl() {
        return mobileUrl;
    }

    public String getIntroUrl() {
        return introUrl;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getLargeUrl() {
        return largeUrl;
    }

    public String getxLargeUrl() {
        return xLargeUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }
}
