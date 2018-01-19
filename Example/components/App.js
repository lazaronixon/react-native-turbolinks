import React, { Component } from 'react'
import { Platform }from 'react-native'
import Turbolinks from 'react-native-turbolinks'
import Constants from './Constants';

export default class App extends Component {

  componentDidMount() {
    Turbolinks.addEventListener('turbolinksVisit', this.handleVisit)
    Turbolinks.addEventListener('turbolinksError', this.handleError)
    Turbolinks.addEventListener('turbolinksMessage', this.showMessage)
    Turbolinks.setMessageHandler('turbolinksDemo')
    Turbolinks.visit({url: Constants.baseUrl})
  }

  componentWillUnmount() {
    Turbolinks.removeEventListener('turbolinksVisit', this.handleVisit)
    Turbolinks.removeEventListener('turbolinksError', this.handleError)
    Turbolinks.removeEventListener('turbolinksMessage', this.showMessage)
  }

  handleVisit = (data) => {
    if (data.path == '/numbers') {
      Turbolinks.visit({component: 'NumbersView', title: 'Numbers'})
    } else {
      Turbolinks.visit({url: data.url, action: data.action})
    }
  }

  replaceWith(passProps) {
    const replace = Turbolinks.Constants.Action.replace
    if (Platform.OS == 'ios') {
      Turbolinks.replaceWith({component: 'ErrorView', passProps: passProps})
    } else {
      Turbolinks.visit({component: 'ErrorView', action: replace, passProps: passProps})
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
            Turbolinks.visit({component: 'AuthenticationView', modal: true})
            break
          case 404:
            var title = 'Page Not Found'
            var message = 'There doesn’t seem to be anything here.'
            this.replaceWith({title: title, message: message})
            break
          default:
            var title = 'Unknown Error'
            var message = 'An unknown error occurred.'
            this.replaceWith({title: title, message: message})
        }
        break
      }
      case networkFailure: {
        var title = 'Can’t Connect'
        var message = 'TurbolinksDemo can’t connect to the server.\nDid you remember to start it?\nSee README.md for more instructions.'
        this.replaceWith({title: title, message: message})
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
