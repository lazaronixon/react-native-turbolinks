import React, { Component } from 'react'
import { SafeAreaView } from 'react-native'
import { WebView } from 'react-native-webview'
import { createStackNavigator, createAppContainer } from 'react-navigation'
import Turbolinks from 'react-native-turbolinks'
import Constants from './Constants'

const signInUrl = Constants.baseUrl + '/sign-in'

class SignIn extends Component {
  static navigationOptions = { title: 'Sign in' }

  handleAuthentication = (e) => {
    if (e.nativeEvent.url != signInUrl) {
      this.webview.stopLoading()

      setTimeout(() => Turbolinks.reloadSession(), 3000)
      Turbolinks.dismiss()
    }
  }

  render() {
    return (
      <SafeAreaView style={{flex: 1}}>
        <WebView ref={webview => { this.webview = webview }}
                 source={{uri: signInUrl}}
                 onLoadStart={this.handleAuthentication}/>
      </SafeAreaView>
    )
  }
}

export default class AuthenticationView extends React.Component {
  render() {
    let StackNavigator = createStackNavigator({ Home: { screen: SignIn } })
    let AppContainer = createAppContainer(StackNavigator)

    return <AppContainer detached={true} />
  }
}
