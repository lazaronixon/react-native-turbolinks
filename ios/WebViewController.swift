import Turbolinks
import UIKit

class WebViewController: Turbolinks.VisitableViewController {
    
    var manager : RNTurbolinksManager!
    var route: TurbolinksRoute!
    var customView: UIView?

    func renderComponent() {
        customView!.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(customView!)
        installErrorViewConstraints()
        visitableDidRender()
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
        navigationItem.leftItemsSupplementBackButton = true
        paintNavBar()
        renderTitle()
        renderBackButton()
        renderRightButton()
        renderLeftButton()
    }
    
    fileprivate func paintNavBar() {
        let navBar = navigationController?.navigationBar
        if manager.barTintColor != nil { navBar?.barTintColor = manager.barTintColor }
        if manager.tintColor != nil { navBar?.tintColor = manager.tintColor }
        if manager.titleTextColor != nil { navBar?.titleTextAttributes = [.foregroundColor: manager.titleTextColor!] }
    }
    
    fileprivate func renderBackButton() {
        let button = UIBarButtonItem(title: "", style: .plain, target: nil, action: nil)
        if manager.backButtonTitleHidden { navigationItem.backBarButtonItem = button }
    }
    
    fileprivate func renderTitle() {
        if route.title != nil { title = route.title }
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
    
    @objc fileprivate func handleRightButtonPress() {
        manager.handleRightButtonPress(URL: visitableURL, component: nil)
    }
    
    @objc fileprivate func handleLeftButtonPress() {
        manager.handleLeftButtonPress(URL: nil, component: route.component)
    }
}

