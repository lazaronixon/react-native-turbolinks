# React Native Turbolinks
A implementation of ![Turbolinks for iOS](https://github.com/turbolinks/turbolinks-ios) and ![Turbolinks Android](https://github.com/turbolinks/turbolinks-android) for React Native.

## Getting started
```
$ npm install react-native-swift --save
$ npm install react-native-turbolinks --save

$ react-native link
```

### Installation iOS

Drop `/node_modules/react-native-turbolinks/ios/Turbolinks.framework` to XCode Embedded Binaries and then check `"Copy items if need"` when it is prompted.

![Install Turbolinks Framework IOS](https://raw.githubusercontent.com/lazaronixon/react-native-turbolinks/master/Example/screenshots/install-turbolinks-framework-ios.jpg)

### Installation Android
Change the variables below on `/android/app/build.gradle`.
- compileSdkVersion 25
- buildToolsVersion "25.0.3"
- minSdkVersion 19

### Warning
This component only applies to projects made with react-native init or to those made with Create React Native App which have since ejected. For more information about ejecting, please see the ![guide](https://github.com/react-community/create-react-native-app/blob/master/EJECTING.md) on the Create React Native App repository.

## Basic Usage
```javascript
import React, { Component } from 'react'
import Turbolinks from 'react-native-turbolinks'

export default class App extends Component {

  componentDidMount() {
    Turbolinks.addEventListener('turbolinksVisit', this.handleVisit)
    Turbolinks.addEventListener('turbolinksError', this.handleError)
    Turbolinks.visit({url: 'http://localhost:9292'})
  }

  handleVisit = (data) => {
    this.turboLinks.visit({url: data.url, action: data.action})
  }

  handleError = (data) => {
    alert(data.description)
  }

  render() {
    return null
  }
}
```

## Running the Demo
This repository includes a demo application to show off features of the framework. The demo bundles a simple HTTP server that serves a Turbolinks 5 web app on localhost at port 9292.

To run the demo, clone this repository to your computer and change into its directory. Then, Open file `Example/components/App/Constants.js` and change `baseUrl` with your IP and start the demo server by running `Example/demo-server` from the command line.

Once you’ve started the demo server, explore the demo application in the Simulator by running `react-native run-ios` or `react-native run-android` on `Example` folder.

## Methods

#### `setUserAgent(userAgent)`
You can check for this string on the server and use it to send specialized markup or assets to your application.

#### `setMessageHandler(messageHandler)`
You can register a Message Handler to send messages from JavaScript to your application.

#### `visit({url:required, title, action})`
Visit a URL with Turbolinks, your params are url, custom title and action. If action is 'advance'(default), so it will perform a animated push, if "replace" will perform a pop without animation.

#### `visit({component:required, title, modal, passProps, action})`
Visit a Component with Turbolinks, your params are component, title, modal, passProps that is a hash with props to next component and action. If action is 'advance'(default), so it will perform a animated push, if "replace" will perform a pop without animation on a overlaped view.

#### `replaceWith({component:required, title, passProps}) (iOS ONLY)`
Replace current visitable with a component, your params are component, title and passProps that is a hash with props to next component.

#### `reloadVisitable()`
Reload current visitable. For example when a connection error view is launched and you want to retry.

#### `reloadSession()`
Reload current session and inject shared cookies on turbolinks before it.

#### `dismiss()`
Dismiss a overlaped view presented by visiting a component with modal option.

#### `addEventListener(eventName, handler)`
Adds an event handler. Supported events:
- `onVisit`: Fires when you tap a Turbolinks-enabled link or call Turbolinks.visit(...). The argument to the event handler is an object with keys: `url, path, action`.
- `onError`: Fires when your visit’s network request fails.The argument to the event handler is an object with keys: `code, statusCode, description`.
- `onMessage`: Fires when you send messages from JavaScript to your native application. The argument to the event handler is a string with the message.

#### `removeEventListener(eventName, handler)`
Removes the listener for given event.

## Constants

`Turbolinks.Constants.ErrorCode.httpFailure: 0`

`Turbolinks.Constants.ErrorCode.networkFailure: 1`

`Turbolinks.Constants.Action.advance: 'advance'`

`Turbolinks.Constants.Action.replace: 'replace'`

`Turbolinks.Constants.Action.restore: 'restore'`
