import Turbolinks
import UIKit

class WebViewController: Turbolinks.VisitableViewController {
    
    var manager: RNTurbolinksManager!
    var route: TurbolinksRoute!
    var customView: UIView!
    var selectorHandleLeftButtonPress: Selector = #selector(handleLeftButtonPress)
    var selectorPresentActions: Selector = #selector(presentActionsGeneric)
    
    convenience required init(_ manager: RNTurbolinksManager,_ route: TurbolinksRoute) {
        self.init(url: route.url!)
        self.manager = manager
        self.route = route
    }

    func renderComponent(_ route: TurbolinksRoute) {
        customView = RCTRootView(bridge: manager.bridge, moduleName: route.component, initialProperties: route.passProps)
        customView.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(customView)
        installErrorViewConstraints()
    }
    
    func reload() {
        customView.removeFromSuperview()
        reloadVisitable()
    }
    
    fileprivate func installErrorViewConstraints() {
        view.addConstraints(NSLayoutConstraint.constraints(withVisualFormat: "H:|[view]|", options: [], metrics: nil, views: [ "view": customView ]))
        view.addConstraints(NSLayoutConstraint.constraints(withVisualFormat: "V:|[view]|", options: [], metrics: nil, views: [ "view": customView ]))
    }
    
    fileprivate func renderLoadingStyle() {
        let loadingBackgroundColor = manager.loadingBackgroundColor ?? .white
        let loadingColor = manager.loadingColor ?? .gray
        visitableView.activityIndicatorView.color = loadingColor
        visitableView.refreshControl.tintColor = loadingColor
        visitableView.activityIndicatorView.backgroundColor = loadingBackgroundColor
        visitableView.webView!.scrollView.backgroundColor = loadingBackgroundColor
    }
    
    fileprivate func handleVisitCompleted() {
        let navController = self.navigationController as! NavigationController
        let javaScriptString = "document.documentElement.outerHTML"
        visitableView.webView!.evaluateJavaScript(javaScriptString) { (r, e) in
            self.manager.handleVisitCompleted(self.visitableView.webView!.url, r as! String, navController.index)
        }
    }
    
    fileprivate func fixScrollWebView() {
        let navBarHeight =  navigationController!.navigationBar.frame.size.height
        visitableView.contentInset = UIEdgeInsetsMake(navBarHeight + 20, 0, 0, 0)
    }
    
    fileprivate func setWebViewTitle() {
        navigationItem.title = visitableView.webView?.title
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        renderBackButton()
        renderLeftButton()
        renderActions()
        renderLoadingStyle()
    }
    
    override func visitableDidRender() {
        fixScrollWebView()
        setWebViewTitle()
        renderTitle()
        handleVisitCompleted()
    }
    
}

extension WebViewController: GenricViewController {
    
    func handleTitlePress() {
        manager.handleTitlePress(visitableView.webView!.url, route.component)
    }
    
    @objc func handleLeftButtonPress() {
        manager.handleLeftButtonPress(visitableView.webView!.url, nil)
    }
    
    @objc func presentActionsGeneric(_ sender: UIBarButtonItem) {
        self.presentActions(sender)
    }
}


