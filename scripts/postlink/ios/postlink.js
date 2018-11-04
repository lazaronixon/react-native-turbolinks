const pbxproj = require("xcode");
const fs = require('fs');
const glob = require("glob");
const addFramework = require('./addFramework');
const addToFrameworkSearchPaths = require('./addToFrameworkSearchPaths');

module.exports = () => {
  var xcodeprojPath = glob.sync("ios/*.xcodeproj")[0];

  function installTurbolinksIOS() {
      const frameworkPath = "../node_modules/react-native-turbolinks/ios/Turbolinks.framework";
      const frameworkSearchPath = '"$(SRCROOT)/../node_modules/react-native-turbolinks/ios/**"';

      const projectPath = xcodeprojPath + "/project.pbxproj";
      const proj = pbxproj.project(projectPath);
      proj.parseSync();

      addFramework(proj, frameworkPath);
      addToFrameworkSearchPaths(proj, frameworkSearchPath)
      fs.writeFileSync(projectPath, proj.writeSync());
  }

  installTurbolinksIOS();
  return Promise.resolve();
}
