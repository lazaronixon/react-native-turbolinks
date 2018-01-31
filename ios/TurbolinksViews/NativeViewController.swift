class NativeViewController: UIViewController {
    
    var manager : RNTurbolinksManager!
    var route: TurbolinksRoute!
    
    convenience init(manager: RNTurbolinksManager, route: TurbolinksRoute) {
        self.init()
        self.view = RCTRootView(bridge: manager.bridge, moduleName: route.component, initialProperties: route.passProps)
        self.manager = manager
        self.route = route
        self.renderTitle()
    }
    
    fileprivate func renderTitle() {
        if route.title != nil { self.title = route.title }
        navigationItem.titleView = TurbolinksTitleView(self)
    }
    
    func handleTitlePress() {
        manager.handleTitlePress(URL: nil, component: route.component)
    }
    
}
