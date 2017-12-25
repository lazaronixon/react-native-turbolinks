import React from 'react';
import Turbolinks from 'react-native-turbolinks'

export default class App extends React.Component {

  showMessage = (message) => {
    alert(message);
  }

  _onVisit = (data) => {
    alert(data.path);
  }

  render() {
    return (
      <Turbolinks url="http://localhost:9292"
                  userAgent="turbolinksDemo"
                  onMessage={this.showMessage}
                  onVisit={this._onVisit}
                  style={{flex: 1}}/>
    );
  }
}
