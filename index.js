import PropTypes from 'prop-types';
import React, { Component } from 'react';
import { requireNativeComponent, NativeModules } from 'react-native';

const RNTurbolinksManager = NativeModules.RNTurbolinksManager

export default class TurboLinks extends Component {

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

  render() {
    return <RNTurboLinks
             {...this.props}
             onMessage={this._onMessage}
             onVisit={this._onVisit}
           />
  }
}

TurboLinks.propTypes = {
  initialRoute: PropTypes.oneOfType([
    PropTypes.shape({ url: PropTypes.string }),
    PropTypes.shape({ component: PropTypes.string, title: PropTypes.string
    })
  ]).isRequired,
  userAgent: PropTypes.string,
  onMessage: PropTypes.func,
  onVisit: PropTypes.func
}

TurboLinks.defaultProps = {
  userAgent: 'RNTurbolinks'
}

var RNTurboLinks = requireNativeComponent('RNTurbolinks', TurboLinks)
