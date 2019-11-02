import WebKit
import Turbolinks

class TurbolinksSession: Session {
    required convenience init(_ webViewConfiguration: WKWebViewConfiguration) {
        self.init(webViewConfiguration: webViewConfiguration)
        self.webView.uiDelegate = self
        self.webView.allowsLinkPreview = false
    }
}

extension TurbolinksSession: WKUIDelegate {
    func webView(_ webView: WKWebView, runJavaScriptConfirmPanelWithMessage message: String, initiatedByFrame frame: WKFrameInfo, completionHandler: @escaping (Bool) -> Void) {
        let confirm = UIAlertController(title: nil, message: message, preferredStyle: .alert)
        confirm.addAction(UIAlertAction(title: getUIKitLocalizedString("Cancel"), style: .cancel) { (action) in completionHandler(false) })
        confirm.addAction(UIAlertAction(title: getUIKitLocalizedString("OK"), style: .default) { (action) in completionHandler(true) })
        topController.present(confirm, animated: true)
    }

    fileprivate func getUIKitLocalizedString(_ key: String) -> String {
        Bundle(identifier: "com.apple.UIKit")!.localizedString(forKey: key, value: nil, table: nil)
    }

    fileprivate var topController: UIViewController {
        var result = UIApplication.shared.keyWindow!.rootViewController!
        while let topController = result.presentedViewController { result = topController }
        return result
    }
}
