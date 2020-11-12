const fs = require("fs");
const glob = require("glob");
const pbxproj = require("xcode");

module.exports = () => {
  const xcodeprojPath = glob.sync("../../ios/*.xcodeproj")[0];
  const projectPath = xcodeprojPath + "/project.pbxproj";
  const project = pbxproj.project(projectPath);
  project.parseSync();

  function installSwift() {
    fs.writeFileSync("../../ios/RNPlaceholder.swift", "");
    fs.writeFileSync("../../ios/RNPlaceholderTests.swift", "");

    const name = project.getFirstTarget().firstTarget.name
    const mainGroup = project.findPBXGroupKey({ name: name })

    const mainTarget = project.findTargetKey(name)
    project.addSourceFile("RNPlaceholder.swift", { target:  mainTarget }, mainGroup);

    const testTarget = project.findTargetKey(name + "Tests")
    project.addSourceFile("RNPlaceholderTests.swift", { target:  testTarget }, mainGroup);

    project.addBuildProperty("SWIFT_VERSION", "5.0");
    fs.writeFileSync(projectPath, project.writeSync());
  }

  installSwift();
  return Promise.resolve();
}
