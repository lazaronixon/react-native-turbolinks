const PbxFile = require('xcode/lib/pbxFile');

function createFrameworkGroup(project) {
  if (!project.pbxGroupByName('Frameworks')) {
    project.addPbxGroup([], 'Frameworks');
  }
}

function createEmbedFrameworkBuildPhase(project) {
  const target = project.getFirstTarget().uuid
  
  if (!project.buildPhaseObject('PBXCopyFilesBuildPhase', 'Embed Frameworks', target)) {
    project.addBuildPhase([], 'PBXCopyFilesBuildPhase', 'Embed Frameworks', target, 'frameworks');
  }
}

module.exports = function addFramework(project, path) {
  createFrameworkGroup(project);
  createEmbedFrameworkBuildPhase(project);

  const file = new PbxFile(path, { customFramework: true });
  file.uuid = project.generateUuid();
  file.fileRef = project.generateUuid();
  project.addToPbxBuildFileSection(file);          // PBXBuildFile
  project.addToPbxFileReferenceSection(file);      // PBXFileReference
  project.addToFrameworksPbxGroup(file);           // PBXGroup
  project.addToPbxFrameworksBuildPhase(file);      // PBXFrameworksBuildPhase

  const embeddedFile = new PbxFile(path, { customFramework: true, embed: true });
  embeddedFile.uuid = project.generateUuid();
  embeddedFile.fileRef = file.fileRef;
  project.addToPbxBuildFileSection(embeddedFile);          // PBXBuildFile
  project.addToPbxEmbedFrameworksBuildPhase(embeddedFile); // PBXCopyFilesBuildPhase
}
