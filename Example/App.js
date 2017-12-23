import React from 'react';
import Turbolinks from 'react-native-turbolinks'

export default class App extends React.Component {

  showMessage = (message) => {
    alert(message);
  }

  render() {
    return (
      <Turbolinks url="http://localhost:9292"
                  userAgent="turbolinksDemo"
                  onMessage={this.showMessage}
                  style={{flex: 1}}/>
    );
  }
}
