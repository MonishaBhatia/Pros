package pros.app.com.pros.account.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserModel {

    @JsonProperty("id")
    private int id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("email")
    private String email;
    @JsonProperty("api_key")
    private String apiKey;
    @JsonProperty("globalId")
    private String global_id;
    @JsonProperty("active")
    private boolean active;
    @JsonProperty("account_status")
    private String accountStatus;
    @JsonProperty("user_type")
    private String userType;
    @JsonProperty("avatar")
    private AvatarModel avatar;

    public UserModel(String firstName, String lastName, String email, String apiKey, String userType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.apiKey = apiKey;
        this.userType = userType;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getGlobal_id() {
        return global_id;
    }

    public boolean isActive() {
        return active;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public String getUserType() {
        return userType;
    }

    public AvatarModel getAvatar() {
        return avatar;
    }
}
