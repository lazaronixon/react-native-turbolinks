import React, { Component } from 'react'
import { FlatList, Text, StyleSheet, SafeAreaView }from 'react-native'

export default class NumbersView extends Component {

  get dataSource() {
    return Array.from(new Array(100), (x,i) => { return { key: `Row ${i}` } })
  }

  render() {
    return (
      <SafeAreaView style={{flex: 1}}>
        <FlatList data={this.dataSource} renderItem={({item}) => <Text style={styles.item}>{item.key}</Text>}/>
      </SafeAreaView>
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
