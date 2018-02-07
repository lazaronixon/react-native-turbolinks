import WebKit
import Turbolinks

class NavigationController: UINavigationController {
    
    var session: Session!
    
    required convenience init(_ manager: RNTurbolinksManager,_ route: Dictionary<AnyHashable, Any>?) {
        self.init()
        
        let webViewConfiguration = WKWebViewConfiguration()
        webViewConfiguration.processPool = manager.processPool
        if (manager.messageHandler != nil) { webViewConfiguration.userContentController.add(manager, name: manager.messageHandler!) }
        if (manager.userAgent != nil) { webViewConfiguration.applicationNameForUserAgent = manager.userAgent }
        
        self.session = Session(webViewConfiguration: webViewConfiguration)
        self.session.delegate = manager
        
        if (route != nil) {
            let tRoute = TurbolinksRoute(route!)
            self.tabBarItem = UITabBarItem(title: tRoute.tabTitle , image: tRoute.tabIcon, selectedImage: tRoute.tabIcon)
        }
    }
}
