import Turbolinks
import UIKit

class WebViewController: Turbolinks.VisitableViewController {
    
    var manager: RNTurbolinksManager!
    var route: TurbolinksRoute!
    var customView: UIView?
    
    convenience init(manager: RNTurbolinksManager, route: TurbolinksRoute) {
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
    
    func installErrorViewConstraints() {
        view.addConstraints(NSLayoutConstraint.constraints(withVisualFormat: "H:|[view]|", options: [], metrics: nil, views: [ "view": customView! ]))
        view.addConstraints(NSLayoutConstraint.constraints(withVisualFormat: "V:|[view]|", options: [], metrics: nil, views: [ "view": customView! ]))
    }
    
    func reload() {
        customView?.removeFromSuperview()
        reloadVisitable()
    }
    
    func handleTitlePress() {
        manager.handleTitlePress(URL: visitableURL, component: nil)
    }
    
    override func visitableDidRender() {
        super.visitableDidRender()
        renderTitle()
        handleVisitCompleted()
    }
    
    fileprivate func renderTitle() {
        if route.title != nil { navigationItem.title = route.title }
        navigationItem.titleView = TurbolinksTitleView(self)
    }
    
    fileprivate func renderLoadingStyle() {
        visitableView.activityIndicatorView.backgroundColor = manager.loadingBackgroundColor ?? .white
        visitableView.activityIndicatorView.color = manager.loadingColor ?? .gray
    }
    
    fileprivate func renderActions() {
        if route.actions != nil {
            let button = UIBarButtonItem.init(title: "\u{22EF}", style: .plain, target: self, action: #selector(presentActions))
            let font = [NSAttributedStringKey.font: UIFont.boldSystemFont(ofSize: 32)]
            button.setTitleTextAttributes(font, for: .normal)
            button.setTitleTextAttributes(font, for: .selected)
            navigationItem.rightBarButtonItem = button
        }
    }
    
    fileprivate func renderBackButton() {
        let backButton = UIBarButtonItem(title: nil, style: .plain, target: nil, action: nil)
        navigationItem.backBarButtonItem = backButton
        navigationItem.leftItemsSupplementBackButton = true
    }
    
    fileprivate func renderLeftButton() {
        if route.leftButtonIcon != nil {
            let button = UIBarButtonItem(image: route.leftButtonIcon, style: .plain, target: self, action: #selector(handleLeftButtonPress))
            navigationItem.leftBarButtonItem = button
        }
    }
    
    fileprivate func handleVisitCompleted() {
        let javaScriptString = "document.documentElement.outerHTML"
        visitableView.webView!.evaluateJavaScript(javaScriptString, completionHandler: { (document, error) in
            self.manager.handleVisitCompleted(url: self.visitableURL, source: document as? String)
        })
    }
    
    @objc func presentActions(sender: UIBarButtonItem) {
        if route.actions != nil {
            let actionsView = ActionsViewController(manager: manager, route: route, barButtonItem: sender)
            present(actionsView,animated: true)
        }
    }
    
    @objc fileprivate func handleLeftButtonPress() {
        manager.handleLeftButtonPress(URL: visitableURL, component: nil)
    }
    
}


