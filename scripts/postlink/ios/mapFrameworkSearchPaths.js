const defaultFrameworkPaths = ['"$(inherited)"'];

module.exports = function frameworkSearchPathIter(project, func) {
  const config = project.pbxXCBuildConfigurationSection();

  Object.keys(config)
    .filter(ref => ref.indexOf('_comment') === -1)
    .forEach(ref => {
      const buildSettings = config[ref].buildSettings;
      const shouldVisitBuildSettings =
        (Array.isArray(buildSettings.OTHER_LDFLAGS)
          ? buildSettings.OTHER_LDFLAGS
          : []
        ).indexOf('"-lc++"') >= 0;

      if (shouldVisitBuildSettings) {
        const searchPaths = buildSettings.FRAMEWORK_SEARCH_PATHS
          ? [].concat(buildSettings.FRAMEWORK_SEARCH_PATHS)
          : defaultHeaderPaths;

        buildSettings.FRAMEWORK_SEARCH_PATHS = func(searchPaths);
      }
    });
};
