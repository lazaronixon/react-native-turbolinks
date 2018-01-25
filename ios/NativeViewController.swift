class NativeViewController: UIViewController {
    
    var manager : RNTurbolinksManager!
    var route: TurbolinksRoute!
    
    convenience init(manager: RNTurbolinksManager, route: TurbolinksRoute) {
        self.init()
        self.view = RCTRootView(bridge: manager.bridge, moduleName: route.component, initialProperties: route.passProps)
        self.manager = manager
        self.route = route
        self.renderTitle()
        self.renderBackButton()
        self.renderRightButton()
        self.renderLeftButton()
        self.renderBackgroundColor()
        self.navigationItem.leftItemsSupplementBackButton = true
        
    }
    
    fileprivate func renderTitle() {
        if route.title != nil { self.title = route.title }
        navigationItem.titleView = TurbolinksTitleView(title: navigationItem.title!, subtitle: route.subtitle, manager: manager)
    }
    
    fileprivate func renderBackgroundColor() {
        let backgroundColor = manager.backgroundColor
        if backgroundColor != nil { view.backgroundColor = backgroundColor }
    }
    
    fileprivate func renderBackButton() {
        let button = UIBarButtonItem(title: "", style: .plain, target: nil, action: nil)
        if manager.backButtonTitleHidden == true { navigationItem.backBarButtonItem = button }
    }
    
    fileprivate func renderRightButton() {
        if route.rightButtonIcon != nil {
            let button = UIBarButtonItem(image: route.rightButtonIcon, style: .plain, target: self, action: #selector(self.handleRightButtonPress))
            navigationItem.rightBarButtonItem = button
        }
        if route.rightButtonTitle != nil {
            let button = UIBarButtonItem(title: route.rightButtonTitle, style: .plain, target: self, action: #selector(self.handleRightButtonPress))
            navigationItem.rightBarButtonItem = button
        }
    }
    
    fileprivate func renderLeftButton() {
        if route.leftButtonIcon != nil {
            let button = UIBarButtonItem(image: route.leftButtonIcon, style: .plain, target: self, action: #selector(self.handleLeftButtonPress))
            navigationItem.leftBarButtonItem = button
        }
        if route.leftButtonTitle != nil {
            let button = UIBarButtonItem(title: route.leftButtonTitle, style: .plain, target: self, action: #selector(self.handleLeftButtonPress))
            navigationItem.leftBarButtonItem = button
        }
    }
    
    @objc fileprivate func handleRightButtonPress() {
        manager.handleRightButtonPress(URL: nil, component: route.component)
    }
    
    @objc fileprivate func handleLeftButtonPress() {
        manager.handleLeftButtonPress(URL: nil, component: route.component)
    }
    
}
