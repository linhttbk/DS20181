package a20181.ds.com.ds20181.models;

import com.google.gson.annotations.SerializedName;

public class SignUpBody {
    @SerializedName("user")
    private UserBody body;

    public SignUpBody(UserBody body) {
        this.body = body;
    }

    public static class UserBody{
        @SerializedName("username")
        private String username;
        @SerializedName("password")
        private String password;
        @SerializedName("name")
        private String name;

        public UserBody(String username, String password, String name) {
            this.username = username;
            this.password = password;
            this.name = name;
        }
    }
}
