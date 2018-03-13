import React, { Component } from 'react'
import { ToolbarAndroid, WebView, View } from 'react-native'
import Turbolinks from 'react-native-turbolinks'
import Constants from './Constants';

export default class AuthenticationView extends Component {

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
      <View style={{flex: 1}}>
        <ToolbarAndroid title="Sign in" style={{height: 56, elevation: 4, backgroundColor: '#f5f5f5'}}/>
        <WebView source={{uri: this.state.url }} onNavigationStateChange={this.handleNavigationStateChange}/>
      </View>
    )
  }
}
