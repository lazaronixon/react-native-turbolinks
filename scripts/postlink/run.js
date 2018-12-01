/** https://github.com/Microsoft/react-native-code-push/blob/master/scripts/postlink/run.js */

var postlinks = [
  require("./ios/postlink"),
  require("./android/postlink")
];

//run them sequentially
postlinks.reduce((p, fn) => p.then(fn), Promise.resolve()).catch((err) => {
  console.error(err.message);
});
