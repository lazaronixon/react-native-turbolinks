import React, { Component } from 'react';
import Turbolinks from 'react-native-turbolinks';
import PubSub from 'pubsub-js';

export default class App extends Component {

  componentDidMount() {
    PubSub.subscribe('retryEvent',this.turboLinks.reload);
    this.turboLinks.visit({url: 'http://localhost:9292'});
  }

  handleVisit = (data) => {
    if (data.path == '/numbers') {
      this.turboLinks.visit({component: 'NumbersView', title: 'Numbers'})
    } else {
      this.turboLinks.visit({url: data.url, action: data.action})
    }
  }

  handleError = (data) => {
    const httpFailure = Turbolinks.constants.ErrorCode.httpFailure;
    const networkFailure = Turbolinks.constants.ErrorCode.networkFailure;
    let title = null;
    let message = null;
    switch (data.code) {
      case httpFailure: {
        switch (data.statusCode) {
          case 404:
            title = 'Page Not Found';
            message = 'There doesn’t seem to be anything here.';
            break;
          default:
            title = 'Unknown Error';
            message = 'An unknown error occurred.';
        }
        break;
      }
      case networkFailure: {
        title = 'Can’t Connect';
        message = 'TurbolinksDemo can’t connect to the server.\nDid you remember to start it?\nSee README.md for more instructions.';
        break;
      }
    }
    this.turboLinks.present({component: 'ErrorView', passProps: {title: title, message: message}})
  }

  showMessage = (message) => {
    alert(message)
  }

  render() {
    return (
      <Turbolinks ref={(tl) => this.turboLinks = tl}
                  userAgent='turbolinksDemo'
                  onVisit={this.handleVisit}
                  onError={this.handleError}
                  onMessage={this.showMessage}
                  style={{flex: 1}}/>
    )
  }
}
