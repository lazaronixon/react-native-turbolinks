import Turbolinks

class CustomVisitableView : VisitableView {
    
    fileprivate var loadingView : UIView!
    
    convenience init(_ manager: RNTurbolinksManager) {
        self.init(frame: CGRect.zero)
        if (manager.loadingView != nil) {
            self.loadingView = RCTRootView(bridge: manager.bridge, moduleName: manager.loadingView, initialProperties: nil)
            self.loadingView.translatesAutoresizingMaskIntoConstraints = false
            installLoadingView()
        }
    }
    
    fileprivate func installLoadingView() {
        addSubview(loadingView)
        addConstraints(NSLayoutConstraint.constraints(withVisualFormat: "H:|[view]|", options: [], metrics: nil, views: [ "view": loadingView ]))
        addConstraints(NSLayoutConstraint.constraints(withVisualFormat: "V:|[view]|", options: [], metrics: nil, views: [ "view": loadingView ]))
    }
    
    override func showActivityIndicator() {
        if (loadingView != nil) {
            if !isRefreshing { bringSubview(toFront: loadingView) }
        } else {
            super.showActivityIndicator()
        }
    }
    
    override func hideActivityIndicator() {
        if (loadingView != nil) {
            loadingView.isHidden = true
        } else {
            super.hideActivityIndicator()
        }
    }
    
}
