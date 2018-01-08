import PropTypes from 'prop-types'
import React, { Component } from 'react'
import ReactNative, { requireNativeComponent, NativeModules, ViewPropTypes } from 'react-native'

const RNTurbolinksManager = NativeModules.RNTurbolinksManager || NativeModules.RNTurbolinksModule

export default class TurboLinks extends Component {

  static Constants = {
    ErrorCode: RNTurbolinksManager.ErrorCode,
    Action: RNTurbolinksManager.Action
  }

  reloadVisitable() {
    RNTurbolinksManager.reloadVisitable()
  }

  reloadSession() {
    RNTurbolinksManager.reloadSession()
  }

  dismiss() {
    RNTurbolinksManager.dismiss()
  }

  visit(route) {
    RNTurbolinksManager.visit(this.reactTag(), route)
  }

  replaceWith(route) {
    RNTurbolinksManager.replaceWith(route)
  }

  _onVisit = (event) => {
    if (this.props.onVisit) this.props.onVisit(event.nativeEvent.data)
  }

  _onError = (event) => {
    if (this.props.onError) this.props.onError(event.nativeEvent.data)
  }

  _onMessage = (event) => {
    if (this.props.onMessage) this.props.onMessage(event.nativeEvent.message)
  }

  reactTag() {
    return ReactNative.findNodeHandle(this)
  }

  render() {
    return <RNTurboLinks
             {...this.props}
             onVisit={this._onVisit}
             onError={this._onError}
             onMessage={this._onMessage}
           />
  }
}

TurboLinks.propTypes = {
  onVisit: PropTypes.func.isRequired,
  onError: PropTypes.func.isRequired,
  onMessage: PropTypes.func,
  userAgent: PropTypes.string,
  ...ViewPropTypes
}

TurboLinks.defaultProps = {
  userAgent: 'RNTurbolinks'
}

var RNTurboLinks = requireNativeComponent('RNTurbolinks', TurboLinks)
