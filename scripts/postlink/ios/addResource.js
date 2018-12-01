function createResourcesGroup(project) {
  if (!project.pbxGroupByName('Resources')) {
    project.addPbxGroup([], 'Resources');
  }
}

module.exports = function addResource(project, path) {
  const target = project.getFirstTarget().uuid;

  createResourcesGroup(project);
  project.addResourceFile(path, { target: target });
}
