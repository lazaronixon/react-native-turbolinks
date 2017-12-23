import React, { Component } from 'react';
import { Platform, StyleSheet, Text, View } from 'react-native';
import Turbolinks from 'react-native-turbolinks'

export default class App extends Component<{}> {

  showMessage = (event) => {
    alert(event.nativeEvent.data);
  }

  render() {
    return (
      <Turbolinks url="http://localhost:9292"
                  userAgent="turbolinksDemo"
                  onMessage={this.showMessage}
                  style={{flex: 1}}/>
    );
  }
}
