# React Native Turbolinks
A implementation of [Turbolinks for iOS](https://github.com/turbolinks/turbolinks-ios) and [Turbolinks Android](https://github.com/turbolinks/turbolinks-android) for React Native.

[![React Native Turbolinks](https://raw.githubusercontent.com/lazaronixon/react-native-turbolinks/master/Example/screenshots/intro-turbolinks.gif)](https://youtu.be/2biqMoHn3jk "Quick Start Video")

## [Getting started](https://youtu.be/2biqMoHn3jk "Quick Start Video")
```sh
yarn add react-native-webview react-native-turbolinks
cd ios && pod install && cd .. # CocoaPods on iOS needs this extra step
```

### Warning
- This component only applies to projects made with react-native init or to those made with expo-cli which have since ejected. For more information about ejecting, please see the [guide](https://docs.expo.io/versions/latest/expokit/eject/#instructions) on Expo docs.

- On android you should use `Volume UP` to show Developer Menu instead `⌘ M`.

- React Native Turbolinks doesn't support [Fast Refresh](https://facebook.github.io/react-native/docs/fast-refresh), please disable it on React Native developer menu `⌘ D`.

- Start rails with `rails s -b 0.0.0.0`.

## Basic Usage
```javascript
import React, { Component } from 'react'
import Turbolinks from 'react-native-turbolinks'

export default class App extends Component {

  componentDidMount() {
    Turbolinks.addEventListener('turbolinksVisit', this.handleVisit)
    Turbolinks.addEventListener('turbolinksError', this.handleError)
    Turbolinks.startSingleScreenApp({url: 'http://MYIP:9292'})
  }

  handleVisit = (data) => {
    Turbolinks.visit({url: data.url, action: data.action})
  }

  handleError = (data) => {
    alert(data.description)
  }

  render() { return null }
}
```

## Running the Demo
This repository includes a demo application to show off features of the framework. The demo bundles a simple HTTP server that serves a Turbolinks 5 web app on localhost at port 9292.

To run the demo, clone this repository to your computer and change into its directory. Then, Open file `Example/app.json` and change `baseUrl` with your IP and start the demo server by running `Example/demo-server` from the command line.

Once you’ve started the demo server, explore the demo application in the Simulator by running `react-native run-ios` or `react-native run-android` on `Example` folder.

![React Native Turbolinks Demo Application](https://raw.githubusercontent.com/lazaronixon/react-native-turbolinks/master/Example/screenshots/application-demo.png)

## Methods

#### `startSingleScreenApp(route, appOption = {})`
Start a Single Screen App. Use it instead visit for first visit.

#### `startSplitScreenApp(primaryComponent, secondaryRoute, appOption = {})`
Start a Splitted Screen App. Use it instead visit for first visit. It is a good choice for iPad. (iOS Only)

#### `visit(route)`
Visit a URL or Component.

#### `replaceWith(route)`
Replace current visitable with a component. With the same route param like to visit a component.

#### `reloadVisitable()`
Reload current visitable. For example when a connection error view is launched and you want to retry.

#### `reloadSession()`
Reload current session.

#### `removeAllCookies()`
Remove all cookies. Return a promise.

#### `dismiss(animated = true)`
Dismiss a overlaped view presented by visiting a component with modal option. Return a promise.

#### `popToRoot(animated = true)`
Back until to root view. Return a promise.

#### `renderTitle(title, subtitle = null)`
Change title of current view. For example if you want to get title from page source.

#### `renderActions(actions)`
Change actions of current view. For example if you want to mount a menu looking for data-attributes on page source.

#### `renderNavBarStyle(navBarStyle)`
Change navbarStyle on run time. For example if you want to provide a way for the user to choose a theme.

#### `injectJavaScript(script)`
Function that accepts a string that will be passed to the WebView and executed immediately as JavaScript. Return a promise.

#### `addEventListener(eventName, handler)`
Adds an event handler. Supported events:
- `turbolinksVisit`: Fires when you tap a Turbolinks-enabled link. The argument to the event handler is an object with keys: `url, path, action`.
- `turbolinksError`: Fires when your visit’s network request fails.The argument to the event handler is an object with keys: `code, statusCode, description`.
- `turbolinksMessage`: Fires when you send messages from JavaScript to your native application. The argument to the event handler is a string with the message.
- `turbolinksActionPress`: Fire when a action is tapped. The arguments to the event handler is an object with keys: `url, path, component, actionId`.
- `turbolinksLeftButtonPress`: Fire when left button item is tapped. The arguments to the event handler is an object with keys: `url, path, component`. (iOS Only)
- `turbolinksRightButtonPress`: Fire when right button item is tapped. The arguments to the event handler is an object with keys: `url, path, component`. (iOS Only)
- `turbolinksVisitCompleted`: Fire when the request has been fulfilled successfully and the page fully rendered, Here you can parse html and create a dynamic menu for example. The arguments to the event handler is `url, path`.

#### `removeEventListener(eventName, handler)`
Removes the listener for given event.

## Objects
#### `Route`
- Url properties
  - `url`: Url to visit. (Required)
- Component properties
  - `component`: Component to visit. (Required)
  - `modal`: A boolean to show a view without navbar and backbutton. (Default false)
  - `dismissable`: When true is possible dismiss modal. (Default false)  
  - `passProps`: Passes this in as props to the rendered component.
- Common properties
  - `title`: The default value is the title of the Web page.
  - `subtitle`: A subtitle for visitable view.
  - `hidesShadow`: Indicates whether to hide the navigation 1px hairline shadow. (Default false) (iOS Only)  
  - `leftButtonText/leftButtonIcon`: A left button text/icon. (iOS Only)
  - `rightButtonText/rightButtonIcon`: A right button text/icon. (iOS Only)
  - `actions`: A Array of `action` objects to mount a menu.
  - `action`: If action is 'advance', so it will perform a animated push, if "replace" will perform a pop without animation. (Default 'advance')

#### `AppOption`
  - `userAgent`: You can check for this string on the server and use it to send specialized markup or assets to your application.
  - `messageHandler`: You can register a Message Handler to send messages from JavaScript to your application.
  - `loadingView`: Set a custom loadingView using a react component.
  - `navBarStyle`: {titleTextColor, subtitleTextColor, barTintColor, tintColor, menuIcon}.
  - `injectedJavaScript`: Set this to provide JavaScript that will be injected into the web page when the view loads.

#### `Action`
  - `id`: A integer identifier for the action. (Required)
  - `title`: A title for the action.
  - `icon`: A icon for the action.
  - `button`: A boolean to show action inside menu or in toolbar. (Default false) (Android Only)

## Constants

`Turbolinks.Constants.ErrorCode.httpFailure`: 0

`Turbolinks.Constants.ErrorCode.networkFailure`: 1

`Turbolinks.Constants.Action.advance`: 'advance'

`Turbolinks.Constants.Action.replace`: 'replace'

`Turbolinks.Constants.Action.restore`: 'restore'

## Android Style
For android set your style on [android/app/src/main/res/values/styles.xml](https://github.com/lazaronixon/react-native-turbolinks/blob/master/Example/android/app/src/main/res/values/styles.xml).

## TODO

- [x] [Third party library](https://github.com/lazaronixon/react-native-form-sheet) to provide native dialogs. Using [MZFormSheetPresentationController
](https://github.com/m1entus/MZFormSheetPresentationController) and [Dialogs](https://developer.android.com/guide/topics/ui/dialogs.html).

## Resources

- [RailsConf 2016 - Turbolinks 5: I Can’t Believe It’s Not Native! by Sam Stephenson](https://www.youtube.com/watch?v=SWEts0rlezA)
- [Basecamp 3 for iOS: Hybrid Architecture](https://m.signalvnoise.com/basecamp-3-for-ios-hybrid-architecture-afc071589c25)
- [The Majestic Monolith](https://m.signalvnoise.com/the-majestic-monolith-29166d022228)
