import React, { Component } from 'react';
import Turbolinks from 'react-native-turbolinks'

export default class App extends Component {

  componentDidMount = () => {
    this.turboLinks.visit({url: 'http://localhost:9292'})
  }

  handleVisit = (data) => {
    if (data.path == '/numbers') {
      this.turboLinks.visit({component: 'NumbersView', title: 'Numbers'})
    } else {
      this.turboLinks.visit({url: data.url, action: data.action})
    }
  }

  handleError = (data) => {
    this.turboLinks.present({component: 'ErrorView', passProps: {error: data}})
  }

  showMessage = (message) => {
    alert(message)
  }

  render() {
    return (
      <Turbolinks ref={(tl) => this.turboLinks = tl}
                  userAgent='turbolinksDemo'
                  onVisit={this.handleVisit}
                  onError={this.handleError}
                  onMessage={this.showMessage}
                  style={{flex: 1}}/>
    )
  }
}
