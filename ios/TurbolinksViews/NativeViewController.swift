class NativeViewController: UIViewController {    
    var manager: RNTurbolinksManager!    
    var route: TurbolinksRoute!
    var selectorHandleLeftButtonPress: Selector = #selector(handleLeftButtonPress)
    var selectorHandleRightButtonPress: Selector = #selector(handleRightButtonPress)
    var selectorPresentActions: Selector = #selector(presentActionsGeneric)
    
    convenience required init(_ manager: RNTurbolinksManager,_ route: TurbolinksRoute) {
        self.init()
        self.manager = manager
        self.route = route
        self.modalPresentationStyle = route.dismissable ? .formSheet : .fullScreen
    }
    
    override func loadView() {
        self.view = RCTRootView(bridge: manager.bridge, moduleName: route.component!, initialProperties: route.passProps)
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        self.view = nil
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        hideOrDisplayShadow()
        hideOrDisplayNavBar()
    }
    
    override func willTransition(to newCollection: UITraitCollection, with coordinator: UIViewControllerTransitionCoordinator) {
        super.willTransition(to: newCollection, with: coordinator)
        renderLeftButton()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        renderTitle()
        renderActions()
        renderBackButton()
        renderLeftButton()
        renderRightButton()
    }
}

extension NativeViewController: ApplicationViewController {
    func handleTitlePress() {
        manager.handleTitlePress(nil, route.component)
    }
    
    @objc func handleLeftButtonPress() {
        manager.handleLeftButtonPress(nil, route.component)
    }
    
    @objc func handleRightButtonPress() {
        manager.handleRightButtonPress(nil, route.component)
    }
    
    @objc func presentActionsGeneric(_ sender: UIBarButtonItem) {
        presentActions(sender)
    }
}
