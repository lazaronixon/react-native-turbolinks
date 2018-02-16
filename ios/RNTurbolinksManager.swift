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
    
    fileprivate var navigation: NavigationController {
        get { return tabBarController.selectedViewController as! NavigationController }
    }
    
    fileprivate var session: Session {
        get { return navigation.session }
    }
    
    fileprivate lazy var tabBarController: UITabBarController = {
        let tabBarController = UITabBarController()
        tabBarController.tabBar.isHidden = true
        tabBarController.viewControllers = [NavigationController(self)]
        UIApplication.topViewController().present(tabBarController, animated: false, completion: nil)
        return tabBarController
    }()
    
    @objc func replaceWith(_ route: Dictionary<AnyHashable, Any>) -> Void {
        if let visitable = navigation.visibleViewController as? WebViewController {
            let tRoute = TurbolinksRoute(route)
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
        navigation.dismiss(animated: true, completion: nil)
    }
    
    @objc func back() -> Void {
        navigation.popViewController(animated: true)
    }
    
    @objc func visit(_ route: Dictionary<AnyHashable, Any>) -> Void {
        let tRoute = TurbolinksRoute(route)
        if tRoute.url != nil {
            presentVisitableForSession(session, tRoute)
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
    
    @objc func setTabBarStyle(_ style: Dictionary<AnyHashable, Any>) -> Void {
        let barTintColor = RCTConvert.uiColor(style["barTintColor"])
        let tintColor = RCTConvert.uiColor(style["tintColor"])
        if barTintColor != nil { self.tabBarController.tabBar.barTintColor = barTintColor }
        if tintColor != nil { self.tabBarController.tabBar.tintColor = tintColor }
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
        let selectedIndex = RCTConvert.nsInteger(tabBarParam["selectedIndex"])
        let routes = RCTConvert.nsDictionaryArray(tabBarParam["routes"])!
        tabBarController.viewControllers = nil
        tabBarController.tabBar.isHidden = false
        for (index, route) in routes.enumerated() {
            tabBarController.viewControllers!.append(NavigationController(self, route))
            tabBarController.selectedIndex = index
            visit(route)
        }
        tabBarController.selectedIndex = selectedIndex
    }
    
    fileprivate func presentVisitableForSession(_ session: Session,_ route: TurbolinksRoute) {
        let visitable = WebViewController(self, route)
        if route.action == .Advance {
            navigation.pushViewController(visitable, animated: true)
        } else if route.action == .Replace {
            navigation.popViewController(animated: false)
            navigation.pushViewController(visitable, animated: false)
        }
        session.visit(visitable)
    }
    
    fileprivate func presentNativeView(_ route: TurbolinksRoute) {
        let viewController = NativeViewController(self, route)
        if route.modal! {
            navigation.present(viewController, animated: true, completion: nil)
        } else if route.action == .Advance {
            navigation.pushViewController(viewController, animated: true)
        } else if route.action == .Replace {
            navigation.popViewController(animated: false)
            navigation.pushViewController(viewController, animated: false)
        }
    }
    
    func handleTitlePress(_ URL: URL?,_ component: String?) {
        sendEvent(withName: "turbolinksTitlePress", body: ["url": URL?.absoluteString, "path": URL?.path, "component": component])
    }
    
    func handleActionPress(_ actionId: Int) {
        sendEvent(withName: "turbolinksActionPress", body: actionId)
    }
    
    func handleLeftButtonPress(_ URL: URL?,_ component: String?) {
        sendEvent(withName: "turbolinksLeftButtonPress", body: ["url": URL?.absoluteString, "path": URL?.path, "component": component])
    }
    
    func handleVisitCompleted(_ url: URL!,_ source: String?) {
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

extension UIApplication {
    
    fileprivate class var rootViewController : UIViewController {
        get { return UIApplication.shared.keyWindow!.rootViewController! }
    }
    
    class func topViewController(base: UIViewController! = rootViewController) -> UIViewController {
        if let presented = base.presentedViewController {
            return topViewController(base: presented)
        }
        return base
    }
}
