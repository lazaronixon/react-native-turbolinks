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
  
  static reset() {
    RNTurbolinksManager.reset()
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

  static setTabBar(options) {
    RNTurbolinksManager.setTabBar(options)
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

  static addEventListener(eventName, callback) {
    RNTurbolinksManagerEmitter.addListener(eventName, callback)
  }

  static removeEventListener(eventName, callback) {
    RNTurbolinksManagerEmitter.removeListener(eventName, callback)
  }

}

module.exports = Turbolinks;
