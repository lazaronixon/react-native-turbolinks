import React, { Component } from 'react';
import { FlatList, View, Text, StyleSheet }from 'react-native';

export default class ErrorView extends Component {

  render() {
    return (
      <View style={{flex: 1}}>
        <Text>Erro</Text>
      </View>
    )
  }
}

const styles = StyleSheet.create({

})
