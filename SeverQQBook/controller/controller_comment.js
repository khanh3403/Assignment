const Comment = require("../models/CommentModel");

const { format } = require("date-fns");
var socket = require("../main");
const Comics = require("../models/ComicModel");

const apiGetIdCommentController = async (req, res) => {
  try {
    const comicId = req.params.comicId;
    const comments = await Comment.find({ comicId });
    res.json(comments);
  } catch (error) {
    console.error("Lỗi khi lấy comments:", error);
    res.status(500).json({ error: "Lỗi máy chủ nội bộ" });
  }
};
const postCommentController = async (req, res) => {
  try {
    const { comicId, userId, username, content } = req.body;
    const comic = await Comics.findById(comicId);
    const name = comic.name;
    const timestamp = format(new Date(), "dd/MM/yyyy - H:m:s"); // Format the current date and time
    const newComment = new Comment({
      comicId,
      userId,
      username,
      content,
      timestamp,
    });

    const savedComment = await newComment.save();
    res.status(201).json(savedComment);
    socket.io.emit(
      "add comment",
      username + " vừa bình luận vào truyện " + name
    );
  } catch (error) {
    console.error("Lỗi khi tạo comment:", error);
    res.status(500).json({ error: "Lỗi máy chủ nội bộ" });
  }
};
const putCommentController = async (req, res) => {
  try {
    const commentId = req.params.commentId;
    const updatedComment = await Comment.findByIdAndUpdate(
      commentId,
      { content: req.body.content },
      { new: true }
    );
    if (!updatedComment) {
      return res.status(404).json({ error: "Không tìm thấy comment" });
    }
    res.json(updatedComment);
  } catch (error) {
    console.error("Lỗi khi cập nhật comment:", error);
    res.status(500).json({ error: "Lỗi máy chủ nội bộ" });
  }
};
const deleteCommentController = async (req, res) => {
  try {
    const commentId = req.params.commentId;
    const deletedComment = await Comment.findByIdAndDelete(commentId);
    if (!deletedComment) {
      return res.status(404).json({ error: "Không tìm thấy comment" });
    }
    res.json({ message: "Comment đã được xóa thành công" });
  } catch (error) {
    console.error("Lỗi khi xóa comment:", error);
    res.status(500).json({ error: "Lỗi máy chủ nội bộ" });
  }
};

module.exports = {
  apiGetIdCommentController,
  postCommentController,
  putCommentController,
  deleteCommentController,
};
