import Turbolinks
import UIKit

class CustomViewController: Turbolinks.VisitableViewController {
    
    fileprivate var customView: UIView?;
    
    func renderComponent(_ customViewParam: UIView) {
        customView = customViewParam
        customView!.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(customView!)
        installErrorViewConstraints()
    }
    
    func installErrorViewConstraints() {
        view.addConstraints(NSLayoutConstraint.constraints(withVisualFormat: "H:|[view]|", options: [], metrics: nil, views: [ "view": customView! ]))
        view.addConstraints(NSLayoutConstraint.constraints(withVisualFormat: "V:|[view]|", options: [], metrics: nil, views: [ "view": customView! ]))
    }
    
    @objc func retry(_ sender: AnyObject) {
        customView?.removeFromSuperview()
        reloadVisitable()
    }
}

