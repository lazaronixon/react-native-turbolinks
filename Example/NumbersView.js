import React, { Component } from 'react';
import { FlatList, View, Text, StyleSheet }from 'react-native';

export default class NumbersView extends Component {

  dataSource() {
    var data = [];
    for (var i = 1; i <= 100; i++) data.push({key: "Row " + i});
    return data;
  }

  render() {
    return (
      <View style={{flex: 1}}>
        <FlatList
          data={this.dataSource()}
          renderItem={({item}) => <Text style={styles.item}>{item.key}</Text>}
        />
      </View>
    )
  }
}

const styles = StyleSheet.create({
  item: {
    padding: 10,
    fontSize: 18,
    height: 44,
  },
})
