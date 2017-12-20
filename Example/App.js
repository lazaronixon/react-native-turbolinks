import React, { Component } from 'react';
import { Platform, StyleSheet, Text, View } from 'react-native';
import Turbolinks from 'react-native-turbolinks'

export default class App extends Component<{}> {

  componentDidMount() {
    this.turboLinksRef.visit('https://3.basecamp.com/sign_in');
    //this.turboLinksRef.visit('https://www.google.com');
    //alert('2')
  }

  render() {
    return (
      <Turbolinks ref={(tl) => { this.turboLinksRef = tl; }}/>
    );
  }
}
