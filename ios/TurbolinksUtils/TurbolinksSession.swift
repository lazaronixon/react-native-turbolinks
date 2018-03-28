import WebKit
import Turbolinks

class TurbolinksSession: Session {
    
    var index: Int!
    fileprivate var webViewCookie: WKWebView!
    
    required convenience init(_ webViewConfiguration: WKWebViewConfiguration,_ index: Int) {
        self.init(webViewConfiguration: webViewConfiguration)
        self.index = index
        self.webView.uiDelegate = self
        self.webViewCookie = WKWebView(frame: .zero, configuration: webViewConfiguration)
    }
    
    func cleanCookies() {
        var isCleaning = true
        let dateFrom = Date(timeIntervalSince1970: 0)
        let dataTypes = Set<String>([WKWebsiteDataTypeCookies])
        let dataStore = self.webView.configuration.websiteDataStore
        dataStore.removeData(ofTypes: dataTypes, modifiedSince: dateFrom) { isCleaning = false }
        while isCleaning { RunLoop.main.run(mode: .defaultRunLoopMode, before: .distantFuture) }
    }
    
    func injectCookies() {
        var isInjecting = true
        guard let url = topmostVisitable?.visitableURL else { return }
        guard let sharedCookies = HTTPCookieStorage.shared.cookies(for: url) else { return }
        webViewCookie.loadHTMLString("<html><body></body></html>", baseURL: url)
        while webViewCookie.isLoading { RunLoop.main.run(mode: .defaultRunLoopMode, before: .distantFuture) }
        webViewCookie.evaluateJavaScript(getJSCookie(sharedCookies)){ (r, e) in isInjecting = false }
        while isInjecting { RunLoop.main.run(mode: .defaultRunLoopMode, before: .distantFuture) }
    }
    
    fileprivate func getJSCookie(_ cookies: [HTTPCookie]) -> String {
        var result = ""
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US")
        dateFormatter.timeZone = TimeZone(abbreviation: "UTC")
        dateFormatter.dateFormat = "EEE, d MMM yyyy HH:mm:ss zzz"
        for cookie in cookies {
            result += "document.cookie='\(cookie.name)=\(cookie.value); domain=\(cookie.domain); path=\(cookie.path); "
            if let date = cookie.expiresDate { result += "expires=\(dateFormatter.string(from: date)); " }
            if (cookie.isSecure) { result += "secure; " }
            result += "'; "
        }
        return result
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
