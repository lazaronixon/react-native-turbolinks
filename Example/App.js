import React, { Component } from 'react';
import { Platform, StyleSheet, Text, View } from 'react-native';
import Turbolinks from 'react-native-turbolinks'

export default class App extends Component<{}> {

  render() {
    return (
      <Turbolinks url="http://localhost:9292" style={{flex: 1}}/>
    );
  }
}
