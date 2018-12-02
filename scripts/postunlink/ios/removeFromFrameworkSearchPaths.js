const mapFrameworkSearchPaths = require('./mapFrameworkSearchPaths');

module.exports = function removeFromFrameworkSearchPaths(project, path) {
  mapFrameworkSearchPaths(project, searchPaths =>
    searchPaths.filter(searchPath => searchPath !== path),
  );
};
