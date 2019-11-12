import { NativeEventEmitter, NativeModules, processColor, Platform } from 'react-native'

const RNTurbolinksManager = NativeModules.RNTurbolinksManager || NativeModules.RNTurbolinksModule
const RNTurbolinksManagerEmitter = new NativeEventEmitter(RNTurbolinksManager)

const resolveAssetSource = require('react-native/Libraries/Image/resolveAssetSource')

class Turbolinks {

  static Constants = {
    ErrorCode: RNTurbolinksManager.ErrorCode,
    Action: RNTurbolinksManager.Action
  }

  static startSingleScreenApp(route, options = {}) {
    this._processRoute(route)
    this._processNavBarStyle(options.navBarStyle)
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
    this._processRoute(route)
    RNTurbolinksManager.visit(route)
  }

  static replaceWith(route) {
    this._processRoute(route)
    RNTurbolinksManager.replaceWith(route)
  }

  static renderTitle(title, subtitle = null) {
    RNTurbolinksManager.renderTitle(title, subtitle)
  }

  static renderActions(actions) {
    RNTurbolinksManager.renderActions(actions)
  }

  static renderNavBarStyle(style) {
    this._processNavBarStyle(style)
    RNTurbolinksManager.renderNavBarStyle(style)
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

  static _processNavBarStyle(style) {
    if (style && style.barTintColor) style.barTintColor = processColor(style.barTintColor)
    if (style && style.tintColor) style.tintColor = processColor(style.tintColor)
    if (style && style.titleTextColor) style.titleTextColor = processColor(style.titleTextColor)
    if (style && style.subtitleTextColor) style.subtitleTextColor = processColor(style.subtitleTextColor)
    if (style && style.menuIcon) style.menuIcon = resolveAssetSource(style.menuIcon)
  }

  static _processRoute(route) {
    if (route.titleImage) route.titleImage = resolveAssetSource(route.titleImage)
    if (route.leftButtonIcon) route.leftButtonIcon = resolveAssetSource(route.leftButtonIcon)
    if (route.rightButtonIcon) route.rightButtonIcon = resolveAssetSource(route.rightButtonIcon)
    if (route.navIcon) route.navIcon = resolveAssetSource(route.navIcon)
    if (route.actions) route.actions = resolveAssetSourceActions(route.actions)

    function resolveAssetSourceActions(actions) {
      actions.filter(act => act.icon).forEach(act => { act.icon = resolveAssetSource(act.icon) })
      return actions
    }
  }

}

module.exports = Turbolinks;
