const fs = require("fs");
const path = require("path");

module.exports = () => {
  function setMinSdkVersion() {
    const buildGradlePath = path.join("../../android/build.gradle");
    const buildGradleContents = fs.readFileSync(buildGradlePath, "utf8");
    const minSdkVersion = buildGradleContents.match(/minSdkVersion.+/).shift()
    const minSdkVersionNew = "minSdkVersion = 19";
    const newBuildGradleContents = buildGradleContents.replace(minSdkVersion, minSdkVersionNew)
    fs.writeFileSync(buildGradlePath, newBuildGradleContents);
  }

  function setAllowBackup() {
    const manifestPath = path.join("../../android/app/src/main/AndroidManifest.xml");
    const manifestContents = fs.readFileSync(manifestPath, "utf8");
    const androidAllowBackup = 'android:allowBackup="false"';
    const androidAllowBackupNew = 'android:allowBackup="true"';
    const newManifestContents = manifestContents.replace(androidAllowBackup, androidAllowBackupNew);
    fs.writeFileSync(manifestPath, newManifestContents);
  }

  setMinSdkVersion()
  setAllowBackup()
  return Promise.resolve();
}
