import React, { Component } from 'react'
import { SafeAreaView } from 'react-native'
import { WebView } from 'react-native-webview'
import Turbolinks from 'react-native-turbolinks'
import Constants from './Constants'

const signInUrl = Constants.baseUrl + '/sign-in'

export default class AuthenticationView extends React.Component {

  handleAuthentication = (e) => {
    if (e.nativeEvent.url != signInUrl) {
      this.webview.stopLoading()
      Turbolinks.dismiss()

      setTimeout(() => Turbolinks.reloadSession(), 2500)
    }
  }

  render() {
    return (
      <SafeAreaView style={{flex: 1, backgroundColor: '#fff'}}>
        <WebView ref={webview => { this.webview = webview }}
                 source={{uri: signInUrl}}
                 onLoadStart={this.handleAuthentication}/>
      </SafeAreaView>
    )
  }
}
