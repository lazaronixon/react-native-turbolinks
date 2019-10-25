protocol ApplicationViewController {
    
    var manager: RNTurbolinksManager! { get set }
    var route: TurbolinksRoute! { get set }
    var selectorHandleLeftButtonPress: Selector { get }
    var selectorHandleRightButtonPress: Selector { get }
    var selectorPresentActions: Selector { get }
    var navigationItem: UINavigationItem { get }
    
    init(_ manager: RNTurbolinksManager,_ route: TurbolinksRoute)
    func renderTitle()
    func renderActions()
    func renderBackButton()
    func renderLeftButton()
    func renderRightButton()
    func handleTitlePress()
    func presentActionsGeneric(_ sender: UIBarButtonItem)
}

extension ApplicationViewController where Self: UIViewController {
    
    var isRoot: Bool { navigationController?.viewControllers.count == 1 }
    var _navigationItem: UINavigationItem { (self as UIViewController).navigationItem }
    var turbolinksBundle: Bundle { Bundle(path: Bundle.main.path(forResource: "RNTurbolinks", ofType: "bundle")!)! }
    
    func renderTitle() {
        _navigationItem.titleView = TurbolinksTitleView(self)
    }
    
    func renderBackButton() {
        let backButton = UIBarButtonItem(title: nil, style: .plain, target: nil, action: nil)
        _navigationItem.backBarButtonItem = backButton
        _navigationItem.leftItemsSupplementBackButton = true
    }
    
    func renderLeftButton() {
        guard let icon = route.leftButtonIcon else { return }
        let button = UIBarButtonItem(image: icon, style: .plain, target: self, action: selectorHandleLeftButtonPress)
        _navigationItem.leftBarButtonItem = button
    }
    
    func renderRightButton() {
        guard let icon = route.rightButtonIcon else { return }
        let button = UIBarButtonItem(image: icon, style: .plain, target: self, action: selectorHandleRightButtonPress)
        _navigationItem.rightBarButtonItem = button
    }
    
    func renderActions() {
        guard let actions = route.actions, !actions.isEmpty else { return }
        let icon = manager.menuIcon ?? UIImage(named: "ic_menu", in: turbolinksBundle, compatibleWith: nil)
        let button = UIBarButtonItem(image: icon, style: .plain, target: self, action: selectorPresentActions)
        _navigationItem.rightBarButtonItem = button
    }
    
    func setupNavigationBar() {
        navigationController?.navigationBar.isHidden = route.navBarHidden
    }
    
    func presentActions(_ sender: UIBarButtonItem) {
        guard let actions = route.actions, !actions.isEmpty else { return }
        present(ActionsViewController(manager, route, sender), animated: true)
    }
}
