/** https://github.com/Microsoft/react-native-code-push/blob/master/scripts/postlink/run.js */

var postunlinks = [
  require("./ios/postunlink"),
  require("./android/postunlink")
];

//run them sequentially
postunlinks.reduce((p, fn) => p.then(fn), Promise.resolve()).catch((err) => {
  console.error(err.message);
});
