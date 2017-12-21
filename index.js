import PropTypes from 'prop-types';
import React, { Component } from 'react';
import { requireNativeComponent, NativeModules } from 'react-native';

const RNTurbolinksManager = NativeModules.RNTurbolinksManager

export default class TurboLinks extends Component {
  render() {
    return <RNTurboLinks {...this.props} />;
  }

}

TurboLinks.propTypes = {
  url: PropTypes.string.isRequired
};

var RNTurboLinks = requireNativeComponent('RNTurbolinks', TurboLinks)
