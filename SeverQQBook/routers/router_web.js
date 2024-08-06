const express = require("express");
const router = express.Router();

router.get("/login", (req, res) => {
  res.render("../views/LoginScreen.ejs");
});
router.get("/home", (req, res) => {
  res.render("../views/HomeScreen.ejs");
});

router.get("/comic", (req, res) => {
  res.render("../views/ListComicScreen.ejs");
});

router.get("/comic/insert", (req, res) => {
  res.render("../views/InsertComicScreen.ejs");
});
router.get("/user", (req, res) => {
  res.render("../views/ListUserScreen.ejs");
});
router.get("/user/register", (req, res) => {
  res.render("../views/RegisterScreen.ejs");
});

router.get("/comments/:comicId", (req, res) => {
  res.render("../views/CommentScreen.ejs", { comicId: req.params.comicId });
});

module.exports = router;
