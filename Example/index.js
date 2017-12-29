import { AppRegistry } from 'react-native'
import App from './components/App'
import NumbersView from './components/NumbersView'
import ErrorView from './components/ErrorView'
import AuthenticationView from './components/AuthenticationView'

AppRegistry.registerComponent('Example', () => App)
AppRegistry.registerComponent('NumbersView', () => NumbersView)
AppRegistry.registerComponent('ErrorView', () => ErrorView)
AppRegistry.registerComponent('AuthenticationView', () => AuthenticationView)
