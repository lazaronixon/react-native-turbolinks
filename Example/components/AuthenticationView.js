import React, { Component } from 'react'
import { SafeAreaView } from 'react-native'
import { WebView } from 'react-native-webview'
import Turbolinks from 'react-native-turbolinks'
import { baseUrl } from './../app.json'

const signInUrl = baseUrl + '/sign-in'

export default class AuthenticationView extends Component {

  handleAuthentication = (event) => {
    if (event.nativeEvent.url != signInUrl) {
      this.webview.stopLoading()

      Turbolinks.dismiss()
      setTimeout(() => Turbolinks.reloadSession(), 1000)
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
