class PrimaryViewController: UIViewController {
    var manager: RNTurbolinksManager!
    var component: String!
    
    convenience required init(_ manager: RNTurbolinksManager,_ component: String) {
        self.init()
        self.manager = manager
        self.component = component
    }
    
    override func loadView() {
        self.view = RCTRootView(bridge: manager.bridge, moduleName: component, initialProperties: nil)
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        self.view = nil
    }
}
