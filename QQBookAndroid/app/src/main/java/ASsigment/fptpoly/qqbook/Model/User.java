package ASsigment.fptpoly.qqbook.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {
    @SerializedName("_id")
    private String _id;
    private String username;
    private String password;
    private String email;
    private String avatar;
    private String fullname;
    private String coin;
    private List<Favorite> favorite;

    // Constructor và các phương thức getter và setter


    public User(String _id, String username, String password, String email, String avatar, String fullname, String coin, List<Favorite> favorite) {
        this._id = _id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.avatar = avatar;
        this.fullname = fullname;
        this.coin = coin;
        this.favorite = favorite;
    }

    public User(List<Favorite> favorite) {
        this.favorite = favorite;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String email, String fullname) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullname = fullname;
    }

    public User() {
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public List<Favorite> getFavorite() {
        return favorite;
    }

    public void setFavorite(List<Favorite> favorite) {
        this.favorite = favorite;
    }
}
