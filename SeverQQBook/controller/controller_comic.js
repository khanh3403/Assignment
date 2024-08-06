const Comic = require("../models/ComicModel");

const multer = require("multer");
const path = require("path");
var socket = require("../main");

const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    if (file.fieldname === "coverImage") {
      cb(null, "images");
    } else if (file.fieldname === "images") {
      cb(null, "pdf");
    } else {
      cb({ error: "Invalid field name" });
    }
  },
  filename: (req, file, cb) => {
    const ext = path.extname(file.originalname);
    cb(null, `${Date.now()}${ext}`);
  },
});
const upload = multer({ storage: storage });

const postComicController = (req, res) => {
  upload.fields([
    { name: "coverImage", maxCount: 1 },
    { name: "images", maxCount: 1 },
  ])(req, res, (err) => {
    if (err) {
      return res.status(500).json({ error: "Error uploading files" });
    }

    console.log(req.files);

    if (!req.files["coverImage"] || !req.files["images"]) {
      return res.status(400).json({
        error: "Vui lòng tải lên cả ảnh bìa và danh sách ảnh nội dung truyện.",
      });
    }

    const newData = new Comic({
      name: req.body.name,
      description: req.body.description,
      price: req.body.price,
      author: req.body.author,
      type: req.body.type,
      publishYear: req.body.publishYear,
      coverImage: req.files["coverImage"][0].filename,
      images: req.files["images"][0].filename,
    });

    newData
      .save()
      .then(() => {
        res.status(200).json({ message: "Post sản phẩm thành công" });
        socket.io.emit("new_comic_added", " Truyện đã được thêm mới");
      })
      .catch((error) => {
        res.status(500).json({ error: "Post sản phẩm không thành công" });
      });
  });
};
const updateComicController = async (req, res) => {
  upload.fields([
    { name: "coverImage", maxCount: 1 },
    { name: "images", maxCount: 1 },
  ])(req, res, async (err) => {
    try {
      if (err) {
        return res.status(500).json({ error: "Error uploading files" });
      }

      const comicId = req.params.comicId;
      const updatedData = {
        name: req.body.name,
        description: req.body.description,
        price: req.body.price,
        author: req.body.author,
        type: req.body.type,
        publishYear: req.body.publishYear,
      };

      if (req.files["coverImage"]) {
        updatedData.coverImage = req.files["coverImage"][0].filename;
      }
      if (req.files["images"]) {
        updatedData.images = req.files["images"][0].filename;
      }

      const updatedComic = await Comic.findByIdAndUpdate(comicId, updatedData, {
        new: true,
      });

      if (!updatedComic) {
        return res.status(404).json({ error: "Không tìm thấy comic" });
      }

      res.json({
        message: "Comic đã được cập nhật thành công",
        comic: updatedComic,
      });
    } catch (error) {
      console.error("Lỗi khi cập nhật comic:", error);
      res.status(500).json({ error: "Lỗi máy chủ nội bộ" });
    }
  });
};

const deleteComicController = async (req, res) => {
  try {
    const comicId = req.params.comicId;
    const deletedComic = await Comic.findByIdAndDelete(comicId);
    if (!deletedComic) {
      return res.status(404).json({ error: "Không tìm thấy comic" });
    }
    res.json({ message: "Comic đã được xóa thành công" });
  } catch (error) {
    console.error("Lỗi khi xóa comic:", error);
    res.status(500).json({ error: "Lỗi máy chủ nội bộ" });
  }
};
const getComicByIdController = async (req, res) => {
  try {
    const comicId = req.params.comicId;
    const comic = await Comic.findById(comicId);

    if (!comic) {
      return res.status(404).json({ error: "Không tìm thấy comic" });
    }

    res.json(comic);
  } catch (error) {
    console.error("Lỗi khi tìm comic theo id:", error);
    res.status(500).json({ error: "Lỗi máy chủ nội bộ" });
  }
};
const apiGetComicController = async (req, res) => {
  try {
    const products = await Comic.find({});
    res.json(products);
  } catch (err) {
    console.error("Error fetching comics:", err);
    res.status(500).send("Internal Server Error");
  }
};
const searchComicByName = async (req, res) => {
  const name = req.params.name;
  try {
    const regex = new RegExp(name, "i"); // 'i' ở đây cho phép tìm kiếm không phân biệt chữ hoa chữ thường
    const comics = await Comic.find({ name: { $regex: regex } });
    if (comics.length > 0) {
      res.json(comics);
    } else {
      res.status(404).json({ message: "Không tìm thấy comic phù hợp." });
    }
  } catch (error) {
    res
      .status(500)
      .json({ message: "Đã xảy ra lỗi trong quá trình tìm kiếm." });
  }
};

module.exports = {
  postComicController,
  deleteComicController,
  apiGetComicController,
  updateComicController,
  getComicByIdController,
  searchComicByName,
};
