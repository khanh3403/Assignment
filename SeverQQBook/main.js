const express = require("express");
const mongoose = require("mongoose");
const http = require("http");
const { Server } = require("socket.io");
const bodyParser = require("body-parser");
const path = require("path");

const port = 3000;
const app = express();
const server = http.createServer(app);
const io = new Server(server);

module.exports = { io, server };

app.use(express.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
app.use("/images", express.static(path.join(__dirname, "images")));
app.use("/pdf", express.static(path.join(__dirname, "pdf")));
app.set("view engine", "ejs");

const routerWeb = require("./routers/router_web");
const routerApi = require("./routers/router_api");
app.use(routerWeb);
app.use(routerApi);

mongoose
  .connect("mongodb://localhost:27017/Comic", {
    useNewUrlParser: true,
    useUnifiedTopology: true,
  })
  .then(() => {
    console.log("Connected to MongoDB");
  })
  .catch((error) => {
    console.error("Error connecting to MongoDB:", error);
  });

io.on("connection", (socket) => {
  console.log("A user connected");

  socket.on("user_login", (user_name) => {
    console.log(user_name + " logged in");
  });

  socket.on("new_comic_added", (data) => {
    io.emit("new_comic_added", data);
  });

  socket.on("add comment", (data) => {
    console.log("Dữ liệu: " + data);
    io.emit("add comment", data);
  });

  socket.on("send_message", (message) => {
    console.log("Received message: " + message);
    io.emit("receiver_message", { data: message });
  });

  socket.on("disconnect", () => {
    console.log("User disconnected");
  });
});

server.listen(port, () => {
  console.log(`Server is running on port ${port}`);
});
