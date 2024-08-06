const User = require("../models/UserModel");
const multer = require("multer");
const path = require("path");
const { use } = require("../routers/router_api");
const Comics = require("../models/ComicModel");

const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    if (file.fieldname === "avatar") {
      cb(null, "images");
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

const postUserLoginController = async (req, res) => {
  const { username, password } = req.body;
  try {
    const user = await User.findOne({ username: username, password: password });
    if (!user) {
      return res.status(404).json({ message: "Tài khoản mật khẩu không đúng" });
    }
    res.json({ success: true, message: "Đăng nhập thành công", user });
  } catch (error) {
    console.error("Lỗi khi xử lý yêu cầu đăng nhập:", error);
    res.status(500).json({ success: false, error: "Internal Server Error" });
  }
};
const getUserLoginController = async (req, res) => {
  const username = req.params.username;
  try {
    const user = await User.findOne({ username: username });
    if (!user) {
      return res.status(404).json({ message: "người dùng không tồn tại" });
    }
    res.json(user);
  } catch (error) {
    console.error("Lỗi khi xử lý yêu cầu đăng nhập:", error);
  }
};

const postUserController = async (req, res) => {
  const newData = new User({
    username: req.body.username,
    password: req.body.password,
    email: req.body.email,
    avatar: "123.png",
    fullname: req.body.fullname,
    coin: 0,
    favorite: req.body.favorite,
  });
  newData
    .save()
    .then(() => {
      res.status(200).json({ message: "Post tài khoản thành công" });
    })
    .catch((error) => {
      res.status(500).json({ error: "Post tài khoản không thành công" });
    });
};
const buyComicController = async (req, res) => {
  const userId = req.params.userId;
  const comicId = req.body.comicId;

  try {
    const user = await User.findById(userId);
    if (!user) {
      return res.status(404).json({ message: "Không tìm user nào" });
    }
    // user.money = parseInt(user.money - price);

    user.favorite.push({ comicId: comicId });
    const updateUser = await user.save();
    res.status(200).json({ message: "Mua truyện thành công", updateUser });
  } catch (error) {
    console.error("Lỗi khi mua truyện:", error);
    res.status(500).json({ error: "Lỗi máy chủ nội bộ" });
  }
};
const getFavoriteComicIds = async (req, res) => {
  const userId = req.params.userId;

  try {
    const user = await User.findById(userId);
    if (!user) {
      return res.status(404).json({ error: "Không tìm thấy người dùng" });
    }

    const favoriteComicIds = user.favorite.map((favorite) => favorite.comicId);

    res.json({ favoriteComicIds });
  } catch (error) {
    console.error("Lỗi khi lấy danh sách comic yêu thích:", error);
    res.status(500).json({ error: "Lỗi máy chủ nội bộ" });
  }
};
const updateAvatarUserController = async (req, res) => {
  upload.fields([{ name: "avatar", maxCount: 1 }])(req, res, async (err) => {
    try {
      if (err) {
        return res.status(500).json({ error: "Error uploading files" });
      }

      const userId = req.params.userId;
      const updatedData = {
        fullname: req.body.fullname,
        email: req.body.email,
      };

      if (req.files["avatar"]) {
        updatedData.avatar = {
          filename: req.files["avatar"][0].filename,
        };
      }
      const updatedComic = await User.findByIdAndUpdate(userId, updatedData, {
        new: true,
      });

      if (!updatedComic) {
        return res.status(404).json({ error: "Không tìm thấy comic" });
      }

      res.json({
        message: "Thông tin đã được cập nhật thành công",
        user: updatedComic,
      });
    } catch (error) {
      console.error("Lỗi khi cập nhật comic:", error);
      res.status(500).json({ error: "Lỗi máy chủ nội bộ" });
    }
  });
};
const updateCoinUserController = async (req, res) => {
  try {
    const userId = req.params.userId;
    const coinToAdd = parseInt(req.body.coin);
    if (isNaN(coinToAdd)) {
      return res.status(400).json({ error: "Số coin không hợp lệ" });
    }

    const user = await User.findById(userId);

    if (!user) {
      return res.status(404).json({ error: "Không tìm thấy người dùng" });
    }
    user.coin += coinToAdd;

    await user.save();
    res.json({
      message: `Đã cộng thêm ${coinToAdd} coin`,
      user: user,
    });
  } catch (error) {
    console.error("Lỗi khi cộng coin:", error);
    res.status(500).json({ error: "Lỗi máy chủ nội bộ" });
  }
};
const apiGetUserController = async (req, res) => {
  try {
    const user = await User.find({});
    res.json(user);
  } catch (err) {
    console.error("Error fetching users:", err);
    res.status(500).send("Internal Server Error");
  }
};
const deleteUserController = async (req, res) => {
  try {
    const userId = req.params.userId;
    const deletedUser = await User.findByIdAndDelete(userId);
    if (!deletedUser) {
      return res.status(404).json({ error: "Không tìm thấy comic" });
    }
    res.json({ message: "Comic đã được xóa thành công" });
  } catch (error) {
    console.error("Lỗi khi xóa comic:", error);
    res.status(500).json({ error: "Lỗi máy chủ nội bộ" });
  }
};
const getUserById = async (req, res) => {
  try {
    const userId = req.params.userId;
    const user = await User.findById(userId);
    if (!user) {
      return res.status(404).json({ error: "Người dùng không tồn tại" });
    }
    res.json({ username: user.username });
  } catch (error) {
    console.error("Lỗi khi lấy thông tin người dùng:", error);
    res.status(500).json({ error: "Lỗi máy chủ nội bộ" });
  }
};
const getComicsByFavoriteIds = async (req, res) => {
  const userId = req.params.userId;

  try {
    const user = await User.findById(userId);
    if (!user) {
      return res.status(404).json({ error: "Người dùng không tồn tại" });
    }

    const favoriteComicIds = user.favorite.map((favorite) => favorite.comicId);

    const comics = await Comics.find({ _id: { $in: favoriteComicIds } });

    // Trả về danh sách các bản ghi comic
    res.json(comics);
  } catch (error) {
    console.error("Lỗi khi lấy thông tin comic từ favorite:", error);
    res.status(500).json({ error: "Lỗi máy chủ nội bộ" });
  }
};

const deleteFavoriteComicById = async (req, res) => {
  const userId = req.params.userId;
  const comicIdToDelete = req.params.comicId;

  try {
    const updatedUser = await User.findByIdAndUpdate(
      userId,
      { $pull: { favorite: { comicId: comicIdToDelete } } },
      { new: true }
    );

    if (!updatedUser) {
      return res.status(404).json({ error: "Không tìm thấy người dùng" });
    }

    res.json({
      message: "Đã xoá comic khỏi danh sách yêu thích",
      user: updatedUser,
    });
  } catch (error) {
    console.error("Lỗi khi xoá comic khỏi danh sách yêu thích:", error);
    res.status(500).json({ error: "Lỗi máy chủ nội bộ" });
  }
};
const changePasswordController = async (req, res) => {
  const userId = req.params.userId;
  const { currentPassword, newPassword } = req.body;

  try {
    const user = await User.findById(userId);
    if (!user) {
      return res.status(404).json({ error: "Người dùng không tồn tại" });
    }

    if (user.password !== currentPassword) {
      return res
        .status(400)
        .json({ error: "Mật khẩu hiện tại không chính xác" });
    }
    user.password = newPassword;
    const updatedUser = await user.save();
    res.json({
      message: "Đổi mật khẩu thành công",
      user: updatedUser,
    });
  } catch (error) {
    console.error("Lỗi khi đổi mật khẩu:", error);
    res.status(500).json({ error: "Lỗi máy chủ nội bộ" });
  }
};

module.exports = {
  postUserLoginController,
  postUserController,
  apiGetUserController,
  deleteUserController,
  updateAvatarUserController,
  updateCoinUserController,
  getUserLoginController,
  buyComicController,
  getFavoriteComicIds,
  getUserById,
  getComicsByFavoriteIds,
  deleteFavoriteComicById,
  changePasswordController,
};
