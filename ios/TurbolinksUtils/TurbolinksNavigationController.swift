import WebKit
import Turbolinks

class TurbolinksNavigationController: UINavigationController {
    
    var session: TurbolinksSession!
    
    var isAtRoot: Bool { return viewControllers.count == 1 }
    
    required convenience init(_ manager: RNTurbolinksManager) {
        self.init()
        self.session = TurbolinksSession(setupWebView(manager))
        self.session.delegate = manager
        if let barTintColor = manager.barTintColor { navigationBar.barTintColor = barTintColor }
        if let tintColor = manager.tintColor { navigationBar.tintColor = tintColor }
    }
    
    fileprivate func setupWebView(_ manager: RNTurbolinksManager) -> WKWebViewConfiguration {
        let webConfig = WKWebViewConfiguration()
        webConfig.processPool = manager.processPool
        if (manager.messageHandler != nil) { webConfig.userContentController.add(manager, name: manager.messageHandler!) }
        if (manager.userAgent != nil) { webConfig.applicationNameForUserAgent = manager.userAgent }
        if (manager.injectedJavaScript != nil) {
            webConfig.userContentController.addUserScript(WKUserScript(source: manager.injectedJavaScript!, injectionTime: .atDocumentEnd, forMainFrameOnly: true))
        }
        return webConfig
    }
}
