import WebKit
import Turbolinks

@objc(RNTurbolinksManager)
class RNTurbolinksManager: RCTViewManager {
    
    fileprivate var turbolinks: RNTurbolinks!
    
    override func view() -> UIView {
        turbolinks = RNTurbolinks()
        return turbolinks
    }
    
    @objc func replaceWith(_ routeParam: Dictionary<AnyHashable, Any>) -> Void {
        let route = RCTConvert.nsDictionary(routeParam)!
        let component = RCTConvert.nsString(route["component"])
        let title = RCTConvert.nsString(route["title"])
        let props = RCTConvert.nsDictionary(route["passProps"])
        let rootView = RCTRootView(bridge: self.bridge, moduleName: component, initialProperties: props)
        let visitable = session.topmostVisitable as! CustomViewController
        visitable.customView = rootView
        visitable.customTitle = title
        visitable.renderComponent()
    }
    
    @objc func visit(_ routeParam: Dictionary<AnyHashable, Any>) -> Void {
        let route = RCTConvert.nsDictionary(routeParam)!
        let title = RCTConvert.nsString(route["title"])
        let action = RCTConvert.nsString(route["action"])
        let actionEnum = Action.init(rawValue: action ?? "advance")!
        if route["url"] != nil {
            let url = RCTConvert.nsurl(route["url"])!
            presentVisitableForSession(session, url: url, title: title, action: actionEnum)
        } else {
            let component = RCTConvert.nsString(route["component"])!
            let props = RCTConvert.nsDictionary(route["passProps"])
            presentNativeView(component, title: title, props: props, action: actionEnum)
        }
    }
    
    @objc func reloadVisitable() -> Void {
        let visitable = session.topmostVisitable as! CustomViewController
        visitable.reload()
    }
    
    @objc func reloadSession() -> Void {
        session.reload()
    }
    
    @objc func dismiss() -> Void {
        turbolinks.navigationController.dismiss(animated: true, completion: nil)
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
    
    fileprivate func presentVisitableForSession(_ session: Session, url: URL, title: String?, action: Action = .Advance) {
        let visitable = CustomViewController(url: url)
        visitable.customTitle = title
        if action == .Advance {
            turbolinks.navigationController.pushViewController(visitable, animated: true)
        } else if action == .Replace {
            turbolinks.navigationController.popViewController(animated: false)
            turbolinks.navigationController.pushViewController(visitable, animated: false)
        }
        session.visit(visitable)
    }
    
    fileprivate func presentNativeView(_ component: String, title: String?, props: Dictionary<AnyHashable, Any>?, action: Action = .Advance) {
        let viewController = UIViewController()
        viewController.view = RCTRootView(bridge: self.bridge, moduleName: component, initialProperties: props)
        viewController.title = title
        if action == .Advance {
            turbolinks.navigationController.pushViewController(viewController, animated: true)
        } else if action == .Replace {
            turbolinks.navigationController.present(viewController, animated: true, completion: nil)
        }
    }
    
    override static func requiresMainQueueSetup() -> Bool {
        return true;
    }
    
    override var methodQueue: DispatchQueue {
        get { return DispatchQueue.main }
    }
    
    override func constantsToExport() -> [AnyHashable: Any]! {
        return [
            "ErrorCode": [
                "httpFailure": ErrorCode.httpFailure.rawValue,
                "networkFailure": ErrorCode.networkFailure.rawValue,
            ],
            "Action": [
                "advance": Action.Advance.rawValue,
                "replace": Action.Replace.rawValue,
                "restore": Action.Restore.rawValue,
            ]
        ]
    }
}

extension RNTurbolinksManager: SessionDelegate {
    func session(_ session: Session, didProposeVisitToURL URL: URL, withAction action: Action) {
        turbolinks.onVisit?(["data": ["url": URL.absoluteString, "path": URL.path, "action": action.rawValue]])
    }
    
    func session(_ session: Session, didFailRequestForVisitable visitable: Visitable, withError error: NSError) {
        turbolinks.onError?(["data": ["code": error.code, "statusCode": error.userInfo["statusCode"]]])
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
        if let message = message.body as? String { turbolinks.onMessage?(["message": message]) }
    }
}
