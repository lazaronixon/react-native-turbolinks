import Turbolinks
import UIKit

class CustomViewController: Turbolinks.VisitableViewController {
    
    func renderComponent(_ viewParam: UIView) {
        viewParam.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(viewParam)
        view.addConstraints(NSLayoutConstraint.constraints(withVisualFormat: "H:|[view]|", options: [], metrics: nil, views: [ "view": viewParam ]))
        view.addConstraints(NSLayoutConstraint.constraints(withVisualFormat: "V:|[view]|", options: [], metrics: nil, views: [ "view": viewParam ]))
    }
    
    @objc func retry(_ sender: AnyObject) {
        errorView.removeFromSuperview()
        reloadVisitable()
    }
}

