import Turbolinks
import UIKit

class WebViewController: Turbolinks.VisitableViewController {
    
    var manager: RNTurbolinksManager!
    var route: TurbolinksRoute!
    var customView: UIView?
    var selectorHandleLeftButtonPress: Selector = #selector(handleLeftButtonPress)
    var selectorPresentActions: Selector = #selector(presentActionsGeneric)
    
    convenience required init(_ manager: RNTurbolinksManager,_ route: TurbolinksRoute) {
        self.init(url: route.url!)
        self.manager = manager
        self.route = route
        self.renderLoadingStyle()
        self.renderActions()
        self.renderBackButton()
        self.renderLeftButton()
    }

    func renderComponent() {
        customView = RCTRootView(bridge: manager.bridge, moduleName: route.component, initialProperties: route.passProps)
        customView!.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(customView!)
        installErrorViewConstraints()
    }
    
    func reload() {
        customView?.removeFromSuperview()
        reloadVisitable()
    }
    
    fileprivate func installErrorViewConstraints() {
        view.addConstraints(NSLayoutConstraint.constraints(withVisualFormat: "H:|[view]|", options: [], metrics: nil, views: [ "view": customView! ]))
        view.addConstraints(NSLayoutConstraint.constraints(withVisualFormat: "V:|[view]|", options: [], metrics: nil, views: [ "view": customView! ]))
    }
    
    fileprivate func renderLoadingStyle() {
        visitableView.activityIndicatorView.backgroundColor = manager.loadingBackgroundColor ?? .white
        visitableView.activityIndicatorView.color = manager.loadingColor ?? .gray
    }
    
    fileprivate func handleVisitCompleted() {
        let javaScriptString = "document.documentElement.outerHTML"
        visitableView.webView!.evaluateJavaScript(javaScriptString, completionHandler: { (document, error) in
            self.manager.handleVisitCompleted(self.visitableURL, document as? String)
        })
    }
    
    fileprivate func fixScrollWebView() {
        let navBarHeight =  navigationController!.navigationBar.frame.size.height
        visitableView.contentInset = UIEdgeInsetsMake(navBarHeight + 20, 0, 0, 0)
    }
    
    fileprivate func setWebViewTitle() {
        navigationItem.title = visitableView.webView?.title
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
        manager.handleTitlePress(visitableURL, route.component)
    }
    
    @objc func handleLeftButtonPress() {
        manager.handleLeftButtonPress(visitableURL, nil)
    }
    
    @objc func presentActionsGeneric(_ sender: UIBarButtonItem) {
        self.presentActions(sender)
    }
}


