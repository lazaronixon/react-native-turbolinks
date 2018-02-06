import WebKit
import Turbolinks

class NavigationController: UINavigationController {
    
    var session: Session!
    var route: TurbolinksRoute!
    
    required convenience init(_ manager: RNTurbolinksManager,_ routeParam: Dictionary<AnyHashable, Any>?) {
        self.init()
        
        let webViewConfiguration = WKWebViewConfiguration()
        webViewConfiguration.processPool = manager.processPool
        if (manager.messageHandler != nil) { webViewConfiguration.userContentController.add(manager, name: manager.messageHandler!) }
        if (manager.userAgent != nil) { webViewConfiguration.applicationNameForUserAgent = manager.userAgent }
        
        self.session = Session(webViewConfiguration: webViewConfiguration)
        session.delegate = manager
        
        if (routeParam != nil) {
            self.route = TurbolinksRoute(route: routeParam!)
            self.tabBarItem = UITabBarItem(title: route.tabTitle , image: route.tabIcon, selectedImage: route.tabIcon)
        }    
    }
    
}
