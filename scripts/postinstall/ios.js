/** https://github.com/transistorsoft/react-native-background-geolocation/blob/master/scripts/postlink.js */

const fs = require('fs');
const glob = require("glob");
const pbxproj = require("xcode");

module.exports = () => {
  const xcodeprojPath = glob.sync("../../ios/*.xcodeproj")[0];
  const projectPath = xcodeprojPath + "/project.pbxproj";
  const project = pbxproj.project(projectPath);
  project.parseSync();

  function installSwift() {
    fs.writeFileSync("../../ios/RNPlaceholder.swift", "");

    project.addSourceFile("RNPlaceholder.swift", { target: project.getFirstTarget().uuid }, project.getFirstProject());
    project.addBuildProperty("SWIFT_VERSION", "5.0");
    fs.writeFileSync(projectPath, project.writeSync());
  }

  installSwift();
  return Promise.resolve();
}
