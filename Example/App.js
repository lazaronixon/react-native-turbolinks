/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import { Platform, StyleSheet, Text, View } from 'react-native';
import Turbolinks from 'react-native-turbolinks'

export default class App extends Component<{}> {

  componentDidMount() {
    //this.turboLinksRef.visit();
  }

  render() {
    return (
      <Turbolinks ref={(tl) => { this.turboLinksRef = tl; }}/>
    );
  }
}
