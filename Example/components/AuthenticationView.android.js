import React, { Component } from 'react'
import { StyleSheet, ToolbarAndroid, View } from 'react-native'
import { WebView } from 'react-native-webview'
import Turbolinks from 'react-native-turbolinks'
import Constants from './Constants'

const signInUrl = Constants.baseUrl + '/sign-in'

export default class AuthenticationView extends Component {

  handleAuthentication = (e) => {
    if (e.nativeEvent.url != signInUrl) {
      this.webview.stopLoading()
      Turbolinks.dismiss()
    }
  }

  render() {
    return (
      <View style={{flex: 1}}>
        <ToolbarAndroid title="Sign in" style={styles.toolBar}/>
        <WebView ref={webview => { this.webview = webview }}
                 source={{uri: signInUrl}}
                 onLoadStart={this.handleAuthentication}/>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  toolBar: {
    height: 56,
    elevation: 4,
    backgroundColor: '#f5f5f5'
  }
})
