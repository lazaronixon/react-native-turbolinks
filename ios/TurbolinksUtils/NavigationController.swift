import WebKit
import Turbolinks

class NavigationController: UINavigationController {
    
    var session: TurbolinksSession!
    var index: Int!
    
    var isAtRoot: Bool { return viewControllers.count == 1 }
    
    required convenience init(_ manager: RNTurbolinksManager,_ route: Dictionary<AnyHashable, Any>,_ tabIndex: Int) {
        let tRoute = TurbolinksRoute(route)
        self.init()
        self.index = tabIndex
        self.session = TurbolinksSession(setupWebView(manager), tabIndex)
        self.session.delegate = manager
        if let barTintColor = manager.barTintColor { navigationBar.barTintColor = barTintColor }
        if let tintColor = manager.tintColor { navigationBar.tintColor = tintColor }
        self.tabBarItem = UITabBarItem(title: tRoute.tabTitle , image: tRoute.tabIcon, selectedImage: tRoute.tabIcon)
        self.tabBarItem.badgeValue = tRoute.tabBadge
    }
    
    fileprivate func setupWebView(_ manager: RNTurbolinksManager) -> WKWebViewConfiguration {
        let webConfig = WKWebViewConfiguration()
        if (manager.messageHandler != nil) { webConfig.userContentController.add(manager, name: manager.messageHandler!) }
        if (manager.userAgent != nil) { webConfig.applicationNameForUserAgent = manager.userAgent }
        webConfig.processPool = manager.processPool
        return webConfig
    }
}
