import { AppRegistry } from 'react-native';
import App from './App';
import NumbersView from './NumbersView';
import ErrorView from './ErrorView';

AppRegistry.registerComponent('Example', () => App);
AppRegistry.registerComponent('NumbersView', () => NumbersView);
AppRegistry.registerComponent('ErrorView', () => ErrorView);
