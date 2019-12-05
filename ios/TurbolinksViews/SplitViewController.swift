class SplitViewController: UISplitViewController {
    var manager: RNTurbolinksManager!
    var component: String!
    
    convenience required init(_ primaryController: UIViewController,_ secondaryController: UIViewController) {
        self.init()
        self.preferredDisplayMode = .allVisible
        self.viewControllers = [primaryController, secondaryController]
    }
}

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
}
