protocol GenricViewController {
    
    var manager: RNTurbolinksManager! { get set }
    var route: TurbolinksRoute! { get set }
    var selectorHandleLeftButtonPress: Selector { get }
    var selectorPresentActions: Selector { get }
    var navigationItem: UINavigationItem { get }
    
    init(_ manager: RNTurbolinksManager,_ route: TurbolinksRoute)
    func renderTitle()
    func renderActions()
    func renderBackButton()
    func renderLeftButton()
    func handleTitlePress()
    func presentActionsGeneric(_ sender: UIBarButtonItem)
}

extension GenricViewController where Self: UIViewController {
    
    var isRoot: Bool { return navigationController?.viewControllers.count == 1 }
    
    func renderTitle() {
        if let title = route.title, !title.isEmpty { navigationItem.title = title }
        navigationItem.titleView = TurbolinksTitleView(self)
    }
    
    func renderBackButton() {
        let backButton = UIBarButtonItem(title: nil, style: .plain, target: nil, action: nil)
        navigationItem.backBarButtonItem = backButton
        navigationItem.leftItemsSupplementBackButton = true
    }
    
    func renderLeftButton() {
        guard let icon = route.leftButtonIcon else { return }
        let button = UIBarButtonItem(image: icon, style: .plain, target: self, action: selectorHandleLeftButtonPress)
        navigationItem.leftBarButtonItem = button
    }
    
    func renderActions() {
        guard let actions = route.actions, !actions.isEmpty else { return }
        if let icon = manager.customMenuIcon {
            let button = UIBarButtonItem(image: icon, style: .plain, target: self, action: selectorPresentActions)
            navigationItem.rightBarButtonItem = button
        } else {
            let button = UIBarButtonItem(title: "\u{22EF}", style: .plain, target: self, action: selectorPresentActions)
            let font = [NSAttributedStringKey.font: UIFont.boldSystemFont(ofSize: 32)]
            button.setTitleTextAttributes(font, for: .normal)
            button.setTitleTextAttributes(font, for: .selected)
            navigationItem.rightBarButtonItem = button
        }
    }
    
    func setupNavigationBar() {
        navigationController!.navigationBar.isHidden = route.navBarHidden
    }
    
    func presentActions(_ sender: UIBarButtonItem) {
        guard let actions = route.actions, !actions.isEmpty else { return }
        present(ActionsViewController(manager, route, sender), animated: true)
    }
}
