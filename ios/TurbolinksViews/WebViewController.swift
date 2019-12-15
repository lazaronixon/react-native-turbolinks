import Turbolinks
import UIKit

class WebViewController: VisitableViewController {
    var manager: RNTurbolinksManager!
    var route: TurbolinksRoute!
    var customView: UIView!
    var selectorHandleLeftButtonPress: Selector = #selector(handleLeftButtonPress)
    var selectorHandleRightButtonPress: Selector = #selector(handleRightButtonPress)
    var selectorPresentActions: Selector = #selector(presentActionsGeneric)
    
    fileprivate var dummyVisitableView: VisitableView!
    
    convenience required init(_ manager: RNTurbolinksManager,_ route: TurbolinksRoute) {
        self.init(url: route.url!)
        self.manager = manager
        self.route = route
    }

    func renderComponent(_ route: TurbolinksRoute) {
        customView = RCTRootView(bridge: manager.bridge, moduleName: route.component!, initialProperties: route.passProps)
        customView.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(customView)
        installErrorViewConstraints()
    }
    
    func reload() {
        customView.removeFromSuperview()
        reloadVisitable()
    }
    
    fileprivate func installErrorViewConstraints() {
        view.addConstraints(NSLayoutConstraint.constraints(withVisualFormat: "H:|[view]|", options: [], metrics: nil, views: [ "view": customView! ]))
        view.addConstraints(NSLayoutConstraint.constraints(withVisualFormat: "V:|[view]|", options: [], metrics: nil, views: [ "view": customView! ]))
    }
    
    fileprivate func handleVisitCompleted() {
        manager.handleVisitCompleted(self.visitableView.webView!.url!)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        hideOrDisplayShadow()
    }
    
    override func willTransition(to newCollection: UITraitCollection, with coordinator: UIViewControllerTransitionCoordinator) {
        super.willTransition(to: newCollection, with: coordinator)
        renderLeftButton()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        renderBackButton()
        renderLeftButton()
        renderRightButton()
        renderActions()
    }
    
    override func visitableDidRender() {
        super.visitableDidRender()
        renderTitle()
        handleVisitCompleted()
    }

    override var visitableView: VisitableView! {
        if dummyVisitableView == nil {
            dummyVisitableView = CustomVisitableView(self.manager)
            dummyVisitableView.translatesAutoresizingMaskIntoConstraints = false
            return dummyVisitableView
        } else {
            return dummyVisitableView
        }
    }
}

extension WebViewController: ApplicationViewController {
    @objc func handleLeftButtonPress() {
        manager.handleLeftButtonPress(visitableView.webView!.url, nil)
    }
    
    @objc func handleRightButtonPress() {
        manager.handleRightButtonPress(visitableView.webView!.url, nil)
    }
    
    @objc func presentActionsGeneric(_ sender: UIBarButtonItem) {
        presentActions(sender)
    }
}
