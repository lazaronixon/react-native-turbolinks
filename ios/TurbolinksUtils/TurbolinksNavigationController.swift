import WebKit
import Turbolinks

class TurbolinksNavigationController: UINavigationController {
    var session: TurbolinksSession!
    var isAtRoot: Bool { return viewControllers.count == 1 }

    required convenience init(_ manager: RNTurbolinksManager) {
        self.init()
        self.session = TurbolinksSession(setupWebView(manager))
        self.session.delegate = manager
    }

    fileprivate func setupWebView(_ manager: RNTurbolinksManager) -> WKWebViewConfiguration {
        let result = WKWebViewConfiguration()
        result.processPool = RNCWKProcessPoolManager.shared().sharedProcessPool()
        if let messageHandler = manager.messageHandler { result.userContentController.add(manager, name: messageHandler) }
        if let userAgent = manager.userAgent { result.applicationNameForUserAgent = userAgent }
        if let source = manager.injectedJavaScript { result.userContentController.addUserScript(WKUserScript(source: source, injectionTime: .atDocumentEnd, forMainFrameOnly: true)) }
        return result
    }
}
