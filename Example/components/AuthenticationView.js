import React, { Component } from 'react'
import { NavigatorIOS, Platform, WebView } from 'react-native'
import Turbolinks from 'react-native-turbolinks'

export default class AuthenticationView extends Component {

  render() {
    if (Platform.OS == 'ios') {
      return ( <NavigatorIOS initialRoute={{component: MyScene, title: 'Sign in'}} style={{flex: 1}}/> );
    } else {
      return ( <MyScene style={{flex: 1}}/> );
    }
  }
}

class MyScene extends Component {

  state = { url: 'http://192.168.1.2:9292/sign-in' }

  handleAuthenticated = () => {
    Turbolinks.reloadSession()
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
