/**
 * @format
 */

import { AppRegistry } from 'react-native'
import App from './components/App'
import NumbersView from './components/NumbersView'
import ErrorView from './components/ErrorView'
import AuthenticationView from './components/AuthenticationView'
import {name as appName} from './app.json'

AppRegistry.registerComponent(appName, () => App)
AppRegistry.registerComponent('NumbersView', () => NumbersView)
AppRegistry.registerComponent('ErrorView', () => ErrorView)
AppRegistry.registerComponent('AuthenticationView', () => AuthenticationView)
