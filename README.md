# React Native Turbolinks
A implementation of ![Turbolinks for iOS](https://github.com/turbolinks/turbolinks-ios) and ![Turbolinks Android](https://github.com/turbolinks/turbolinks-android) for React Native.

![React Native Turbolinks](https://raw.githubusercontent.com/lazaronixon/react-native-turbolinks/master/Example/screenshots/intro-turbolinks.gif)

## Getting started
```
$ npm install react-native-swift --save
$ npm install react-native-turbolinks --save

$ react-native link
```

### Installation iOS

Drop `/node_modules/react-native-turbolinks/ios/Turbolinks.framework` to XCode Embedded Binaries and then check `"Copy items if need"` when it is prompted.

[Install Turbolinks Framework IOS](https://raw.githubusercontent.com/lazaronixon/react-native-turbolinks/master/Example/screenshots/install-turbolinks-framework-ios.jpg)

### Installation Android
Change the variables below on ![/android/app/build.gradle](https://github.com/lazaronixon/react-native-turbolinks/blob/master/Example/android/app/build.gradle):
- compileSdkVersion 27
- buildToolsVersion "27.0.3"
- minSdkVersion 19
- targetSdkVersion 27
- compile "com.android.support:appcompat-v7:27.0.2"

Add google repository on ![/android/build.gradle](https://github.com/lazaronixon/react-native-turbolinks/blob/master/Example/android/build.gradle):
```
maven {
    url 'https://maven.google.com/'
    name 'Google'
}
```

On android you should use `Volume UP` to show Developer Menu instead `⌘ M`.

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
    Turbolinks.visit({url: 'http://MYIP:9292'}, true)
  }

  handleVisit = (data) => {
    Turbolinks.visit({url: data.url, action: data.action})
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

#### `visit(route, initial)`
Visit a URL with Turbolinks.
- `url:` Url to visit. (Required)
- `title:` The default value is the title of the Web page.
- `subtitle:` A subtitle for visitable view.
- `leftButtonIcon`: A left button icon. (iOS Only)
- `actions:` A Array of action objects to mount a menu.
  - `action:`
    - `id:` A integer identifier for the action. (Required)
    - `title:` A title for the action.
    - `icon:` A icon for the action.
    - `button:` A boolean to show action inside menu or in toolbar. (Android Only)(Default false)
- `action`: If action is 'advance', so it will perform a animated push, if "replace" will perform a pop without animation. (Default 'advance')
- `initial`: Should be true on first visit or if you need restart navigation. (Default false)

#### `visit(route, initial)`
Visit a Component with Turbolinks.
- `component:` Component to visit. (Required)
- `modal:` A boolean to show a view without navbar and backbutton. (Default false)
- `passProps`: Passes this in as props to the rendered component.
- `title:` The default value is the title of the Web page.
- `subtitle:` A subtitle for visitable view.
- `leftButtonIcon`: A left button icon. (iOS Only)
- `actions:` A Array of action objects to mount a menu.
  - `action:`
    - `id:` A integer identifier for the action. (Required)
    - `title:` A title for the action.
    - `icon:` A icon for the action.
    - `button:` A boolean to show action inside menu or in toolbar. (Android Only)(Default false).
- `action`: If action is 'advance', so it will perform a animated push, if "replace" will perform a pop without animation. (Default 'advance')
- `initial`: Should be true on first visit or if you need restart navigation. (Default false)

#### `replaceWith(route)`
#### `replaceWith(route, tabIndex)`
Replace current visitable with a component. With the same route param like to visit a component.

#### `reloadVisitable()`
Reload current visitable. For example when a connection error view is launched and you want to retry.

#### `reloadSession()`
Reload current session and (inject shared cookies on turbolinks before it (iOS >= 11)).

#### `dismiss()`
Dismiss a overlaped view presented by visiting a component with modal option.

#### `back()`
Trigger a native back event. For example if you using a custom navbar and need to provide a back button.

#### `setNavigationBarStyle({titleTextColor, subtitleTextColor, barTintColor, tintColor})`(iOS Only)
Set style for navigation bar on iOS. For android set your style on ![android/app/src/main/res/values/styles.xml](https://github.com/lazaronixon/react-native-turbolinks/blob/master/Example/android/app/src/main/res/values/styles.xml).

#### `setLoadingStyle({color, backgroundColor})` (iOS Only)
Set style for Activity Indicator View. For android set your style on ![android/app/src/main/res/values/styles.xml](https://github.com/lazaronixon/react-native-turbolinks/blob/master/Example/android/app/src/main/res/values/styles.xml).

#### `setNavigationBarHidden(boolean)`
Hidden navigation bar. For example if you want to use a web navbar. (Default false)

#### `visitTabBar(routes)`
#### `visitTabBar(routes, selectedIndex)`
A tab bar appears at the bottom of an app screen and provides the ability to quickly switch between different sections of an app.
- `routes:` A Array of route objects to mount TabBar. Use `tabTitle` and `tabIcon` here.
- `selectedIndex:` Index for initial selected view. (Default 0).

#### `setTabBarStyle({barTintColor, tintColor})` (iOS Only)
Set style for TabBar. For android set your style on ![android/app/src/main/res/values/styles.xml](https://github.com/lazaronixon/react-native-turbolinks/blob/master/Example/android/app/src/main/res/values/styles.xml).

#### `renderTitle(title)`
#### `renderTitle(title, subtitle)`
#### `renderTitle(title, subtitle, tabIndex)`
Change title of current view. For example if you want to get title from page source.

#### `renderActions(actions)`
#### `renderActions(actions, tabIndex)`
Change actions of current view. For example if you want to mount a menu looking for data-attributes on page source.
- `action:`
  - `id:` A integer identifier for the action. (Required)
  - `title:` A title for the action.
  - `icon:` A icon for the action.
  - `button:` A boolean to show action inside menu or in toolbar. (Android Only)(Default false).

#### `addEventListener(eventName, handler)`
Adds an event handler. Supported events:
- `turbolinksVisit`: Fires when you tap a Turbolinks-enabled link. The argument to the event handler is an object with keys: `url, path, action`.
- `turbolinksError`: Fires when your visit’s network request fails.The argument to the event handler is an object with keys: `code, statusCode, description, tabIndex`.
- `turbolinksMessage`: Fires when you send messages from JavaScript to your native application. The argument to the event handler is a string with the message.
- `turbolinksTitlePress`: Fires when you tap view title. The arguments to the event handler is an object with keys: `url, path, component`.
- `turbolinksActionPress`: Fire when a action is tapped. The arguments to the event is a integer with the action id.
- `turbolinksLeftButtonPress:` Fire when left button item on iOS is tapped. The arguments to the event handler is an object with keys: `url, path, component`.
- `turbolinksVisitCompleted`: Fire when the request has been fulfilled successfully and the page fully rendered, Here you can parse html and create a dynamic menu for example. The arguments to the event handler is `url, path, source, tabIndex`.

#### `removeEventListener(eventName, handler)`
Removes the listener for given event.

## Constants

`Turbolinks.Constants.ErrorCode.httpFailure: 0`

`Turbolinks.Constants.ErrorCode.networkFailure: 1`

`Turbolinks.Constants.Action.advance: 'advance'`

`Turbolinks.Constants.Action.replace: 'replace'`

`Turbolinks.Constants.Action.restore: 'restore'`

## TODO

- [x] ![Third party library](https://github.com/lazaronixon/react-native-form-sheet) to provide native dialogs. Using ![MZFormSheetPresentationController
](https://github.com/m1entus/MZFormSheetPresentationController) and [Dialogs](https://developer.android.com/guide/topics/ui/dialogs.html).
- [ ] [Segmented Controls](https://developer.apple.com/ios/human-interface-guidelines/controls/segmented-controls/)

## Resources

- [RailsConf 2016 - Turbolinks 5: I Can’t Believe It’s Not Native! by Sam Stephenson](https://www.youtube.com/watch?v=SWEts0rlezA)
