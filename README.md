# React Native Turbolinks
A implementation of [Turbolinks for iOS](https://github.com/turbolinks/turbolinks-ios) and [Turbolinks Android](https://github.com/turbolinks/turbolinks-android) for React Native.

[![React Native Turbolinks](https://raw.githubusercontent.com/lazaronixon/react-native-turbolinks/master/Example/screenshots/intro-turbolinks.gif)](https://youtu.be/2biqMoHn3jk "Quick Start Video")

## [Getting started](https://youtu.be/2biqMoHn3jk "Quick Start Video")
```
$ npm install react-native-turbolinks --save
$ react-native link
```

### Warning
This component only applies to projects made with react-native init or to those made with Create React Native App which have since ejected. For more information about ejecting, please see the [guide](https://github.com/react-community/create-react-native-app/blob/master/EJECTING.md) on the Create React Native App repository.

On android you should use `Volume UP` to show Developer Menu instead `⌘ M`.

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
![React Native Turbolinks Demo Application](https://raw.githubusercontent.com/lazaronixon/react-native-turbolinks/master/Example/screenshots/application-demo.png)
This repository includes a demo application to show off features of the framework. The demo bundles a simple HTTP server that serves a Turbolinks 5 web app on localhost at port 9292.

To run the demo, clone this repository to your computer and change into its directory. Then, Open file `Example/app.json` and change `baseUrl` with your IP and start the demo server by running `Example/demo-server` from the command line.

Once you’ve started the demo server, explore the demo application in the Simulator by running `react-native run-ios` or `react-native run-android` on `Example` folder.

## Methods

#### `startSingleScreenApp(route)`
#### `startSingleScreenApp(route, appOption)`
Start a Single Screen App. Use it instead visit for first visit.

#### `visit(route)`
Visit a URL or Component.

#### `replaceWith(route)`
Replace current visitable with a component. With the same route param like to visit a component.

#### `reloadVisitable()`
Reload current visitable. For example when a connection error view is launched and you want to retry.

#### `reloadSession()`
Reload current session.

#### `dismiss(animated)`
Dismiss a overlaped view presented by visiting a component with modal option.

#### `popToRoot(animated)`
Back until to root view.

#### `back(animated)`
Trigger a native back event. For example if you using a custom navbar and need to provide a back button.

#### `renderTitle(title)`
#### `renderTitle(title, subtitle)`
Change title of current view. For example if you want to get title from page source.

#### `injectJavaScript(script)`
Function that accepts a string that will be passed to the WebView and executed immediately as JavaScript.

#### `renderActions(actions)`
Change actions of current view. For example if you want to mount a menu looking for data-attributes on page source.

#### `addEventListener(eventName, handler)`
Adds an event handler. Supported events:
- `turbolinksVisit`: Fires when you tap a Turbolinks-enabled link. The argument to the event handler is an object with keys: `url, path, action`.
- `turbolinksError`: Fires when your visit’s network request fails.The argument to the event handler is an object with keys: `code, statusCode, description`.
- `turbolinksMessage`: Fires when you send messages from JavaScript to your native application. The argument to the event handler is a string with the message.
- `turbolinksTitlePress`: Fires when you tap view title. The arguments to the event handler is an object with keys: `url, path, component`.
- `turbolinksActionPress`: Fire when a action is tapped. The arguments to the event is a integer with the action id.
- `turbolinksLeftButtonPress`: Fire when left button item on iOS is tapped. The arguments to the event handler is an object with keys: `url, path, component`.
- `turbolinksRightButtonPress`: Fire when right button item on iOS is tapped. The arguments to the event handler is an object with keys: `url, path, component`.
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
  - `passProps`: Passes this in as props to the rendered component.
- Common properties
  - `title`: The default value is the title of the Web page.
  - `subtitle`: A subtitle for visitable view.
  - `titleImage`: A image to show on navbar.
  - `navBarHidden`: Hidden navigation bar. (Default false)
  - `navBarDropDown`: Show a small dropdown next to the title. (Default false)(iOS Only)
  - `leftButtonIcon`: A left button icon. (iOS Only)
  - `rightButtonIcon`: A right button icon. (iOS Only)
  - `navIcon`: Set the icon to display in the 'home' section of the action bar. (Android Only)
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
  - `button`: A boolean to show action inside menu or in toolbar. (Android Only)(Default false)

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
