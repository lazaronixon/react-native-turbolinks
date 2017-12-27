import React, { Component } from 'react';
import { FlatList, View, Button, Text, StyleSheet }from 'react-native';
import Turbolinks from 'react-native-turbolinks';
import PubSub from 'pubsub-js';

export default class ErrorView extends Component {

  retry = () => {
    PubSub.publish('retryEvent')
  }

  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.h1}>{this.props.title}</Text>
        <Text style={styles.p}>{this.props.message}</Text>
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
