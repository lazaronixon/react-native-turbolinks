import React from 'react';
import Turbolinks from 'react-native-turbolinks'

export default class App extends React.Component {

  showMessage = (message) => {
    alert(message)
  }

  _onVisit = (data) => {
    if (data.path == "/numbers") {
      this.turboLinks.visit({component: "Numbers", title: "Numbers"})
    } else {
      this.turboLinks.visit({url: data.url, action: data.action})
    }
  }

  render() {
    return (
      <Turbolinks ref={(tl) => this.turboLinks = tl}
                  initialRoute={{url: "http://localhost:9292"}}
                  userAgent="turbolinksDemo"
                  onMessage={this.showMessage}
                  onVisit={this._onVisit}
                  style={{flex: 1}}/>
    )
  }
}
