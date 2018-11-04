const mapFrameworkSearchPaths = require('./mapFrameworkSearchPaths');

module.exports = function addToFrameworkSearchPaths(project, path) {
  mapFrameworkSearchPaths(project, searchPaths => searchPaths.concat(path));
};
