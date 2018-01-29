import React, { Component } from 'react'
import { NavigatorIOS, ToolbarAndroid, Platform, WebView, View, StyleSheet } from 'react-native'
import Turbolinks from 'react-native-turbolinks'
import Constants from './Constants';

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

  state = { url: Constants.baseUrl + '/sign-in' }

  handleNavigationStateChange = (navState) => {
    if (this.state.url != navState.url) { Turbolinks.dismiss() }
    this.setState({ url: navState.url })
  }

  render() {
    return (
      <View style={{flex: 1}}>
        {Platform.OS == 'android' && <ToolbarAndroid title="Sign in" style={styles.toolBar}/>}
        <WebView source={{uri: this.state.url }} onNavigationStateChange={this.handleNavigationStateChange}/>
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
