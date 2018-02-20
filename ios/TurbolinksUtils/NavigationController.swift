import WebKit
import Turbolinks

class NavigationController: UINavigationController {
    
    var session: TurbolinksSession!
    
    required convenience init(_ manager: RNTurbolinksManager,_ tabIndex: Int) {
        self.init()
        self.session = TurbolinksSession(NavigationController.setupWebViewConfiguration(manager), tabIndex)
        self.session.delegate = manager
        self.session.webView.uiDelegate = self
        self.session.injectSharedCookies()
    }
    
    required convenience init(_ manager: RNTurbolinksManager,_ route: Dictionary<AnyHashable, Any>,_ tabIndex: Int) {
        self.init(manager, tabIndex)
        let tRoute = TurbolinksRoute(route)
        self.tabBarItem = UITabBarItem(title: tRoute.tabTitle , image: tRoute.tabIcon, selectedImage: tRoute.tabIcon)
    }
    
    fileprivate static func setupWebViewConfiguration(_ manager: RNTurbolinksManager) -> WKWebViewConfiguration {
        let webViewConfiguration = WKWebViewConfiguration()
        webViewConfiguration.processPool = manager.processPool
        if (manager.messageHandler != nil) { webViewConfiguration.userContentController.add(manager, name: manager.messageHandler!) }
        if (manager.userAgent != nil) { webViewConfiguration.applicationNameForUserAgent = manager.userAgent }
        return webViewConfiguration
    }
    
}

extension NavigationController: WKUIDelegate {
    func webView(_ webView: WKWebView, runJavaScriptConfirmPanelWithMessage message: String, initiatedByFrame frame: WKFrameInfo, completionHandler: @escaping (Bool) -> Void) {
        let confirm = UIAlertController(title: nil, message: message, preferredStyle: .alert)
        let cancel = TurbolinksHelper.getUIKitLocalizedString("Cancel")
        let ok = TurbolinksHelper.getUIKitLocalizedString("OK")
        
        confirm.addAction(UIAlertAction(title: cancel, style: .cancel) { (action) in
            completionHandler(false)
        })
        
        confirm.addAction(UIAlertAction(title: ok, style: .default) { (action) in
            completionHandler(true)
        })
        
        self.present(confirm, animated: true, completion: nil)
    }
}
