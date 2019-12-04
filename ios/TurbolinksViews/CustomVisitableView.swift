import Turbolinks

class CustomVisitableView : VisitableView {
    fileprivate var loadingView : UIView!
    
    convenience init(_ manager: RNTurbolinksManager) {
        self.init(frame: CGRect.zero)
        initLoadingView(manager)
    }
    
    fileprivate func initLoadingView(_ manager: RNTurbolinksManager) {
        guard let loadingView = manager.loadingView else { return }
        self.loadingView = RCTRootView(bridge: manager.bridge, moduleName: loadingView, initialProperties: nil)
        self.loadingView.translatesAutoresizingMaskIntoConstraints = false
        installLoadingView()
    }
    
    fileprivate func installLoadingView() {
        addSubview(loadingView)
        addConstraints(NSLayoutConstraint.constraints(withVisualFormat: "H:|[view]|", options: [], metrics: nil, views: [ "view": loadingView! ]))
        addConstraints(NSLayoutConstraint.constraints(withVisualFormat: "V:|[view]|", options: [], metrics: nil, views: [ "view": loadingView! ]))
    }
    
    fileprivate func showCustomLoadingView() {
        if isRefreshing { return }
        loadingView.isHidden = false
        bringSubviewToFront(loadingView)
    }
    
    override func showActivityIndicator() {
        if (loadingView != nil) {
            self.showCustomLoadingView()
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
