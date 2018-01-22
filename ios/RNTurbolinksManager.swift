import WebKit
import Turbolinks

@objc(RNTurbolinksManager)
class RNTurbolinksManager: RCTEventEmitter {
    
    fileprivate var rootViewController: UIViewController {
        return UIApplication.shared.delegate!.window!!.rootViewController!
    }
    
    fileprivate lazy var webViewConfiguration: WKWebViewConfiguration = {
        return WKWebViewConfiguration()
    }()
    
    fileprivate lazy var session: Session = {
        let session = Session(webViewConfiguration: webViewConfiguration)
        session.delegate = self
        return session
    }()

    fileprivate lazy var navigation: UINavigationController = {
        let navigation = UINavigationController()
        navigation.navigationBar.isTranslucent = true
        rootViewController.view.addSubview(navigation.view)
        return navigation
    }()
    
    @objc func replaceWith(_ routeParam: Dictionary<AnyHashable, Any>) -> Void {
        let visitable = navigation.visibleViewController as! WebViewController
        let tRoute = TurbolinksRoute(route: RCTConvert.nsDictionary(routeParam))
        visitable.manager = self
        visitable.route = tRoute
        visitable.customView = RCTRootView(bridge: self.bridge, moduleName: tRoute.component, initialProperties: tRoute.passProps)
        visitable.renderComponent()
    }
    
    @objc func reloadVisitable() -> Void {
        let visitable = session.topmostVisitable as! WebViewController
        visitable.reload()
    }
    
    @objc func reloadSession() -> Void {
        let sharedCookies = HTTPCookieStorage.shared.cookies!
        let cookieScript = getJSCookiesString(sharedCookies)
        session.webView.evaluateJavaScript(cookieScript)
        session.reload()
    }
    
    @objc func dismiss() -> Void {
        reloadSession()
        navigation.dismiss(animated: true, completion: nil)
    }
    
    @objc func visit(_ routeParam: Dictionary<AnyHashable, Any>) -> Void {
        let tRoute = TurbolinksRoute(route: RCTConvert.nsDictionary(routeParam))
        if tRoute.url != nil {
            presentVisitableForSession(session, route: tRoute)
        } else {
            presentNativeView(tRoute)
        }
    }
    
    @objc func setUserAgent(_ userAgent: String) -> Void {
        webViewConfiguration.applicationNameForUserAgent = userAgent
    }
    
    @objc func setMessageHandler(_ messageHandler: String) -> Void {
        webViewConfiguration.userContentController.removeScriptMessageHandler(forName: messageHandler)
        webViewConfiguration.userContentController.add(self, name: messageHandler)
    }
    
    public func presentVisitableForSession(_ session: Session, route: TurbolinksRoute) {
        let visitable = WebViewController(url: route.url!)
        visitable.manager = self
        visitable.route = route
        if route.action == .Advance {
            navigation.pushViewController(visitable, animated: true)
        } else if route.action == .Replace {
            navigation.popViewController(animated: false)
            navigation.pushViewController(visitable, animated: false)
        }
        session.visit(visitable)
    }
    
    fileprivate func presentNativeView(_ route: TurbolinksRoute) {
        let viewController = NativeViewController()
        viewController.manager = self
        viewController.route = route
        viewController.view = RCTRootView(bridge: self.bridge, moduleName: route.component, initialProperties: route.passProps)
        if route.modal! {
            navigation.present(viewController, animated: true, completion: nil)
        } else if route.action == .Advance {
            navigation.pushViewController(viewController, animated: true)
        } else if route.action == .Replace {
            navigation.popViewController(animated: false)
            navigation.pushViewController(viewController, animated: false)
        }
    }
    
    fileprivate func getJSCookiesString(_ cookies: [HTTPCookie]) -> String {
        var result = ""
        let dateFormatter = DateFormatter()
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
    
    func handleRightButtonPress(URL: URL?, component: String?) {
        sendEvent(withName: "turbolinksRightButtonPress", body: ["url": URL?.absoluteString, "path": URL?.path, "component": component])
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
    
    override func supportedEvents() -> [String]! {
        return ["turbolinksVisit", "turbolinksMessage", "turbolinksError", "turbolinksRightButtonPress"]
    }
}

extension RNTurbolinksManager: SessionDelegate {
    func session(_ session: Session, didProposeVisitToURL URL: URL, withAction action: Action) {
        sendEvent(withName: "turbolinksVisit", body: ["url": URL.absoluteString, "path": URL.path, "action": action.rawValue])
    }
    
    func session(_ session: Session, didFailRequestForVisitable visitable: Visitable, withError error: NSError) {
        sendEvent(withName: "turbolinksError", body: ["code": error.code, "statusCode": error.userInfo["statusCode"]])
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
        if let message = message.body as? String { sendEvent(withName: "turbolinksMessage", body: message) }
    }
}
