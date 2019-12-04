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
    var turbolinksBundle: Bundle { Bundle(path: Bundle.main.path(forResource: "RNTurbolinks", ofType: "bundle")!)! }
    var defaultMenuIcon: UIImage { UIImage(named: "ic_menu", in: turbolinksBundle, compatibleWith: nil)! }
    
    func renderTitle() { navigationItem.titleView = UITitleView(self) }
    
    func setupNavigationBar() { navigationController?.navigationBar.isHidden = route.navBarHidden }
    
    func renderBackButton() {
        navigationItem.backBarButtonItem = UIBarButtonItem(title: nil, style: .plain, target: nil, action: nil)
        navigationItem.leftItemsSupplementBackButton = true
    }
    
    func renderLeftButton() {
        if let text = route.leftButtonText {
            navigationItem.leftBarButtonItem = UIBarButtonItem(title: text, style: .plain, target: self, action: selectorHandleLeftButtonPress)
        }
        if let icon = route.leftButtonIcon {
            navigationItem.leftBarButtonItem = UIBarButtonItem(image: icon, style: .plain, target: self, action: selectorHandleLeftButtonPress)
        }
    }
    
    func renderRightButton() {
        if let text = route.rightButtonText {
            navigationItem.rightBarButtonItem = UIBarButtonItem(title: text, style: .plain, target: self, action: selectorHandleLeftButtonPress)
        }
        if let icon = route.rightButtonIcon {
            navigationItem.rightBarButtonItem = UIBarButtonItem(image: icon, style: .plain, target: self, action: selectorHandleLeftButtonPress)
        }
    }
    
    func renderActions() {
        guard let actions = route.actions, !actions.isEmpty else { return }
        navigationItem.rightBarButtonItem = UIBarButtonItem(image: manager.menuIcon ?? defaultMenuIcon, style: .plain, target: self, action: selectorPresentActions)
    }
    
    func presentActions(_ sender: UIBarButtonItem) {
        guard let actions = route.actions, !actions.isEmpty else { return }
        present(ActionsViewController(manager, route, sender), animated: true)
    }
}
