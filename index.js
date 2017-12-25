import PropTypes from 'prop-types';
import React from 'react';
import { requireNativeComponent, NativeModules } from 'react-native';

const RNTurbolinksManager = NativeModules.RNTurbolinksManager

export default class TurboLinks extends React.Component {

  componentDidMount() {
    RNTurbolinksManager.initialize();
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
           />;
  }

}

TurboLinks.propTypes = {
  url: PropTypes.string.isRequired,
  userAgent: PropTypes.string,
  onMessage: PropTypes.func,
  onVisit: PropTypes.func,
};

TurboLinks.defaultProps = {
  userAgent: 'RNTurbolinks'
};

var RNTurboLinks = requireNativeComponent('RNTurbolinks', TurboLinks)
