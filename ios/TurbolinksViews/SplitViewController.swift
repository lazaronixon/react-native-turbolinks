class SplitViewController: UISplitViewController {
    var manager: RNTurbolinksManager!
    var primaryController: UIViewController!
    var secondaryController: UIViewController!
        
    convenience required init(_ manager: RNTurbolinksManager,_ primaryComponent: String,_ secondaryController: UIViewController) {
        self.init()
        self.manager = manager
        self.primaryController = PrimaryViewController(manager, primaryComponent)
        self.secondaryController = secondaryController
        
        self.delegate = self
        self.preferredDisplayMode = .allVisible
        self.preferredPrimaryColumnWidthFraction = 0.4
        self.viewControllers = [self.primaryController, self.secondaryController]
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
        
        override func viewDidDisappear(_ animated: Bool) {
            super.viewDidDisappear(animated)
            self.view = nil
        }
    }
}

extension SplitViewController: UISplitViewControllerDelegate {
    func primaryViewController(forCollapsing splitViewController: UISplitViewController) -> UIViewController? {
        return secondaryController
    }
    
    func primaryViewController(forExpanding splitViewController: UISplitViewController) -> UIViewController? {
        return primaryController
    }
}
