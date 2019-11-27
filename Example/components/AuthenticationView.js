import React, { Component } from 'react'
import { SafeAreaView } from 'react-native'
import { WebView } from 'react-native-webview'
import Turbolinks from 'react-native-turbolinks'
import { baseUrl } from './../app.json'

const signInUrl = baseUrl + '/sign-in'
const signedUrl = baseUrl + '/'
var authenticatedFlag = false

export default class AuthenticationView extends Component {

  // authenticatedFlag prevents old android devices handleAuthentication twice.
  handleAuthentication = (event) => {
    if (event.nativeEvent.url == signedUrl && authenticatedFlag == false) {
      authenticatedFlag = true

      this.webview.stopLoading()
      Turbolinks.dismiss().then(() => Turbolinks.reloadSession())
    }
  }

  render() {
    return (
      <SafeAreaView style={{flex: 1}}>
        <WebView ref={ref => (this.webview = ref)}
                 source={{uri: signInUrl}}
                 onLoadStart={this.handleAuthentication}/>
      </SafeAreaView>
    )
  }
}
