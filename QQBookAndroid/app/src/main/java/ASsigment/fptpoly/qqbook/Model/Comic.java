package ASsigment.fptpoly.qqbook.Model;

public class Comic {
    private String _id;
    private String name;
    private String description;

    private String price;
    private String author;
    private String type;
    private String publishYear;
    private String coverImage;
    private String images;

    public Comic() {
    }

    public Comic(String _id, String name, String description, String price, String author, String type, String publishYear, String coverImage, String images) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.author = author;
        this.type = type;
        this.publishYear = publishYear;
        this.coverImage = coverImage;
        this.images = images;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(String publishYear) {
        this.publishYear = publishYear;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
