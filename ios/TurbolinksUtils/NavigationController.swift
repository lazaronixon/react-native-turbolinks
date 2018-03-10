import WebKit
import Turbolinks

class NavigationController: UINavigationController {
    
    var session: TurbolinksSession!
    var index: Int!
    
    required convenience init(_ manager: RNTurbolinksManager,_ tabIndex: Int) {
        self.init()
        self.index = tabIndex
        self.session = TurbolinksSession(setupWebView(manager), tabIndex)
        self.session.delegate = manager
        if manager.barTintColor != nil { navigationBar.barTintColor = manager.barTintColor }
        if manager.tintColor != nil { navigationBar.tintColor = manager.tintColor }
    }
    
    required convenience init(_ manager: RNTurbolinksManager,_ route: Dictionary<AnyHashable, Any>,_ tabIndex: Int) {
        self.init(manager, tabIndex)
        let tRoute = TurbolinksRoute(route)
        self.tabBarItem = UITabBarItem(title: tRoute.tabTitle , image: tRoute.tabIcon, selectedImage: tRoute.tabIcon)
    }
    
    fileprivate func setupWebView(_ manager: RNTurbolinksManager) -> WKWebViewConfiguration {
        let webConfig = WKWebViewConfiguration()
        if (manager.messageHandler != nil) { webConfig.userContentController.add(manager, name: manager.messageHandler!) }
        if (manager.userAgent != nil) { webConfig.applicationNameForUserAgent = manager.userAgent }
        webConfig.processPool = manager.processPool
        return webConfig
    }
}
