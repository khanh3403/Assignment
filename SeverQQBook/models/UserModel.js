const mongoose = require("mongoose");
const UserSchema = new mongoose.Schema({
  username: String,
  password: String,
  email: String,
  avatar: Object,
  fullname: String,
  coin: { type: Number, default: 0 },
  favorite: [
    {
      comicId: String,
    },
  ],
});

const User = new mongoose.model("Users", UserSchema);
module.exports = User;
