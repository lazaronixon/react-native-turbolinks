class SplitViewController: UISplitViewController {
    var manager: RNTurbolinksManager!
    var primaryController: UIViewController!
    var secondaryController: UIViewController!
    var toolbarIcon: UIImage { UIImage(named: "toolbar", in: manager.turbolinksBundle, compatibleWith: nil)! }
        
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
    
    override var displayModeButtonItem: UIBarButtonItem {
        get {
            let icon = isCollapsed ? nil : toolbarIcon
            let originalButton = super.displayModeButtonItem
            return UIBarButtonItem(image: icon, style: .plain, target: originalButton.target, action: originalButton.action)
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
