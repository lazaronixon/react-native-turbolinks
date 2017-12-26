import React, { Component } from 'react';
import { FlatList, View, Text, StyleSheet }from 'react-native';

export default class NumbersView extends Component {

  dataSource() {
    var data = [];
    for (var i = 1; i <= 100; i++) data.push({key: 'Row ' + i});
    return data;
  }

  render() {
    return (
      <View style={styles.container}>
        <FlatList
          data={this.dataSource()}
          renderItem={({item}) => <Text style={styles.item}>{item.key}</Text>}/>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    paddingTop: 64,
  },
  item: {
    padding: 10,
    fontSize: 18,
    height: 44,
  },
})
