import { NativeEventEmitter, NativeModules, processColor, Platform } from 'react-native'

const RNTurbolinksManager = NativeModules.RNTurbolinksManager || NativeModules.RNTurbolinksModule
const RNTurbolinksManagerEmitter = new NativeEventEmitter(RNTurbolinksManager);

class Turbolinks {

  static Constants = {
    ErrorCode: RNTurbolinksManager.ErrorCode,
    Action: RNTurbolinksManager.Action
  }

  static reloadVisitable() {
    RNTurbolinksManager.reloadVisitable()
  }

  static reloadSession(cleanCookies = false) {
    RNTurbolinksManager.reloadSession(cleanCookies)
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

  static replaceWith(route, tabIndex = -1) {
    RNTurbolinksManager.replaceWith(route, tabIndex)
  }

  static setUserAgent(userAgent) {
    RNTurbolinksManager.setUserAgent(userAgent)
  }

  static setMessageHandler(messageHandler) {
    RNTurbolinksManager.setMessageHandler(messageHandler)
  }

  static setNavigationBarStyle(options) {
    RNTurbolinksManager.setNavigationBarStyle({
      ...options,
      barTintColor: processColor(options.barTintColor),
      tintColor: processColor(options.tintColor),
      titleTextColor: processColor(options.titleTextColor),
      subtitleTextColor: processColor(options.subtitleTextColor)
    })
  }

  static setNavigationBarHidden(isHidden = false) {
    RNTurbolinksManager.setNavigationBarHidden(isHidden)
  }

  static setLoadingView(loadingView) {
    RNTurbolinksManager.setLoadingView(loadingView)
  }

  static startSingleScreenApp(route) {
    RNTurbolinksManager.startSingleScreenApp(route)
  }

  static startTabBasedApp(routes, selectedIndex = 0) {
    RNTurbolinksManager.startTabBasedApp(routes, selectedIndex)
  }

  static setTabBarStyle(options) {
    RNTurbolinksManager.setTabBarStyle({
      ...options,
      barTintColor: processColor(options.barTintColor),
      tintColor: processColor(options.tintColor)
    })
  }

  static renderTitle(title, subtitle = null, tabIndex = -1) {
    RNTurbolinksManager.renderTitle(title, subtitle, tabIndex)
  }

  static renderActions(actions, tabIndex = -1) {
    RNTurbolinksManager.renderActions(actions, tabIndex)
  }

  static evaluateJavaScript(script, tabIndex = -1) {
    if (Platform.OS == 'ios') {
      return RNTurbolinksManager.evaluateJavaScript(script, tabIndex)
    } else {
      return RNTurbolinksManager.evaluateJavaScript(script).then((r) => JSON.parse(r))
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
