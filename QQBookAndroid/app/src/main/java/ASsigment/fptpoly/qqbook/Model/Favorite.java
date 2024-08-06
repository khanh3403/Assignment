package ASsigment.fptpoly.qqbook.Model;

public class Favorite {
    private String _id;
    private String comicId;

    public Favorite() {
    }

    public Favorite(String _id, String comicId) {
        this._id = _id;
        this.comicId = comicId;
    }

    public Favorite(String comicId) {
        this.comicId = comicId;
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
}
