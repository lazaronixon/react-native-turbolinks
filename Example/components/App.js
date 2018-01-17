import React, { Component } from 'react'
import Turbolinks from 'react-native-turbolinks'

export default class App extends Component {

  componentDidMount() {
    Turbolinks.addListener('turbolinksVisit', this.handleVisit)
    Turbolinks.addListener('turbolinksError', this.handleError)
    Turbolinks.addListener('turbolinksMessage', this.showMessage)
    Turbolinks.setMessageHandler('turbolinksDemo')
    Turbolinks.visit({url: 'http://localhost:9292'})
  }

  componentWillUnmount() {
    Turbolinks.removeEventListener('turbolinksVisit', this.handleVisit);
    Turbolinks.removeEventListener('turbolinksMessage', this.showMessage);
  }

  handleVisit = (data) => {
    if (data.path == '/numbers') {
      Turbolinks.visit({component: 'NumbersView', title: 'Numbers'})
    } else {
      Turbolinks.visit({url: data.url, action: data.action})
    }
  }

  handleError = (data) => {
    const httpFailure = Turbolinks.Constants.ErrorCode.httpFailure
    const networkFailure = Turbolinks.Constants.ErrorCode.networkFailure
    const replace = Turbolinks.Constants.Action.replace
    switch (data.code) {
      case httpFailure: {
        switch (data.statusCode) {
          case 401:
            Turbolinks.visit({component: 'AuthenticationView', action: replace})
            break
          case 404:
            var title = 'Page Not Found'
            var message = 'There doesn’t seem to be anything here.'
            Turbolinks.replaceWith({component: 'ErrorView', passProps: {title: title, message: message}})
            break
          default:
            var title = 'Unknown Error'
            var message = 'An unknown error occurred.'
            Turbolinks.replaceWith({component: 'ErrorView', passProps: {title: title, message: message}})
        }
        break
      }
      case networkFailure: {
        var title = 'Can’t Connect'
        var message = 'TurbolinksDemo can’t connect to the server.\nDid you remember to start it?\nSee README.md for more instructions.'
        Turbolinks.replaceWith({component: 'ErrorView', passProps: {title: title, message: message}})
        break
      }
    }
  }

  showMessage = (message) => {
    alert(message)
  }

  render() {
    return null
  }
}
