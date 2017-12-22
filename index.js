import PropTypes from 'prop-types';
import React, { Component } from 'react';
import { requireNativeComponent, NativeModules } from 'react-native';

const RNTurbolinksManager = NativeModules.RNTurbolinksManager

export default class TurboLinks extends Component {

  componentDidMount() {
    RNTurbolinksManager.initialize();
  }

  render() {
    return <RNTurboLinks {...this.props} />;
  }

}

TurboLinks.propTypes = {
  url: PropTypes.string.isRequired,
  userAgent: PropTypes.string
};

TurboLinks.defaultProps = {
  userAgent: 'Turbolinks Mobile'
};

var RNTurboLinks = requireNativeComponent('RNTurbolinks', TurboLinks)
