protocol GenricViewController {
    
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

extension GenricViewController where Self: UIViewController {
    
    var isRoot: Bool { return navigationController?.viewControllers.count == 1 }
    var _navigationItem: UINavigationItem { return (self as UIViewController).navigationItem }
    
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
        if let icon = manager.customMenuIcon {
            let button = UIBarButtonItem(image: icon, style: .plain, target: self, action: selectorPresentActions)
            _navigationItem.rightBarButtonItem = button
        } else {
            let button = UIBarButtonItem(title: "\u{22EF}", style: .plain, target: self, action: selectorPresentActions)
            let font = [NSAttributedString.Key.font: UIFont.boldSystemFont(ofSize: 32)]
            button.setTitleTextAttributes(font, for: .normal)
            button.setTitleTextAttributes(font, for: .selected)
            _navigationItem.rightBarButtonItem = button
        }
    }
    
    func setupNavigationBar() {
        navigationController?.navigationBar.isHidden = route.navBarHidden
    }
    
    func presentActions(_ sender: UIBarButtonItem) {
        guard let actions = route.actions, !actions.isEmpty else { return }
        present(ActionsViewController(manager, route, sender), animated: true)
    }
}
