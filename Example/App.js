import React, { Component } from 'react';
import { Platform, StyleSheet, Text, View } from 'react-native';
import Turbolinks from 'react-native-turbolinks'

export default class App extends Component<{}> {

  componentDidMount() {
    this.turboLinks.visit('http://localhost:9292');
  }

  render() {
    return (
      <Turbolinks ref={tl => this.turboLinks = tl}/>
    );
  }
}
