class NativeViewController: UIViewController {
    
    var manager: RNTurbolinksManager!    
    var route: TurbolinksRoute!
    var selectorHandleLeftButtonPress: Selector!
    var selectorPresentActions: Selector!
    
    convenience init(manager: RNTurbolinksManager, route: TurbolinksRoute) {
        self.init()
        self.view = RCTRootView(bridge: manager.bridge, moduleName: route.component, initialProperties: route.passProps)
        self.manager = manager
        self.route = route
        self.selectorHandleLeftButtonPress = #selector(handleLeftButtonPress)
        self.selectorPresentActions = #selector(presentActionsGeneric)
        self.renderTitle()
        self.renderActions()
        self.renderBackButton()
        self.renderLeftButton()
    }
    
}

extension NativeViewController: GenricViewController{
    
    @objc func handleLeftButtonPress() {
        manager.handleLeftButtonPress(URL: nil, component: self.route.component)
    }
    
    @objc func presentActionsGeneric(_ sender: UIBarButtonItem) {
        self.presentActions(sender)
    }
}
