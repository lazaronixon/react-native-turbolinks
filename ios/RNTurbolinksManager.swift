import WebKit
import Turbolinks

@objc(RNTurbolinksManager)
class RNTurbolinksManager: RCTEventEmitter {
    
    var titleTextColor: UIColor?
    var subtitleTextColor: UIColor?
    var loadingBackgroundColor: UIColor?
    var loadingColor: UIColor?
    var messageHandler: String?
    var userAgent: String?
    var processPool = WKProcessPool()
    
    var navigation: NavigationController {
        get { return tabBarController.selectedViewController as! NavigationController }
    }
    
    var session: Session {
        get { return navigation.session }
    }
    
    fileprivate lazy var tabBarController: UITabBarController = {
        let rootViewController = UIApplication.shared.delegate!.window!!.rootViewController!
        let tabBarController = UITabBarController()
        tabBarController.tabBar.isHidden = true
        tabBarController.viewControllers = [NavigationController(self, nil)]
        rootViewController.view.addSubview(tabBarController.view)
        return tabBarController
    }()
    
    @objc func replaceWith(_ route: Dictionary<AnyHashable, Any>) -> Void {
        if let visitable = navigation.visibleViewController as? WebViewController {
            let tRoute = TurbolinksRoute(route: route)
            visitable.route = tRoute
            visitable.renderComponent()
        }
    }
    
    @objc func reloadVisitable() -> Void {
        let visitable = navigation.visibleViewController as? WebViewController
        visitable?.reload()
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
    
    @objc func visit(_ route: Dictionary<AnyHashable, Any>) -> Void {
        let tRoute = TurbolinksRoute(route: route)
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
        navigation.isNavigationBarHidden  = navigationBarHidden
    }
    
    @objc func setUserAgent(_ userAgent: String) -> Void {
        self.userAgent = userAgent
    }
    
    @objc func setMessageHandler(_ handler: String) -> Void {
        self.messageHandler = handler
    }
    
    @objc func renderTitle(_ title: String,_ subtitle: String) {
        let visitable = navigation.visibleViewController as! GenricViewController
        visitable.route.title = title
        visitable.route.subtitle = subtitle
        visitable.renderTitle()
    }
    
    @objc func renderActions(_ actions: Array<Dictionary<AnyHashable, Any>>) {
        let visitable = navigation.visibleViewController as! GenricViewController
        visitable.route.actions = actions
        visitable.renderActions()
    }
    
    @objc func setTabBar(_ tabBarParam: Dictionary<AnyHashable, Any>) {
        tabBarController.viewControllers = nil
        let routes = RCTConvert.nsDictionaryArray(tabBarParam["routes"])!
        for (index, route) in routes.enumerated() {
            let viewController = NavigationController(self, route)
            tabBarController.viewControllers!.append(viewController)
            tabBarController.selectedIndex = index
            visit(route)
        }
        tabBarController.selectedIndex = RCTConvert.nsInteger(tabBarParam["selectedIndex"])
        tabBarController.tabBar.isHidden = false
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
