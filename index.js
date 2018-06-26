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

  static startAppInView(viewTag, route, options = {}) {
    this._processAppOptions(options)
    RNTurbolinksManager.startAppInView(viewTag, route, options)
  }

  static startTabBasedApp(routes, options = {}, selectedIndex = 0) {
    this._processAppOptions(options)
    RNTurbolinksManager.startTabBasedApp(routes, options, selectedIndex)
  }

  static reloadVisitable() {
    RNTurbolinksManager.reloadVisitable()
  }

  static reloadSession() {
    RNTurbolinksManager.reloadSession()
  }

  static dismiss() {
    RNTurbolinksManager.dismiss()
  }

  static popToRoot() {
    RNTurbolinksManager.popToRoot()
  }

  static back() {
    RNTurbolinksManager.back()
  }

  static visit(route) {
    RNTurbolinksManager.visit(route)
  }

  static replaceWith(route, tabIndex = 0) {
    RNTurbolinksManager.replaceWith(route, tabIndex)
  }

  static renderTitle(title, subtitle = null, tabIndex = 0) {
    RNTurbolinksManager.renderTitle(title, subtitle, tabIndex)
  }

  static renderActions(actions, tabIndex = 0) {
    RNTurbolinksManager.renderActions(actions, tabIndex)
  }

  static evaluateJavaScript(script, tabIndex = 0) {
    if (Platform.OS == 'ios') {
      return RNTurbolinksManager.evaluateJavaScript(script, tabIndex)
    } else {
      return RNTurbolinksManager.evaluateJavaScript(script).then((r) => JSON.parse(r))
    }
  }

  static notifyTabItem(tabIndex, enabled) {
    RNTurbolinksManager.notifyTabItem(tabIndex, enabled)
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
    if (options.tabBarStyle) {
      options.tabBarStyle = {
        ...options.tabBarStyle,
        barTintColor: processColor(options.tabBarStyle.barTintColor),
        tintColor: processColor(options.tabBarStyle.tintColor)
      }
    }
  }

  static addEventListener(eventName, callback) {
    RNTurbolinksManagerEmitter.addListener(eventName, callback)
  }

  static removeEventListener(eventName, callback) {
    RNTurbolinksManagerEmitter.removeListener(eventName, callback)
  }

}

module.exports = Turbolinks;
