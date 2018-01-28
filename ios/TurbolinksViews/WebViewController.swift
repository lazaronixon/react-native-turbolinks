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
        self.renderBackButton()
        self.renderRightButton()
        self.renderLeftButton()
        self.navigationItem.leftItemsSupplementBackButton = true
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
    
    fileprivate func renderBackButton() {
        let button = UIBarButtonItem(title: "", style: .plain, target: nil, action: nil)
        navigationItem.backBarButtonItem = button
    }
    
    fileprivate func renderRightButton() {
        if route.rightButtonIcon != nil {
            let button = UIBarButtonItem(image: route.rightButtonIcon, style: .plain, target: self, action: #selector(self.handleRightButtonPress))
            navigationItem.rightBarButtonItem = button
        }
        if route.rightButtonTitle != nil {
            let button = UIBarButtonItem(title: route.rightButtonTitle, style: .plain, target: self, action: #selector(self.handleRightButtonPress))
            navigationItem.rightBarButtonItem = button
        }
    }
    
    fileprivate func renderLeftButton() {
        if route.leftButtonIcon != nil {
            let button = UIBarButtonItem(image: route.leftButtonIcon, style: .plain, target: self, action: #selector(self.handleLeftButtonPress))
            navigationItem.leftBarButtonItem = button
        }
        if route.leftButtonTitle != nil {
            let button = UIBarButtonItem(title: route.leftButtonTitle, style: .plain, target: self, action: #selector(self.handleLeftButtonPress))
            navigationItem.leftBarButtonItem = button
        }
    }
    
    fileprivate func renderLoadingStyle() {
        self.visitableView.activityIndicatorView.backgroundColor = self.manager.loadingBackgroundColor ?? .white
        self.visitableView.activityIndicatorView.color = self.manager.loadingColor ?? .gray
    }
    
    func handleTitlePress() {
        manager.handleTitlePress(URL: visitableURL, component: nil)
    }
    
    @objc fileprivate func handleRightButtonPress() {
        manager.handleRightButtonPress(URL: visitableURL, component: nil)
    }
    
    @objc fileprivate func handleLeftButtonPress() {
        manager.handleLeftButtonPress(URL: nil, component: route.component)
    }
}



