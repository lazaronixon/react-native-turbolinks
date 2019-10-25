module.exports = {
  dependency: {
    platforms: {
      ios: {},
      android: {},
    },
    hooks: {
      "postlink": "node node_modules/react-native-turbolinks/scripts/postlink/run",
      "postunlink": "node node_modules/react-native-turbolinks/scripts/postunlink/run"
    },
  },
};
