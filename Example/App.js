import React, { Component } from 'react';
import Turbolinks from 'react-native-turbolinks'

export default class App extends Component {

  showMessage = (message) => {
    alert(message)
  }

  handleVisit = (data) => {
    if (data.path == "/numbers") {
      this.turboLinks.visit({component: "NumbersView", title: "Numbers"})
    } else {
      this.turboLinks.visit({url: data.url, action: data.action})
    }
  }

  handleError = (data) => {
    this.turboLinks.presentComponent("ErrorView")
  }

  render() {
    return (
      <Turbolinks ref={(tl) => this.turboLinks = tl}
                  url="http://localhost:9292"
                  userAgent="turbolinksDemo"
                  onMessage={this.showMessage}
                  onVisit={this.handleVisit}
                  onError={this.handleError}
                  style={{flex: 1}}/>
    )
  }
}
