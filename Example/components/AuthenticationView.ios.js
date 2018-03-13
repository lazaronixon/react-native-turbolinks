import React, { Component } from 'react'
import { NavigatorIOS, WebView } from 'react-native'
import Turbolinks from 'react-native-turbolinks'
import Constants from './Constants';

const signInUrl = Constants.baseUrl + '/sign-in'

export default class AuthenticationView extends Component {

  render() {
    return (
      <NavigatorIOS initialRoute={{component: MyScene, title: 'Sign in'}} style={{flex: 1}}/>
    )
  }
}

class MyScene extends Component {

  handleAuthentication = (navState) => {
    if (navState.url == signInUrl) return true
    Turbolinks.dismiss()
    setTimeout(() => Turbolinks.reloadSession(), 800)
    return false
  }

  render() {
    return (
        <WebView source={{uri: signInUrl}}
                 onShouldStartLoadWithRequest={this.handleAuthentication}/>
    )
  }
}
