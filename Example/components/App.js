import React, { Component } from 'react'
import Turbolinks from 'react-native-turbolinks'
import PubSub from 'pubsub-js'

export default class App extends Component {

  componentDidMount() {
    PubSub.subscribe('retryEvent', this.handleRetry)
    PubSub.subscribe('authenticatedEvent', this.handleAuthenticated)
    this.turboLinks.visit({url: 'http://115.0.11.61:9292'})
  }

  handleVisit = (data) => {
    if (data.path == '/numbers') {
      this.turboLinks.visit({component: 'NumbersView', title: 'Numbers'})
    } else {
      this.turboLinks.visit({url: data.url, action: data.action})
    }
  }

  handleError = (data) => {
    const { httpFailure, networkFailure }= Turbolinks.Constants.ErrorCode
    const { replace } = Turbolinks.Constants.Action
    switch (data.code) {
      case httpFailure: {
        switch (data.statusCode) {
          case 401:
            this.turboLinks.visit({component: 'AuthenticationView', action: replace})
            break
          case 404:
            var title = 'Page Not Found'
            var message = 'There doesn’t seem to be anything here.'
            this.turboLinks.replaceWith({component: 'ErrorView', passProps: {title: title, message: message}})
            break
          default:
            var title = 'Unknown Error'
            var message = 'An unknown error occurred.'
            this.turboLinks.replaceWith({component: 'ErrorView', passProps: {title: title, message: message}})
        }
        break
      }
      case networkFailure: {
        var title = 'Can’t Connect'
        var message = 'TurbolinksDemo can’t connect to the server.\nDid you remember to start it?\nSee README.md for more instructions.'
        this.turboLinks.replaceWith({component: 'ErrorView', passProps: {title: title, message: message}})
        break
      }
    }
  }

  handleRetry = () => {
    this.turboLinks.reloadVisitable()
  }

  handleAuthenticated = () => {
    this.turboLinks.reloadSession()
    this.turboLinks.dismiss()
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
