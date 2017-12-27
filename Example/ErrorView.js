import React, { Component } from 'react';
import { FlatList, View, Button, Text, StyleSheet }from 'react-native';
import Turbolinks from 'react-native-turbolinks'

export default class ErrorView extends Component {

  retry = () => {
    alert('reload')
  }

  render() {
    const errorCode = this.props.error.code;
    const statusCode = this.props.error.statusCode;
    const httpFailure = Turbolinks.constants.ErrorCode.httpFailure;
    const networkFailure = Turbolinks.constants.ErrorCode.networkFailure;
    let title = null;
    let message = null;
    switch (errorCode) {
      case httpFailure: {
        switch (statusCode) {
          case 404:
            title = 'Page Not Found';
            message = 'There doesn’t seem to be anything here.';
            break;
          default:
            title = 'Unknown Error';
            message = 'An unknown error occurred.';
            break;
        }
        break;
      }
      case networkFailure: {
        title = 'Can’t Connect';
        message = 'TurbolinksDemo can’t connect to the server.\nDid you remember to start it?\nSee README.md for more instructions.';
        break;
      }
    }
    return (
      <View style={styles.container}>
        <Text style={styles.h1}>{title}</Text>
        <Text style={styles.p}>{message}</Text>
        <Button onPress={this.retry} title="Retry" />
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  h1: {
    fontSize: 28,
    paddingBottom: 10,
  },
  p: {
    fontSize: 18,
    paddingBottom: 10,
    textAlign: 'center',
  }
})
