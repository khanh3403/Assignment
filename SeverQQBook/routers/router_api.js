const express = require("express");
const routerapi = express.Router();

const {
  postComicController,
  deleteComicController,
  apiGetComicController,
  updateComicController,
  getComicByIdController,
  searchComicByName
} = require("../controller/controller_comic");
const {
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
  changePasswordController
} = require("../controller/controller_user");
const {
  apiGetIdCommentController,
  postCommentController,
  putCommentController,
  deleteCommentController,
} = require("../controller/controller_comment");

// Comic
routerapi.post("/comic/post", postComicController);
routerapi.delete("/comic/delete/:comicId", deleteComicController);
routerapi.get("/api/comics", apiGetComicController);
routerapi.get("/comics/search/:name", searchComicByName);
routerapi.get("/api/comics/:comicId", getComicByIdController);
routerapi.put("/comic/update/:comicId", updateComicController);

//User
routerapi.post("/login", postUserLoginController);
routerapi.post("/user/post", postUserController);
routerapi.get("/api/username/:username", getUserLoginController);
routerapi.get("/api/favorite/comicId/:userId", getFavoriteComicIds);
routerapi.put("/user/buyComic/:userId", buyComicController);
routerapi.get("/users", apiGetUserController);
routerapi.get("/user/getname/:userId", getUserById);
routerapi.delete("/user/delete/:userId", deleteUserController);
routerapi.put("/user/update/:userId", updateAvatarUserController);
routerapi.put("/user/addcoin/:userId", updateCoinUserController);
routerapi.get("/user/favorite/:userId", getComicsByFavoriteIds);
routerapi.delete(
  "/user/delete/favorite/:userId/:comicId",
  deleteFavoriteComicById
);
routerapi.put("/user/update/password/:userId", changePasswordController);

//Comment
routerapi.get("/api/comments/:comicId", apiGetIdCommentController);
routerapi.post("/comments", postCommentController);
routerapi.put("/comments/update/:commentId", putCommentController);
routerapi.delete("/comments/delete/:commentId", deleteCommentController);

module.exports = routerapi;
