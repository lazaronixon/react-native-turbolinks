import PropTypes from 'prop-types';
import React, { Component } from 'react';
import { requireNativeComponent } from 'react-native';

const RNTurbolinksManager = NativeModules.RNTurbolinksManager

export default class TurboLinks extends Component {
  render() {
    return <RNTurboLinks {...this.props} />;
  }

  visit() {
    RNTurbolinksManager.visit()
  }

}

TurboLinks.propTypes = {
};

var RNTurboLinks = requireNativeComponent('RNTurbolinks', TurboLinks)
