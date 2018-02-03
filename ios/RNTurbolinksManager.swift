import WebKit
import Turbolinks

@objc(RNTurbolinksManager)
class RNTurbolinksManager: RCTEventEmitter {
    
    var titleTextColor: UIColor?
    var subtitleTextColor: UIColor?
    var loadingBackgroundColor: UIColor?
    var loadingColor: UIColor?
    
    fileprivate lazy var navigation: UINavigationController = {
        let rootViewController = UIApplication.shared.delegate!.window!!.rootViewController!
        let navigation = UINavigationController()
        navigation.navigationBar.isTranslucent = true
        rootViewController.view.addSubview(navigation.view)
        return navigation
    }()
    
    fileprivate lazy var webViewConfiguration: WKWebViewConfiguration = {
        return WKWebViewConfiguration()
    }()
    
    fileprivate lazy var session: Session = {
        let session = Session(webViewConfiguration: webViewConfiguration)
        session.delegate = self
        return session
    }()
    
    @objc func replaceWith(_ routeParam: Dictionary<AnyHashable, Any>) -> Void {
        let tRoute = TurbolinksRoute(route: routeParam)
        let visitable = navigation.visibleViewController as! WebViewController
        visitable.route = tRoute
        visitable.renderComponent()
    }
    
    @objc func reloadVisitable() -> Void {
        let visitable = navigation.visibleViewController as! WebViewController
        visitable.reload()
    }
    
    @objc func reloadSession() -> Void {
        let sharedCookies = HTTPCookieStorage.shared.cookies!
        let cookieScript = TurbolinksHelper.getJSCookiesString(sharedCookies)
        session.webView.evaluateJavaScript(cookieScript)
        session.reload()
    }
    
    @objc func dismiss() -> Void {
        reloadSession()
        navigation.dismiss(animated: true, completion: nil)
    }
    
    @objc func back() -> Void {
        navigation.popViewController(animated: true)
    }
    
    @objc func visit(_ routeParam: Dictionary<AnyHashable, Any>) -> Void {
        let tRoute = TurbolinksRoute(route: routeParam)
        if tRoute.url != nil {
            presentVisitableForSession(session, route: tRoute)
        } else {
            presentNativeView(tRoute)
        }
    }
    
    @objc func setLoadingStyle(_ style: Dictionary<AnyHashable, Any>) {
        self.loadingColor = RCTConvert.uiColor(style["color"])
        self.loadingBackgroundColor = RCTConvert.uiColor(style["backgroundColor"])
    }
    
    @objc func setNavigationBarStyle(_ style: Dictionary<AnyHashable, Any>) -> Void {
        self.titleTextColor = RCTConvert.uiColor(style["titleTextColor"])
        self.subtitleTextColor = RCTConvert.uiColor(style["subtitleTextColor"])
        let barTintColor = RCTConvert.uiColor(style["barTintColor"])
        let tintColor = RCTConvert.uiColor(style["tintColor"])
        if barTintColor != nil { self.navigation.navigationBar.barTintColor = barTintColor }
        if tintColor != nil { self.navigation.navigationBar.tintColor = tintColor }
    }
    
    @objc func setNavigationBarHidden(_ navigationBarHidden: Bool) -> Void {
        self.navigation.isNavigationBarHidden  = navigationBarHidden
    }
    
    @objc func setUserAgent(_ userAgent: String) -> Void {
        webViewConfiguration.applicationNameForUserAgent = userAgent
    }
    
    @objc func setMessageHandler(_ handler: String) -> Void {
        webViewConfiguration.userContentController.removeScriptMessageHandler(forName: handler)
        webViewConfiguration.userContentController.add(self, name: handler)
    }
    
    fileprivate func presentVisitableForSession(_ session: Session, route: TurbolinksRoute) {
        let visitable = WebViewController(manager: self, route: route)        
        if route.action == .Advance {
            navigation.pushViewController(visitable, animated: true)
        } else if route.action == .Replace {
            navigation.popViewController(animated: false)
            navigation.pushViewController(visitable, animated: false)
        }
        session.visit(visitable)
    }
    
    fileprivate func presentNativeView(_ route: TurbolinksRoute) {
        let viewController = NativeViewController(manager: self, route: route)
        if route.modal! {
            navigation.present(viewController, animated: true, completion: nil)
        } else if route.action == .Advance {
            navigation.pushViewController(viewController, animated: true)
        } else if route.action == .Replace {
            navigation.popViewController(animated: false)
            navigation.pushViewController(viewController, animated: false)
        }
    }    
    
    func handleTitlePress(URL: URL?, component: String?) {
        sendEvent(withName: "turbolinksTitlePress", body: ["url": URL?.absoluteString, "path": URL?.path, "component": component])
    }
    
    func handleActionPress(actionId: Int) {
        sendEvent(withName: "turbolinksActionPress", body: actionId)
    }
    
    func handleLeftButtonPress(URL: URL?, component: String?) {
        sendEvent(withName: "turbolinksLeftButtonPress", body: ["url": URL?.absoluteString, "path": URL?.path, "component": component])
    }
    
    func handleVisitCompleted(url: URL!, source: String?) {
        sendEvent(withName: "turbolinksVisitCompleted", body: ["url": url.absoluteString, "path": url.path, "source": source])
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
        return ["turbolinksVisit", "turbolinksMessage", "turbolinksError", "turbolinksTitlePress", "turbolinksActionPress", "turbolinksLeftButtonPress", "turbolinksVisitCompleted"]
    }
}

extension RNTurbolinksManager: SessionDelegate {
    func session(_ session: Session, didProposeVisitToURL URL: URL, withAction action: Action) {
        sendEvent(withName: "turbolinksVisit", body: ["url": URL.absoluteString, "path": URL.path, "action": action.rawValue])
    }
    
    func session(_ session: Session, didFailRequestForVisitable visitable: Visitable, withError error: NSError) {
        sendEvent(withName: "turbolinksError", body: ["code": error.code, "statusCode": error.userInfo["statusCode"], "description": error.localizedDescription])
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
