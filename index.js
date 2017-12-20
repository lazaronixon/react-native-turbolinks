import PropTypes from 'prop-types';
import React, { Component } from 'react';
import { requireNativeComponent, NativeModules } from 'react-native';

const RNTurbolinksManager = NativeModules.RNTurbolinksManager

export default class TurboLinks extends Component {
  render() {
    return <RNTurboLinks {...this.props} />;
  }

  visit(url) {
    RNTurbolinksManager.visit(url)
  }

}

TurboLinks.propTypes = {
};

var RNTurboLinks = requireNativeComponent('RNTurbolinks', TurboLinks)
