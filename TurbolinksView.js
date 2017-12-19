import { requireNativeComponent, NativeModules } from 'react-native';

const RNTurbolinksView = requireNativeComponent('RNTurbolinks', null);
const RNTurbolinksManager = NativeModules.RNTurbolinksManager;

module.exports = RNTurbolinksView;
