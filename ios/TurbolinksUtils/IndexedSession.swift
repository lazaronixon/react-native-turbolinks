import WebKit
import Turbolinks

class IndexedSession: Session {
    
    var index: Int!
    
    required convenience init(_ webViewConfiguration: WKWebViewConfiguration,_ index: Int) {
        self.init(webViewConfiguration: webViewConfiguration)
        self.index = index
    }
    
}
