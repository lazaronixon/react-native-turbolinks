const pbxproj = require("xcode");
const fs = require('fs');
const glob = require("glob");
const path = require("path")
const addFramework = require('./addFramework');
const addToFrameworkSearchPaths = require('./addToFrameworkSearchPaths');

module.exports = () => {
  const xcodeprojPath = glob.sync("ios/*.xcodeproj")[0];
  const projectPath = xcodeprojPath + "/project.pbxproj";

  function installTurbolinksIOS() {
      const frameworkPath = "../node_modules/react-native-turbolinks/ios/Turbolinks.framework";
      const frameworkSearchPath = '"$(SRCROOT)/../node_modules/react-native-turbolinks/ios/**"';
      const proj = pbxproj.project(projectPath);
      proj.parseSync();

      addFramework(proj, frameworkPath);
      addToFrameworkSearchPaths(proj, frameworkSearchPath);
      fs.writeFileSync(projectPath, proj.writeSync());
  }

  function installSwift() {
    const proj = pbxproj.project(projectPath);
    proj.parseSync();

    const placeholderPath = path.join("ios", "RNPlaceholder.swift");
    fs.writeFileSync(placeholderPath, "");

    const firstProject = proj.getFirstProject();
    const firstTarget = proj.getFirstTarget().uuid;
    proj.addSourceFile("RNPlaceholder.swift", { target: firstTarget }, firstProject);
    proj.addBuildProperty("SWIFT_VERSION", "4.2");
    fs.writeFileSync(projectPath, proj.writeSync());
  }

  installTurbolinksIOS();
  installSwift();
  return Promise.resolve();
}
