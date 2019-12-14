import WebKit
import Turbolinks

@objc(RNTurbolinksManager)
class RNTurbolinksManager: RCTEventEmitter {
    var splitViewController: UISplitViewController?
    var navigationController: TurbolinksNavigationController!
    var messageHandler: String?
    var injectedJavaScript: String?
    var userAgent: String?
    var menuIcon: UIImage?
    var loadingView: String?
    
    var turbolinksBundle: Bundle! {
        return Bundle(path: Bundle.main.path(forResource: "RNTurbolinks", ofType: "bundle")!)
    }
    
    fileprivate var application: UIApplication {
        return UIApplication.shared
    }
    
    fileprivate var rootViewController: UIViewController {
        return application.keyWindow!.rootViewController!
    }
    
    fileprivate var session: TurbolinksSession {
        return navigationController.session
    }
    
    fileprivate var visibleViewController: UIViewController {
        return navigationController.visibleViewController!
    }
    
    @objc func replaceWith(_ route: Dictionary<AnyHashable, Any>) {
        (navigationController.visibleViewController as! WebViewController).renderComponent(TurbolinksRoute(route))
    }
    
    @objc func reloadVisitable() {
        (visibleViewController as! WebViewController).reload()
    }
    
    @objc func reloadSession() {
        session.injectSharedCookies()
        session.reload()
    }
    
    @objc func removeAllCookies() {
        removeSharedCookies()
        removeWKWebViewCookies()
    }
    
    @objc func dismiss(_ animated: Bool) {
        navigationController.dismiss(animated: animated)
    }
    
    @objc func popToRoot(_ animated: Bool) {
        navigationController.popToRootViewController(animated: animated)
    }
    
    @objc func startSingleScreenApp(_ route: Dictionary<AnyHashable, Any>,_ options: Dictionary<AnyHashable, Any>) {
        setAppOptions(options)
        navigationController = TurbolinksNavigationController(self)
        addToRootViewController(navigationController)
        
        visit(route)
    }
    
    @objc func startSplitScreenApp(_ primaryComponent: String,_ secondaryRoute: Dictionary<AnyHashable, Any>,_ options: Dictionary<AnyHashable, Any>) {
        setAppOptions(options)
        navigationController = TurbolinksNavigationController(self)
        splitViewController = SplitViewController(self, primaryComponent, navigationController)
        addToRootViewController(splitViewController!)
        
        visit(secondaryRoute)
    }
    
    @objc func visit(_ route: Dictionary<AnyHashable, Any>) {
        if route["url"] != nil {
            presentVisitableForSession(TurbolinksRoute(route))
        } else {
            presentNativeView(TurbolinksRoute(route))
        }
    }
    
    @objc func renderNavBarStyle(_ style: Dictionary<AnyHashable, Any>) {
        setNavBarStyle(style)
        refreshAppearence()
    }
    
    @objc func renderTitle(_ title: String,_ subtitle: String) {
        guard let visitable = navigationController.visibleViewController as? ApplicationViewController else { return }
        visitable.route.title = title
        visitable.renderTitle()
    }
    
    @objc func renderActions(_ actions: Array<Dictionary<AnyHashable, Any>>) {
        guard let visitable = navigationController.visibleViewController as? ApplicationViewController else { return }
        visitable.route.actions = actions
        visitable.renderActions()
    }
    
    @objc func injectJavaScript(_ script: String) {
        session.webView.evaluateJavaScript(script)
    }
    
    fileprivate func presentVisitableForSession(_ route: TurbolinksRoute) {
        let visitable = WebViewController(self, route)
        if route.action == .Advance {
            navigationController.pushViewController(visitable, animated: true)
        } else if route.action == .Replace {
            if navigationController.isAtRoot {
                navigationController.setViewControllers([visitable], animated: false)
            } else {
                navigationController.popViewController(animated: false)
                navigationController.pushViewController(visitable, animated: false)
            }
        }
        session.visit(visitable)
    }    
    
    fileprivate func presentNativeView(_ route: TurbolinksRoute) {
        let viewController = NativeViewController(self, route)
        if route.modal {
            navigationController.present(viewController, animated: true)
        } else if route.action == .Advance {
            navigationController.pushViewController(viewController, animated: true)
        } else if route.action == .Replace {
            if navigationController.isAtRoot {
                navigationController.setViewControllers([viewController], animated: false)
            } else {
                navigationController.popViewController(animated: false)
                navigationController.pushViewController(viewController, animated: false)
            }
        }
    }
    
    fileprivate func setAppOptions(_ options: Dictionary<AnyHashable, Any>) {
        userAgent = RCTConvert.nsString(options["userAgent"])
        messageHandler = RCTConvert.nsString(options["messageHandler"])
        loadingView = RCTConvert.nsString(options["loadingView"])
        injectedJavaScript = RCTConvert.nsString(options["injectedJavaScript"])
        if options["navBarStyle"] != nil { setNavBarStyle(RCTConvert.nsDictionary(options["navBarStyle"])) }
    }
    
    fileprivate func setNavBarStyle(_ style: Dictionary<AnyHashable, Any>) {
        UINavigationBar.appearance().barTintColor = RCTConvert.uiColor(style["barTintColor"])
        UINavigationBar.appearance().tintColor = RCTConvert.uiColor(style["tintColor"])
        UINavigationBar.appearance().titleTextAttributes = [.foregroundColor : RCTConvert.uiColor(style["titleTextColor"]) ?? .black]
        menuIcon = RCTConvert.uiImage(style["menuIcon"])
    }
    
    fileprivate func refreshAppearence() {
        let rootView = rootViewController.view!
        let superView = rootViewController.view.superview!
        rootView.removeFromSuperview()
        superView.addSubview(rootView)
    }
    
    fileprivate func addToRootViewController(_ viewController: UIViewController) {
        rootViewController.dismiss(animated: false)
        rootViewController.addChild(viewController)
        rootViewController.view.addSubview(viewController.view)
    }
        
    fileprivate func removeSharedCookies() {
        HTTPCookieStorage.shared.removeCookies(since: Date.distantPast)
    }
    
    fileprivate func removeWKWebViewCookies() {
        WKWebsiteDataStore.default().removeData(ofTypes: [WKWebsiteDataTypeCookies], modifiedSince: Date.distantPast, completionHandler: {})
    }
    
    func handleActionPress(_ URL: URL?,_ component: String?, _ actionId: Int) {
        sendEvent(withName: "turbolinksActionPress", body: ["url": URL?.absoluteString, "path": URL?.path, "component": component, "actionId": actionId])
    }
    
    func handleLeftButtonPress(_ URL: URL?,_ component: String?) {
        sendEvent(withName: "turbolinksLeftButtonPress", body: ["url": URL?.absoluteString, "path": URL?.path, "component": component])
    }
    
    func handleRightButtonPress(_ URL: URL?,_ component: String?) {
        sendEvent(withName: "turbolinksRightButtonPress", body: ["url": URL?.absoluteString, "path": URL?.path, "component": component])
    }
    
    func handleVisitCompleted(_ URL: URL) {
        sendEvent(withName: "turbolinksVisitCompleted", body: ["url": URL.absoluteString, "path": URL.path])
    }
    
    override static func requiresMainQueueSetup() -> Bool {
        return true;
    }
    
    override var methodQueue: DispatchQueue {
        return DispatchQueue.main
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
        return ["turbolinksVisit", "turbolinksMessage", "turbolinksError", "turbolinksActionPress", "turbolinksLeftButtonPress", "turbolinksRightButtonPress", "turbolinksVisitCompleted"]
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
        application.isNetworkActivityIndicatorVisible = true
    }
    
    func sessionDidFinishRequest(_ session: Session) {
        application.isNetworkActivityIndicatorVisible = false
    }
}

extension RNTurbolinksManager: WKScriptMessageHandler {
    func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
        if let message = message.body as? String { sendEvent(withName: "turbolinksMessage", body: message) }
    }
}
