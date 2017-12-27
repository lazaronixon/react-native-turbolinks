import WebKit
import Turbolinks

@objc(RNTurbolinksManager)
class RNTurbolinksManager: RCTViewManager {
    
    fileprivate var turbolinks: RNTurbolinks!
    
    override func view() -> UIView {
        turbolinks = RNTurbolinks()
        return turbolinks
    }
    
    override static func requiresMainQueueSetup() -> Bool {
        return true;
    }
    
    override var methodQueue: DispatchQueue {
        get { return DispatchQueue.main }
    }
    
    @objc func present(_ routeParam: Dictionary<AnyHashable, Any>) -> Void {
        let route = RCTConvert.nsDictionary(routeParam)!
        let component = RCTConvert.nsString(route["component"])!
        let props = RCTConvert.nsDictionary(route["passProps"]) ?? Dictionary()
        let customViewController = session.topmostVisitable as! CustomViewController
        let rootView = RCTRootView(bridge: self.bridge, moduleName: component, initialProperties: props)!
        customViewController.renderComponent(rootView)
    }
    
    @objc func visit(_ routeParam: Dictionary<AnyHashable, Any>) -> Void {
        let route = RCTConvert.nsDictionary(routeParam)!
        if (route["url"] != nil) {
          let url = RCTConvert.nsurl(route["url"])!
          let action = RCTConvert.nsString(route["action"]) ?? Action.Advance.rawValue
          let actionEnum = Action.init(rawValue: action)!
          presentVisitableForSession(session, url: url, action: actionEnum)
        } else {
            let component = RCTConvert.nsString(route["component"])!
            let title = RCTConvert.nsString(route["title"]) ?? ""
            let props = RCTConvert.nsDictionary(route["passProps"]) ?? Dictionary()
            presentNativeView(component, title: title, props: props)
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
        let visitable = CustomViewController(url: url)
        if action == .Advance {
            turbolinks.navigationController.pushViewController(visitable, animated: true)
        } else if action == .Replace {
            turbolinks.navigationController.popViewController(animated: false)
            turbolinks.navigationController.pushViewController(visitable, animated: false)
        }
        session.visit(visitable)
    }
    
    fileprivate func presentNativeView(_ component: String, title: String, props: Dictionary<AnyHashable, Any>) {
        let viewController = UIViewController()
        viewController.view = RCTRootView(bridge: self.bridge, moduleName: component, initialProperties: props)
        viewController.title = title
        turbolinks.navigationController.pushViewController(viewController, animated: true)
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
        if let message = message.body as? String {
            turbolinks.onMessage?(["message": message]);
        }
    }
}
