import React, { Component } from 'react'
import { FlatList, Platform, View, Button, Text, StyleSheet }from 'react-native'
import Turbolinks from 'react-native-turbolinks'

export default class ErrorView extends Component {

  retry = () => {
    Turbolinks.reloadVisitable()
  }

  render() {
    let retryButton = Platform.OS == 'ios' ? <Button onPress={this.retry} title="Retry" /> : null
    return (
      <View style={styles.container}>
        <Text style={styles.h1}>{this.props.title}</Text>
        <Text style={styles.p}>{this.props.message}</Text>
        {retryButton}
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
