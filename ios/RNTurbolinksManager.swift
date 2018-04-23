import WebKit
import Turbolinks

@objc(RNTurbolinksManager)
class RNTurbolinksManager: RCTEventEmitter {
    
    var tabBarController: UITabBarController!
    var navigationController: NavigationController!
    var titleTextColor: UIColor?
    var subtitleTextColor: UIColor?
    var barTintColor: UIColor?
    var tintColor: UIColor?
    var tabBarBarTintColor: UIColor?
    var tabBarTintColor: UIColor?
    var messageHandler: String?
    var userAgent: String?
    var customMenuIcon: UIImage?
    var loadingView: String?    
    var processPool = WKProcessPool()
    
    fileprivate var application: UIApplication {
        return UIApplication.shared
    }
    
    fileprivate var rootViewController: UIViewController {
        return application.keyWindow!.rootViewController!
    }
    
    fileprivate var navigation: NavigationController {
        if (navigationController != nil) {
            return navigationController
        } else {
            return tabBarController.selectedViewController as! NavigationController
        }
    }
    
    fileprivate var session: TurbolinksSession {
        return navigation.session
    }
    
    fileprivate var visibleViewController: UIViewController {
        return navigation.visibleViewController!
    }
    
    @objc func replaceWith(_ route: Dictionary<AnyHashable, Any>,_ tabIndex: Int) {
        let view = tabIndex != -1 ? getViewControllerByIndex(tabIndex) : visibleViewController
        let visitable = view as! WebViewController
        visitable.renderComponent(TurbolinksRoute(route))
    }
    
    @objc func reloadVisitable() {
        let visitable = visibleViewController as! WebViewController
        visitable.reload()
    }
    
    @objc func reloadSession() {
        session.cleanCookies()
        session.injectCookies()
        session.reload()
    }
    
    @objc func dismiss() {
        navigation.dismiss(animated: true)
    }
    
    @objc func popToRoot() {
        navigation.popToRootViewController(animated: true)
    }
    
    @objc func back() {
        navigation.popViewController(animated: true)
    }
    
    @objc func startSingleScreenApp(_ route: Dictionary<AnyHashable, Any>,_ options: Dictionary<AnyHashable, Any>) {
        setAppOptions(options)
        navigationController = NavigationController(self, route, 0)
        rootViewController.addChildViewController(navigationController)
        rootViewController.view.addSubview(navigationController.view)
        visit(route)
    }
    
    @objc func startTabBasedApp(_ routes: Array<Dictionary<AnyHashable, Any>> ,_ options: Dictionary<AnyHashable, Any> ,_ selectedIndex: Int) {
        setAppOptions(options)
        tabBarController = UITabBarController()
        tabBarController.viewControllers = routes.enumerated().map { (index, route) in NavigationController(self, route, index) }
        if tabBarBarTintColor != nil { tabBarController.tabBar.barTintColor = tabBarBarTintColor }
        if tabBarTintColor != nil { tabBarController.tabBar.tintColor = tabBarTintColor }
        rootViewController.addChildViewController(tabBarController)
        rootViewController.view.addSubview(tabBarController.view)
        visitTabRoutes(routes)
        tabBarController.selectedIndex = selectedIndex
    }
    
    @objc func visit(_ route: Dictionary<AnyHashable, Any>) {
        let tRoute = TurbolinksRoute(route)
        if tRoute.url != nil {
            presentVisitableForSession(tRoute)
        } else {
            presentNativeView(tRoute)
        }
    }
    
    @objc func renderTitle(_ title: String,_ subtitle: String,_ tabIndex: Int) {
        let view = tabIndex != -1 ? getViewControllerByIndex(tabIndex) : visibleViewController
        guard let visitable = view as? GenricViewController else { return }
        visitable.route.title = title
        visitable.route.subtitle = subtitle
        visitable.renderTitle()
    }
    
    @objc func renderActions(_ actions: Array<Dictionary<AnyHashable, Any>>,_ tabIndex: Int) {
        let view = tabIndex != -1 ? getViewControllerByIndex(tabIndex) : visibleViewController
        guard let visitable = view as? GenricViewController else { return }
        visitable.route.actions = actions
        visitable.renderActions()
    }
    
    @objc func evaluateJavaScript(_ script: String, _ tabIndex: Int,_ resolve: @escaping RCTPromiseResolveBlock,_ reject: @escaping RCTPromiseRejectBlock) {
        let nav = tabIndex != -1 ? getNavigationByIndex(tabIndex) : navigation
        nav.session.webView.evaluateJavaScript(script) {(result, error) in
            if error != nil {
                reject("js_error", error!.localizedDescription, error)
            } else {
                resolve(result)
            }
        }
    }
    
    @objc func notifyTabItem(_ tabIndex: Int, _ enabled: Bool) {
        let tabItem = tabBarController.tabBar.items![tabIndex]
        if (enabled) {
            tabItem.badgeValue = ""
        } else {
            tabItem.badgeValue = nil
        }
    }
    
    fileprivate func presentVisitableForSession(_ route: TurbolinksRoute) {
        let visitable = WebViewController(self, route)
        if route.action == .Advance {
            navigation.pushViewController(visitable, animated: true)
        } else if route.action == .Replace {
            if navigation.isAtRoot {
                navigation.setViewControllers([visitable], animated: false)
            } else {
                navigation.popViewController(animated: false)
                navigation.pushViewController(visitable, animated: false)
            }
        }
        session.visit(visitable)
    }
    
    fileprivate func presentNativeView(_ route: TurbolinksRoute) {
        let viewController = NativeViewController(self, route)
        if route.modal {
            navigation.present(viewController, animated: true)
        } else if route.action == .Advance {
            navigation.pushViewController(viewController, animated: true)
        } else if route.action == .Replace {
            if navigation.isAtRoot {
                navigation.setViewControllers([viewController], animated: false)
            } else {
                navigation.popViewController(animated: false)
                navigation.pushViewController(viewController, animated: false)
            }
        }
    }
    
    fileprivate func getViewControllerByIndex(_ index: Int) -> UIViewController {
        let navController = tabBarController.viewControllers![index] as! NavigationController
        return navController.visibleViewController!
    }
    
    fileprivate func getNavigationByIndex(_ index: Int) -> NavigationController {
        return tabBarController.viewControllers![index] as! NavigationController
    }
    
    fileprivate func setAppOptions(_ options: Dictionary<AnyHashable, Any>) {
        self.userAgent = RCTConvert.nsString(options["userAgent"])
        self.messageHandler = RCTConvert.nsString(options["messageHandler"])
        self.loadingView = RCTConvert.nsString(options["loadingView"])
        if (options["navBarStyle"] != nil) { setNavBarStyle(RCTConvert.nsDictionary(options["navBarStyle"])) }
        if (options["tabBarStyle"] != nil) { setTabBarStyle(RCTConvert.nsDictionary(options["tabBarStyle"])) }
    }
    
    fileprivate func setNavBarStyle(_ style: Dictionary<AnyHashable, Any>) {
        barTintColor = RCTConvert.uiColor(style["barTintColor"])
        tintColor = RCTConvert.uiColor(style["tintColor"])
        titleTextColor = RCTConvert.uiColor(style["titleTextColor"])
        subtitleTextColor = RCTConvert.uiColor(style["subtitleTextColor"])
        customMenuIcon = RCTConvert.uiImage(style["menuIcon"])
    }
    
    fileprivate func setTabBarStyle(_ style: Dictionary<AnyHashable, Any>) {
        tabBarBarTintColor = RCTConvert.uiColor(style["barTintColor"])
        tabBarTintColor = RCTConvert.uiColor(style["tintColor"])
    }
    
    fileprivate func visitTabRoutes(_ routes: Array<Dictionary<AnyHashable, Any>>) {
        for (index, route) in routes.enumerated() {
            tabBarController.selectedIndex = index
            visit(route)
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
    
    func handleVisitCompleted(_ URL: URL,_ tabIndex: Int) {
        sendEvent(withName: "turbolinksVisitCompleted", body: ["url": URL.absoluteString, "path": URL.path, "tabIndex": tabIndex])
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
        return ["turbolinksVisit", "turbolinksMessage", "turbolinksError", "turbolinksTitlePress", "turbolinksActionPress", "turbolinksLeftButtonPress", "turbolinksVisitCompleted"]
    }
}

extension RNTurbolinksManager: SessionDelegate {
    func session(_ session: Session, didProposeVisitToURL URL: URL, withAction action: Action) {
        sendEvent(withName: "turbolinksVisit", body: ["url": URL.absoluteString, "path": URL.path, "action": action.rawValue])
    }
    
    func session(_ session: Session, didFailRequestForVisitable visitable: Visitable, withError error: NSError) {
        let session = session as! TurbolinksSession
        sendEvent(withName: "turbolinksError", body: ["code": error.code, "statusCode": error.userInfo["statusCode"] ?? 0, "description": error.localizedDescription, "tabIndex": session.index])
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
