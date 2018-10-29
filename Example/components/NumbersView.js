import React, { Component } from 'react'
import { FlatList, View, Text, StyleSheet, Platform }from 'react-native'

export default class NumbersView extends Component {

  dataSource() {
    return Array.from(new Array(100), (x,i) => { return { key: `Row ${i}` } })
  }

  render() {
    return (
      <View style={styles.container}>
        <FlatList data={this.dataSource()} renderItem={({item}) => <Text style={styles.item}>{item.key}</Text>}/>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    paddingTop: Platform.OS === 'ios' ? 64 : 0,
  },
  item: {
    padding: 10,
    fontSize: 18,
    height: 44,
  },
})
