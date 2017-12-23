import PropTypes from 'prop-types';
import React, { Component } from 'react';
import { requireNativeComponent, NativeModules } from 'react-native';

const RNTurbolinksManager = NativeModules.RNTurbolinksManager

export default class TurboLinks extends Component {

  componentDidMount() {
    RNTurbolinksManager.initialize();
  }

  _onMessage= (event) => {
    if (!this.props.onMessage) this.props.onMessage(event);
  }

  render() {
    return <RNTurboLinks onMessage={this._onMessage} {...this.props} />;
  }

}

TurboLinks.propTypes = {
  url: PropTypes.string.isRequired,
  userAgent: PropTypes.string,
  onMessage: PropTypes.func,
};

TurboLinks.defaultProps = {
  userAgent: 'RNTurbolinks'
};

var RNTurboLinks = requireNativeComponent('RNTurbolinks', TurboLinks)
