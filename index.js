import PropTypes from 'prop-types'
import React, { Component } from 'react'
import { NativeEventEmitter, NativeModules, processColor } from 'react-native'

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

  static reloadSession() {
    RNTurbolinksManager.reloadSession()
  }

  static dismiss() {
    RNTurbolinksManager.dismiss()
  }

  static back() {
    RNTurbolinksManager.back()
  }

  static visit(route) {
    RNTurbolinksManager.visit(route)
  }

  static replaceWith(route) {
    RNTurbolinksManager.replaceWith(route)
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

  static setNavigationBarHidden(isHidden) {
    RNTurbolinksManager.setNavigationBarHidden(isHidden)
  }

  static setLoadingStyle(options) {
    RNTurbolinksManager.setLoadingStyle({
      ...options,
      color: processColor(options.color),
      backgroundColor: processColor(options.backgroundColor)
    })
  }

  static renderTitle(title, subtitle) {
    RNTurbolinksManager.renderTitle(title, subtitle)
  }

  static renderActions(actions) {
    RNTurbolinksManager.renderActions(actions)
  }

  static addEventListener(eventName, callback) {
    RNTurbolinksManagerEmitter.addListener(eventName, callback)
  }

  static removeEventListener(eventName, callback) {
    RNTurbolinksManagerEmitter.removeListener(eventName, callback)
  }

}

module.exports = Turbolinks;
