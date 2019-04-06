/** https://github.com/transistorsoft/react-native-background-geolocation/blob/master/scripts/postlink.js */

const pbxproj = require("xcode");
const fs = require('fs');
const glob = require("glob");
const addFramework = require('./addFramework');
const addResource = require('./addResource');
const addToFrameworkSearchPaths = require('./addToFrameworkSearchPaths');

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

  function installTurbolinksIOS() {
      const project = pbxproj.project(projectPath);
      project.parseSync();

      const frameworkPath = "../node_modules/react-native-turbolinks/ios/Turbolinks.framework";
      const frameworkSearchPath = '"$(SRCROOT)/../node_modules/react-native-turbolinks/ios/**"';
      if (!project.hasFile(frameworkPath)) {
        addFramework(project, frameworkPath);
        addToFrameworkSearchPaths(project, frameworkSearchPath);
        fs.writeFileSync(projectPath, project.writeSync());
      }
  }

  function installTurbolinksResources() {
    const project = pbxproj.project(projectPath);
    project.parseSync();

    addResource(project, "../node_modules/react-native-turbolinks/ios/RNTurbolinksImages.xcassets");
    fs.writeFileSync(projectPath, project.writeSync());
  }

  installSwift();
  installTurbolinksIOS();
  installTurbolinksResources();
  return Promise.resolve();
}
