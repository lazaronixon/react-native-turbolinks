class NativeViewController: UIViewController {
    
    var manager: RNTurbolinksManager!    
    var route: TurbolinksRoute!
    var selectorHandleLeftButtonPress: Selector = #selector(handleLeftButtonPress)
    var selectorHandleRightButtonPress: Selector = #selector(handleRightButtonPress)
    var selectorPresentActions: Selector = #selector(presentActionsGeneric)
    
    convenience required init(_ manager: RNTurbolinksManager,_ route: TurbolinksRoute) {
        self.init()
        self.modalPresentationStyle = .fullScreen
        self.manager = manager
        self.route = route
    }
    
    override func loadView() {
        self.view = RCTRootView(bridge: manager.bridge, moduleName: route.component!, initialProperties: route.passProps)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        renderTitle()
        renderActions()
        renderBackButton()
        renderLeftButton()
        renderRightButton()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        setupNavigationBar()
    }
    
}

extension NativeViewController: ApplicationViewController {
    
    func handleTitlePress() {
        manager.handleTitlePress(nil, route.component)
    }
    
    @objc func handleLeftButtonPress() {
        manager.handleLeftButtonPress(nil, self.route.component)
    }
    
    @objc func handleRightButtonPress() {
        manager.handleRightButtonPress(nil, self.route.component)
    }
    
    @objc func presentActionsGeneric(_ sender: UIBarButtonItem) {
        self.presentActions(sender)
    }
}
