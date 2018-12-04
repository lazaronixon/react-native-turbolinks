const fs = require('fs')
const getDevPaths = require('get-dev-paths')

const projectRoot = __dirname

module.exports = {
  watchFolders: Array.from(new Set(
    getDevPaths(projectRoot).map($ => fs.realpathSync($))
  ))
}
