import WebKit
import Turbolinks

class TurbolinksSession: Session {
    
    var index: Int!
    
    required convenience init(_ webViewConfiguration: WKWebViewConfiguration,_ index: Int) {
        self.init(webViewConfiguration: webViewConfiguration)
        self.index = index
        self.webView.uiDelegate = self
        self.injectCookies()
    }
    
    override func reload() {
        self.injectCookies()
        super.reload()
    }
    
    fileprivate func injectCookies() {
        if #available(iOS 11.0, *) {
            guard let sharedCookies = HTTPCookieStorage.shared.cookies else { return }
            let websiteDataStore = webView.configuration.websiteDataStore
            let httpCookieStore = websiteDataStore.httpCookieStore
            for cookie in sharedCookies { httpCookieStore.setCookie(cookie) }
        }
    }
}

extension TurbolinksSession: WKUIDelegate {
    
    func webView(_ webView: WKWebView, runJavaScriptConfirmPanelWithMessage message: String, initiatedByFrame frame: WKFrameInfo, completionHandler: @escaping (Bool) -> Void) {
        let confirm = UIAlertController(title: nil, message: message, preferredStyle: .alert)
        let cancel = getUIKitLocalizedString("Cancel")
        let ok = getUIKitLocalizedString("OK")
        confirm.addAction(UIAlertAction(title: cancel, style: .cancel) { (action) in completionHandler(false) })
        confirm.addAction(UIAlertAction(title: ok, style: .default) { (action) in completionHandler(true) })
        topController.present(confirm, animated: true)
    }
    
    fileprivate func getUIKitLocalizedString(_ key: String) -> String {
        let bundle = Bundle(identifier: "com.apple.UIKit")!
        return bundle.localizedString(forKey: key, value: nil, table: nil)
    }
    
    fileprivate var topController: UIViewController {
        var topController = UIApplication.shared.keyWindow!.rootViewController!
        while (topController.presentedViewController != nil) { topController = topController.presentedViewController! }
        return topController
    }
}
