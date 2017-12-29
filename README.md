# React Native Turbolinks
A implementation of [Turbolinks for iOS](https://github.com/turbolinks/turbolinks-ios) and [Turbolinks Android (Coming soon)](https://github.com/turbolinks/turbolinks-android) for React Native.

## Getting started
`$ npm install react-native-turbolinks --save`

### Installation
### Step 1
`$ react-native link`

### Step 2
Drag `/node_modules/react-native-turbolinks/ios/turbolinks.framework` to XCode Embedded Binaries.
![Install Turbolinks Framework IOS](https://raw.githubusercontent.com/lazaronixon/react-native-turbolinks/master/screenshots/install-turbolinks-framework-ios.jpg)

## Basic Usage
```javascript
import React, { Component } from 'react'
import Turbolinks from 'react-native-turbolinks'

export default class App extends Component {

  componentDidMount() {
    this.turboLinks.visit({url: 'http://localhost:9292'})
  }

  handleVisit = (data) => {
    this.turboLinks.visit({url: data.url, action: data.action})
  }

  handleError = (data) => {
    alert(data.description)
  }

  render() {
    return (
      <Turbolinks ref={(tl) => this.turboLinks = tl}
                  onVisit={this.handleVisit}
                  onError={this.handleError}
                  style={{flex: 1}}/>
    )
  }
}
```

## Running the Demo
This repository includes a demo application to show off features of the framework. The demo bundles a simple HTTP server that serves a Turbolinks 5 web app on localhost at port 9292.

To run the demo, clone this repository to your computer and change into its directory. Then, start the demo server by running `Example/demo-server` from the command line.

Once you’ve started the demo server, explore the demo application in the Simulator by running `react-native run-ios` on Example folder.

## Properties

#### `onVisit`

#### `onError`

#### `onMessage`

#### `userAgent`

## Methods

#### `visit`

#### `replaceWith`

#### `reloadVisitable`

#### `reloadSession`

#### `dismiss`
