import React, { Component } from 'react'
import { NavigatorIOS, WebView } from 'react-native'
import Turbolinks from 'react-native-turbolinks'
import Constants from './Constants';

export default class AuthenticationView extends Component {

  render() {
    return (
      <NavigatorIOS initialRoute={{component: MyScene, title: 'Sign in'}} style={{flex: 1}}/>
    )
  }
}

class MyScene extends Component {

  state = { url: Constants.baseUrl + '/sign-in' }

  handleAuthenticated = () => {
    setTimeout(() => Turbolinks.reloadSession(), 1000)
    Turbolinks.dismiss()
  }

  handleNavigationStateChange = (navState) => {
    if (this.state.url != navState.url) { this.handleAuthenticated() }
    this.setState({ url: navState.url })
  }

  render() {
    return (
        <WebView source={{uri: this.state.url }}
                 onNavigationStateChange={this.handleNavigationStateChange}/>
    )
  }
}
