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
        if manager.messageHandler != nil { result.userContentController.add(manager, name: manager.messageHandler!) }
        if manager.userAgent != nil { result.applicationNameForUserAgent = manager.userAgent }
        if manager.injectedJavaScript != nil { result.userContentController.addUserScript(WKUserScript(source: manager.injectedJavaScript!, injectionTime: .atDocumentEnd, forMainFrameOnly: true)) }
        result.processPool = RNCWKProcessPoolManager.shared()!.sharedProcessPool()
        return result
    }
}
