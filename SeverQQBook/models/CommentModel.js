const mongoose = require("mongoose");
const CommentSchema = new mongoose.Schema({
  comicId: mongoose.Schema.Types.ObjectId,
  userId: mongoose.Schema.Types.ObjectId,
  username: String,
  content: String,
  timestamp: String,
});

const Coment = new mongoose.model("Comments", CommentSchema);
module.exports = Coment;
