class NativeViewController: UIViewController {
    
    var manager : RNTurbolinksManager!
    var route: TurbolinksRoute!
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        paintNavBar()
        renderTitle()
        renderBackButton()
        renderRightButton()
        renderLeftButton()
    }
    
    fileprivate func paintNavBar() {
        let navBar = navigationController?.navigationBar
        if manager.barTintColor != nil { navBar?.barTintColor = manager.barTintColor }
        if manager.tintColor != nil { navBar?.tintColor = manager.tintColor }
        if manager.titleTextColor != nil { navBar?.titleTextAttributes = [.foregroundColor: manager.titleTextColor!] }
    }
    
    fileprivate func renderTitle() {
        if route.title != nil { title = route.title }
    }
    
    fileprivate func renderBackButton() {
        let button = UIBarButtonItem(title: "", style: .plain, target: nil, action: nil)
        if manager.backButtonTitleHidden { navigationItem.backBarButtonItem = button }
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
