import PropTypes from 'prop-types';
import React, { Component } from 'react';
import { requireNativeComponent } from 'react-native';

class TurboLinks extends Component {
  render() {
    return <RNTurboLinks {...this.props} />;
  }
}

TurboLinks.propTypes = {
};

var RNTurboLinks = requireNativeComponent('RNTurbolinks', TurboLinks)

module.exports = TurboLinks;
