# React Native Turbolinks
A implementation of ![Turbolinks for iOS](https://github.com/turbolinks/turbolinks-ios) and ![Turbolinks Android (Coming soon)](https://github.com/turbolinks/turbolinks-android) for React Native.

## Getting started
`$ npm install react-native-turbolinks --save`

`$ react-native link`

### Installation iOS

### Step 1
Drop `/node_modules/react-native-turbolinks/ios/Turbolinks.framework` to XCode Embedded Binaries and then check `"Copy items if need"` when it is prompted.

![Install Turbolinks Framework IOS](https://raw.githubusercontent.com/lazaronixon/react-native-turbolinks/master/Example/screenshots/install-turbolinks-framework-ios.jpg)

### Step 2
On XCode add a empty swift file clicking with right mouse button on main folder project and then click on `Create Bridging Header` when prompted.

### Warning
This component only applies to projects made with react-native init or to those made with Create React Native App which have since ejected. For more information about ejecting, please see the ![guide](https://github.com/react-community/create-react-native-app/blob/master/EJECTING.md) on the Create React Native App repository.

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

Once you’ve started the demo server, explore the demo application in the Simulator by running `react-native run-ios` on `Example` folder.

## Properties

#### `onVisit: Event {{url, path, action}} required`
Function that is invoked when you tap a Turbolinks-enabled link or call this.turbolinks.visit(...).

#### `onError: Event {{code, statusCode, description}} required`
Function that is invoked when your visit’s network request fails.

#### `onMessage: Event {message}`
Function that is invoked when you send messages from JavaScript to your native application.

#### `userAgent: String`
You can check for this string on the server and use it to send specialized markup or assets to your application.

## Methods

#### `visit({url:required, title, action})`
Visit a URL with Turbolinks, your params are url, custom title and action. If action is 'advance'(default), so it will perform a animated push, if "replace" will perform a pop without animation.

#### `visit({component:required, title, passProps, action})`
Visit a Component with Turbolinks, your params are component, title, passProps that is a hash with props to next component and action. If action is 'advance'(default), so it will perform a animated push, if "replace" will perform a pop without animation on a overlaped view.

#### `replaceWith({component:required, title, passProps})`
Replace current visitable with a component, your params are component, title and passProps that is a hash with props to next component.

#### `reloadVisitable()`
Reload current visitable. For example when a connection error view is launched and you want to retry.

#### `reloadSession()`
Reload current session and inject shared cookies on turbolinks before it.

#### `dismiss()`
Dismiss a overlaped view presented by visiting a component with replace action.

## Constants

`Turbolinks.Constants.ErrorCode.httpFailure: 0`

`Turbolinks.Constants.ErrorCode.networkFailure: 1`

`Turbolinks.Constants.Action.advance: 'advance'`

`Turbolinks.Constants.Action.replace: 'replace'`

`Turbolinks.Constants.Action.restore: 'restore'`
