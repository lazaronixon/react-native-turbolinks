import WebKit
import Turbolinks

class TurbolinksSession: Session {
    
    var index: Int!
    
    required convenience init(_ webViewConfiguration: WKWebViewConfiguration,_ index: Int) {
        self.init(webViewConfiguration: webViewConfiguration)
        self.index = index
    }
    
    func injectSharedCookies() {
        if #available(iOS 11.0, *) {
            guard let sharedCookies = HTTPCookieStorage.shared.cookies else { return }
            let configuration = self.webView.configuration
            let websiteDataStore = configuration.websiteDataStore
            let httpCookieStore = websiteDataStore.httpCookieStore
            for cookie in sharedCookies { httpCookieStore.setCookie(cookie) }
        }
    }
}
