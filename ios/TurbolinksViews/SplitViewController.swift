class SplitViewController: UISplitViewController {
    var manager: RNTurbolinksManager!
    var component: String!
    
    convenience required init(_ primaryController: UIViewController,_ secondaryController: UIViewController) {
        self.init()
        self.preferredDisplayMode = .allVisible
        self.viewControllers = [primaryController, secondaryController]
    }
}
