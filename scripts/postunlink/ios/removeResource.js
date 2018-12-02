module.exports = function removeResource(project, path) {
  const target = project.getFirstTarget().uuid;
  project.removeResourceFile(path, { target: target });
}
