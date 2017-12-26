import WebKit
import Turbolinks

@objc(RNTurbolinksManager)
class RNTurbolinksManager: RCTViewManager {
    
    fileprivate var turbolinks: RNTurbolinks!
    fileprivate var jsCodeLocation = RCTBundleURLProvider.sharedSettings().jsBundleURL(forBundleRoot: "index", fallbackResource: nil)
    
    override func view() -> UIView {
        turbolinks = RNTurbolinks()
        return turbolinks
    }
    
    override static func requiresMainQueueSetup() -> Bool {
        return true;
    }
    
    @objc func initialize() -> Void {
        DispatchQueue.main.sync {
            presentVisitableForSession(session, url:turbolinks.url)
        }
    }
    
    @objc func visit(_ routeParam: Dictionary<AnyHashable, Any>) -> Void {
        DispatchQueue.main.sync {
            let route = RCTConvert.nsDictionary(routeParam)!
            if (route["component"] != nil) {
              let component = RCTConvert.nsString(route["component"])!
              let title = RCTConvert.nsString(route["title"])!
              presentNativeView(component, title: title)
            } else {
                let url = RCTConvert.nsurl(route["url"])!
                let action = RCTConvert.nsString(route["action"])!
                presentVisitableForSession(session, url: url, action: Action.init(rawValue: action)!)
            }
        }
    }
    
    fileprivate lazy var webViewConfiguration: WKWebViewConfiguration = {
        let configuration = WKWebViewConfiguration()
        configuration.userContentController.add(self, name: turbolinks.userAgent)
        configuration.applicationNameForUserAgent = turbolinks.userAgent
        return configuration
    }()
    
    fileprivate lazy var session: Session = {
        let session = Session(webViewConfiguration: webViewConfiguration)
        session.delegate = self
        return session
    }()
    
    fileprivate func presentVisitableForSession(_ session: Session, url: URL, action: Action = .Advance) {
        let visitable = VisitableViewController(url: url)
        if action == .Advance {
            turbolinks.navigationController.pushViewController(visitable, animated: true)
        } else if action == .Replace {
            turbolinks.navigationController.popViewController(animated: false)
            turbolinks.navigationController.pushViewController(visitable, animated: false)
        }
        session.visit(visitable)
        turbolinks.navigationController.navigationBar.isTranslucent = true
    }
    
    fileprivate func presentNativeView(_ component: String, title: String) {
        let viewController = UIViewController()
        viewController.view = RCTRootView(bundleURL: jsCodeLocation, moduleName: component, initialProperties: nil, launchOptions: nil)!
        viewController.title = title
        turbolinks.navigationController.pushViewController(viewController, animated: true)
        turbolinks.navigationController.navigationBar.isTranslucent = false
    }
}

extension RNTurbolinksManager: SessionDelegate {
    func session(_ session: Session, didProposeVisitToURL URL: URL, withAction action: Action) {
        turbolinks.onVisit?(["data": ["url": URL.absoluteString, "path": URL.path, "action": action.rawValue]])
    }
    
    func session(_ session: Session, didFailRequestForVisitable visitable: Visitable, withError error: NSError) {
    }
    
    func sessionDidStartRequest(_ session: Session) {
        UIApplication.shared.isNetworkActivityIndicatorVisible = true
    }
    
    func sessionDidFinishRequest(_ session: Session) {
        UIApplication.shared.isNetworkActivityIndicatorVisible = false
    }
}

extension RNTurbolinksManager: WKScriptMessageHandler {
    func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
        if let message = message.body as? String {
            turbolinks.onMessage?(["message": message]);
        }
    }
}

