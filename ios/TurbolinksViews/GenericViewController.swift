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
    
    func renderTitle() {
        navigationItem.title = route.title ?? navigationItem.title
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
        if (navigationController!.viewControllers.count != 1) { button.imageInsets = UIEdgeInsetsMake(0, -10, 0, 0) }
        navigationItem.leftBarButtonItem = button
    }
    
    func renderActions() {
        if route.actions == nil { return }
        let button = UIBarButtonItem.init(title: "\u{22EF}", style: .plain, target: self, action: selectorPresentActions)
        let font = [NSAttributedStringKey.font: UIFont.boldSystemFont(ofSize: 32)]
        button.setTitleTextAttributes(font, for: .normal)
        button.setTitleTextAttributes(font, for: .selected)
        navigationItem.rightBarButtonItem = button
    }
    
    func presentActions(_ sender: UIBarButtonItem) {
        if route.actions == nil { return }
        let actionsView = ActionsViewController(manager, route, sender)
        present(actionsView, animated: true)
    }
}
