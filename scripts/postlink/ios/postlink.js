/** https://github.com/transistorsoft/react-native-background-geolocation/blob/master/scripts/postlink.js */

const pbxproj = require("xcode");
const fs = require('fs');
const glob = require("glob");

module.exports = () => {
  const xcodeprojPath = glob.sync("ios/*.xcodeproj")[0];
  const projectPath = xcodeprojPath + "/project.pbxproj";

  function installSwift() {
    const project = pbxproj.project(projectPath);
    project.parseSync();

    const firstTarget = project.getFirstTarget().uuid;
    const firstProject = project.getFirstProject();

    fs.writeFileSync("ios/RNPlaceholder.swift", "");
    project.addSourceFile("RNPlaceholder.swift", { target: firstTarget }, firstProject);
    project.addBuildProperty("SWIFT_VERSION", "5.0");
    fs.writeFileSync(projectPath, project.writeSync());
  }

  installSwift();
  return Promise.resolve();
}
