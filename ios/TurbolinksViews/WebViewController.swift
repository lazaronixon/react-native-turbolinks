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
    
    override func visitableDidRender() {
        super.visitableDidRender()
        renderTitle()
    }
    
    fileprivate func renderTitle() {
        if route.title != nil { navigationItem.title = route.title }
        navigationItem.titleView = TurbolinksTitleView(self)
    }
    
    fileprivate func renderLoadingStyle() {
        self.visitableView.activityIndicatorView.backgroundColor = self.manager.loadingBackgroundColor ?? .white
        self.visitableView.activityIndicatorView.color = self.manager.loadingColor ?? .gray
    }
    
    func handleTitlePress() {
        manager.handleTitlePress(URL: visitableURL, component: nil)
    }
    
    func renderActions() {
        if route.action != nil {
            let button = UIBarButtonItem.init(title: "Menu", style: .plain, target: self, action: #selector(self.showActions))
            navigationItem.rightBarButtonItem = button
        }
    }
    
    @objc func showActions(sender: UIBarButtonItem) {
        let actionsView = ActionsViewController(manager: manager, route: route, barButtonItem: sender)
        present(actionsView,animated: true)
    }
    
}


