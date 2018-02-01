class NativeViewController: UIViewController {
    
    var manager : RNTurbolinksManager!
    var route: TurbolinksRoute!
    
    convenience init(manager: RNTurbolinksManager, route: TurbolinksRoute) {
        self.init()
        self.view = RCTRootView(bridge: manager.bridge, moduleName: route.component, initialProperties: route.passProps)
        self.manager = manager
        self.route = route
        self.renderTitle()
        self.renderActions()
        self.renderBackButton()
        self.renderLeftButton()
    }
    
    func handleTitlePress() {
        manager.handleTitlePress(URL: nil, component: route.component)
    }
    
    fileprivate func renderTitle() {
        if route.title != nil { self.title = route.title }
        navigationItem.titleView = TurbolinksTitleView(self)
    }
    
    fileprivate func renderActions() {
        if route.actions != nil {
            let button = UIBarButtonItem.init(title: "Menu", style: .plain, target: self, action: #selector(presentActions))
            navigationItem.rightBarButtonItem = button
        }
    }
    
    fileprivate func renderBackButton() {
        let backButton = UIBarButtonItem(title: nil, style: .plain, target: nil, action: nil)
        navigationItem.backBarButtonItem = backButton
        navigationItem.leftItemsSupplementBackButton = true
    }
    
    fileprivate func renderLeftButton() {
        if route.leftButtonIcon != nil {
            let button = UIBarButtonItem(image: route.leftButtonIcon, style: .plain, target: self, action: #selector(handleLeftButtonPress))
            navigationItem.leftBarButtonItem = button
        }
    }
    
    @objc func presentActions(sender: UIBarButtonItem) {
        if route.actions != nil {
            let actionsView = ActionsViewController(manager: manager, route: route, barButtonItem: sender)
            present(actionsView,animated: true)
        }
    }
    
    @objc fileprivate func handleLeftButtonPress() {
        manager.handleLeftButtonPress(URL: nil, component: route.component)
    }
    
}
