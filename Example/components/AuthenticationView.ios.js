import React, { Component } from 'react'
import { StyleSheet, SafeAreaView, View } from 'react-native'
import { WebView } from 'react-native-webview'
import NavigationBar from 'react-native-navbar'
import Turbolinks from 'react-native-turbolinks'
import Constants from './Constants'

const signInUrl = Constants.baseUrl + '/sign-in'

export default class AuthenticationView extends Component {

  handleAuthentication = (e) => {
    if (e.nativeEvent.url != signInUrl) {
      this.webview.stopLoading()

      setTimeout(() => Turbolinks.reloadSession(), 2000)
      Turbolinks.dismiss()
    }
  }

  render() {
    return (
      <SafeAreaView style={{flex: 1, backgroundColor: '#F9F9F9'}}>
        <NavigationBar title={{title: 'Sign in', style: styles.navBarTitle}} statusBar={{hidden: true}} style={styles.navBar} />
        <WebView ref={webview => { this.webview = webview }}
                 source={{uri: signInUrl}}
                 onLoadStart={this.handleAuthentication}/>
      </SafeAreaView>
    )
  }
}

const styles = StyleSheet.create({
  navBar: {
    height: 44,
    borderBottomWidth: StyleSheet.hairlineWidth,
    borderBottomColor: '#A7A7AA',
    backgroundColor: '#F9F9F9'
  },
  navBarTitle: {
    fontSize: 17,
    fontWeight: '600',
  },
})
