package ASsigment.fptpoly.qqbook.Model;

public class Comment {
    private String _id;
    private String comicId;
    private String userId;
    private String username;
    private String content;
    private String timestamp;

    public Comment() {
    }

    public Comment(String _id, String comicId, String userId, String content, String timestamp) {
        this._id = _id;
        this.comicId = comicId;
        this.userId = userId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Comment(String comicId, String userId, String content) {
        this.comicId = comicId;
        this.userId = userId;
        this.content = content;
    }

    public Comment(String _id, String content) {
        this._id = _id;
        this.content = content;
    }

    public Comment(String comicId, String userId, String username, String content) {
        this.comicId = comicId;
        this.userId = userId;
        this.username = username;
        this.content = content;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getComicId() {
        return comicId;
    }

    public void setComicId(String comicId) {
        this.comicId = comicId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
