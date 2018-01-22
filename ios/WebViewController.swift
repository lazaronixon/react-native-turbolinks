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
        renderTitle()
        renderRightButton()
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
    
    @objc fileprivate func handleRightButtonPress() {
        manager.handleRightButtonPress(URL: visitableURL, component: nil)
    }
}

