var pbxproj = require("xcode");
var fs = require('fs');
var glob = require("glob");
var addToFrameworkSearchPaths = require('./addToFrameworkSearchPaths');

module.exports = () => {
  var xcodeprojPath = glob.sync("ios/*.xcodeproj")[0];

  function installTurbolinksIOS() {
      var frameworkSearchPath = '"$(SRCROOT)/../node_modules/react-native-turbolinks/ios/**"';
      var frameworkPath = "../node_modules/react-native-turbolinks/ios/Turbolinks.framework";

      var projectPath = xcodeprojPath + "/project.pbxproj";
      var proj = pbxproj.project(projectPath);
      proj.parseSync();
      proj.addFramework(frameworkPath, { customFramework: true, embed: true });
      addToFrameworkSearchPaths(proj, frameworkSearchPath)
      fs.writeFileSync(projectPath, proj.writeSync())
  }

  installTurbolinksIOS();
  return Promise.resolve();
}
