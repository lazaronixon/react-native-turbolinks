import { AppRegistry } from 'react-native'
import App from './App'
import NumbersView from './NumbersView'
import ErrorView from './ErrorView'
import AuthenticationView from './AuthenticationView'

AppRegistry.registerComponent('Example', () => App)
AppRegistry.registerComponent('NumbersView', () => NumbersView)
AppRegistry.registerComponent('ErrorView', () => ErrorView)
AppRegistry.registerComponent('AuthenticationView', () => AuthenticationView)
