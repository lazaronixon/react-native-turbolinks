import { NativeEventEmitter, NativeModules, processColor, Platform } from 'react-native'

const RNTurbolinksManager = NativeModules.RNTurbolinksManager || NativeModules.RNTurbolinksModule
const RNTurbolinksManagerEmitter = new NativeEventEmitter(RNTurbolinksManager);

class Turbolinks {

  static Constants = {
    ErrorCode: RNTurbolinksManager.ErrorCode,
    Action: RNTurbolinksManager.Action
  }

  static startSingleScreenApp(route, options = {}) {
    this._processAppOptions(options)
    RNTurbolinksManager.startSingleScreenApp(route, options)
  }

  static reloadVisitable() {
    RNTurbolinksManager.reloadVisitable()
  }

  static reloadSession() {
    RNTurbolinksManager.reloadSession()
  }

  static dismiss(animated = true) {
    RNTurbolinksManager.dismiss(animated)
  }

  static popToRoot(animated = true) {
    RNTurbolinksManager.popToRoot(animated)
  }

  static back(animated = true) {
    RNTurbolinksManager.back(animated)
  }

  static visit(route) {
    RNTurbolinksManager.visit(route)
  }

  static replaceWith(route) {
    RNTurbolinksManager.replaceWith(route)
  }

  static renderTitle(title, subtitle = null) {
    RNTurbolinksManager.renderTitle(title, subtitle)
  }

  static renderActions(actions) {
    RNTurbolinksManager.renderActions(actions)
  }

  static injectJavaScript(script) {
    return RNTurbolinksManager.injectJavaScript(script)
  }

  static addEventListener(eventName, callback) {
    RNTurbolinksManagerEmitter.addListener(eventName, callback)
  }

  static removeEventListener(eventName, callback) {
    RNTurbolinksManagerEmitter.removeListener(eventName, callback)
  }

  static _processAppOptions(options) {
    if (options.navBarStyle) {
      options.navBarStyle = {
        ...options.navBarStyle,
        barTintColor: processColor(options.navBarStyle.barTintColor),
        tintColor: processColor(options.navBarStyle.tintColor),
        titleTextColor: processColor(options.navBarStyle.titleTextColor),
        subtitleTextColor: processColor(options.navBarStyle.subtitleTextColor)
      }
    }
  }

}

module.exports = Turbolinks;
