class NativeViewController: UIViewController {
    
    var manager: RNTurbolinksManager!    
    var route: TurbolinksRoute!
    var selectorHandleLeftButtonPress: Selector = #selector(handleLeftButtonPress)
    var selectorPresentActions: Selector = #selector(presentActionsGeneric)
    
    convenience required init(manager: RNTurbolinksManager, route: TurbolinksRoute) {
        self.init()
        self.view = RCTRootView(bridge: manager.bridge, moduleName: route.component, initialProperties: route.passProps)
        self.manager = manager
        self.route = route
        self.renderTitle()
        self.renderActions()
        self.renderBackButton()
        self.renderLeftButton()
    }
    
}

extension NativeViewController: GenricViewController {
    
    @objc func handleLeftButtonPress() {
        manager.handleLeftButtonPress(URL: nil, component: self.route.component)
    }
    
    @objc func presentActionsGeneric(_ sender: UIBarButtonItem) {
        self.presentActions(sender)
    }
}
