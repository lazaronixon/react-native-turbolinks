class NativeViewController: UIViewController {
    
    var manager: RNTurbolinksManager!    
    var route: TurbolinksRoute!
    var selectorHandleLeftButtonPress: Selector = #selector(handleLeftButtonPress)
    var selectorPresentActions: Selector = #selector(presentActionsGeneric)
    
    convenience required init(_ manager: RNTurbolinksManager,_ route: TurbolinksRoute) {
        self.init()
        self.manager = manager
        self.route = route
    }
    
    override func loadView() {
        self.view = RCTRootView(bridge: manager.bridge, moduleName: route.component, initialProperties: route.passProps)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        renderTitle()
        renderActions()
        renderBackButton()
        renderLeftButton()
    }
    
}

extension NativeViewController: GenricViewController {
    
    func handleTitlePress() {
        manager.handleTitlePress(nil, route.component)
    }
    
    @objc func handleLeftButtonPress() {
        manager.handleLeftButtonPress(nil, self.route.component)
    }
    
    @objc func presentActionsGeneric(_ sender: UIBarButtonItem) {
        self.presentActions(sender)
    }
}
