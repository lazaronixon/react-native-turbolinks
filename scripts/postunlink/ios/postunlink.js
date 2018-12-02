const pbxproj = require("xcode");
const fs = require('fs');
const glob = require("glob");
const removeFramework = require('./removeFramework');
const removeResource = require('./removeResource');
const removeFromFrameworkSearchPaths = require('./removeFromFrameworkSearchPaths');

module.exports = () => {
  const xcodeprojPath = glob.sync("ios/*.xcodeproj")[0];
  const projectPath = xcodeprojPath + "/project.pbxproj";

  function removeTurbolinksIOS() {
    const project = pbxproj.project(projectPath);
    project.parseSync();

    removeFramework(project, "../node_modules/react-native-turbolinks/ios/Turbolinks.framework");
    removeFromFrameworkSearchPaths(project, '"$(SRCROOT)/../node_modules/react-native-turbolinks/ios/**"');
    fs.writeFileSync(projectPath, project.writeSync());
  }

  function removeTurbolinksResources() {
    const project = pbxproj.project(projectPath);
    project.parseSync();

    removeResource(project, "../node_modules/react-native-turbolinks/ios/RNTurbolinksImages.xcassets");
    fs.writeFileSync(projectPath, project.writeSync());
  }

  removeTurbolinksIOS();
  removeTurbolinksResources();
  return Promise.resolve();
}
