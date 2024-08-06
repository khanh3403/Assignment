const mongoose = require("mongoose");
const ComicSchema = new mongoose.Schema({
  name: String,
  description: String,
  price: String,
  author: String,
  type: String,
  publishYear: String,
  coverImage: Object,
  images: Object,
});

const Comics = new mongoose.model("Comic", ComicSchema);
module.exports = Comics;
