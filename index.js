import PropTypes from 'prop-types'
import React, { Component } from 'react'
import { NativeEventEmitter, NativeModules } from 'react-native'

const RNTurbolinksManager = NativeModules.RNTurbolinksManager || NativeModules.RNTurbolinksModule
const RNTurbolinksManagerEmitter = new NativeEventEmitter(RNTurbolinksManager);
const processColor = require('processColor');

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
      subTitleTextColor: processColor(options.subTitleTextColor)
    })
  }

  static setNavigationBarHidden(isHidden) {
    RNTurbolinksManager.setNavigationBarHidden(isHidden)
  }

  static setBackgroundColor(backgroundColor) {
    RNTurbolinksManager.setBackgroundColor(processColor(backgroundColor))
  }

  static addEventListener(eventName, callback) {
    RNTurbolinksManagerEmitter.addListener(eventName, callback)
  }

  static removeEventListener(eventName, callback) {
    RNTurbolinksManagerEmitter.removeListener(eventName, callback)
  }

}

module.exports = Turbolinks;
