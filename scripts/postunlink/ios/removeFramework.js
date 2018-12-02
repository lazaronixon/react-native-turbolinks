const PbxFile = require('xcode/lib/pbxFile');

module.exports = function removeFramework(project, path) {
  const file = new PbxFile(path, { customFramework: true });

  project.removeFromPbxBuildFileSection(file);          // PBXBuildFile
  project.removeFromPbxFileReferenceSection(file);      // PBXFileReference
  project.removeFromFrameworksPbxGroup(file);           // PBXGroup
  project.removeFromPbxFrameworksBuildPhase(file);      // PBXFrameworksBuildPhase

  const embeddedFile = new PbxFile(path, { customFramework: true, embed: true });
  file.embeddedFile = project.getFirstTarget().uuid;

  project.removeFromPbxBuildFileSection(embeddedFile);          // PBXBuildFile
  project.removeFromPbxEmbedFrameworksBuildPhase(embeddedFile); // PBXCopyFilesBuildPhase
}
