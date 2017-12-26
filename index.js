import PropTypes from 'prop-types';
import React, { Component } from 'react';
import { requireNativeComponent, NativeModules } from 'react-native';

const RNTurbolinksManager = NativeModules.RNTurbolinksManager

export default class TurboLinks extends Component {

  static constants = {
    ErrorCode: RNTurbolinksManager.ErrorCode,
    Action: RNTurbolinksManager.Action
  };  

  componentDidMount() {
    RNTurbolinksManager.initialize();
  }

  visit = (route) => {
    RNTurbolinksManager.visit(route);
  }

  _onMessage = (event) => {
    if (this.props.onMessage) this.props.onMessage(event.nativeEvent.message);
  }

  _onVisit = (event) => {
    if (this.props.onVisit) this.props.onVisit(event.nativeEvent.data);
  }

  _onError = (event) => {
    if (this.props.onError) this.props.onError(event.nativeEvent.data);
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
  url: PropTypes.string.isRequired,
  onVisit: PropTypes.func.isRequired,
  onError: PropTypes.func.isRequired,
  userAgent: PropTypes.string,
  onMessage: PropTypes.func
}

TurboLinks.defaultProps = {
  userAgent: 'RNTurbolinks'
}

var RNTurboLinks = requireNativeComponent('RNTurbolinks', TurboLinks)
