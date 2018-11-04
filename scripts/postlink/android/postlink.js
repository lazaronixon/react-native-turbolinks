var fs = require("fs");
var path = require("path");

module.exports = () => {
  var buildGradlePath = path.join("android", "build.gradle");
  var manifestPath = path.join("android", "app", "src", "main", "AndroidManifest.xml");

  function setMinSdkVersion() {
    var buildGradleContents = fs.readFileSync(buildGradlePath, "utf8");
    var minSdkVersion = buildGradleContents.match(/minSdkVersion.+/)[0]
    var minSdkVersionNew = `minSdkVersion = 19`;
    buildGradleContents = buildGradleContents.replace(minSdkVersion, minSdkVersionNew)
    fs.writeFileSync(buildGradlePath, buildGradleContents);
  }

  function setAllowBackup() {
    var manifestContents = fs.readFileSync(manifestPath, "utf8");
    var androidAllowBackup = `android:allowBackup="false"`;
    var androidAllowBackupNew = `android:allowBackup="true"`;
    manifestContents = manifestContents.replace(androidAllowBackup, androidAllowBackupNew)
    fs.writeFileSync(manifestPath, manifestContents);
  }

  setMinSdkVersion()
  setAllowBackup()

  return Promise.resolve();
}
