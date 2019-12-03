import Turbolinks
import UIKit

class WebViewController: VisitableViewController {
    var manager: RNTurbolinksManager!
    var route: TurbolinksRoute!
    var customView: UIView!
    var selectorHandleLeftButtonPress: Selector = #selector(handleLeftButtonPress)
    var selectorHandleRightButtonPress: Selector = #selector(handleRightButtonPress)
    var selectorPresentActions: Selector = #selector(presentActionsGeneric)
    
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
    
    fileprivate func setWebViewTitle() {
        navigationItem.title = visitableView.webView!.title
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        renderBackButton()
        renderLeftButton()
        renderRightButton()
        renderActions()
    }
    
    override func visitableDidRender() {
        setWebViewTitle()
        renderTitle()
        handleVisitCompleted()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        setupNavigationBar()
    }

    fileprivate var dummyVisitableView: VisitableView!
    override var visitableView: VisitableView! {
        if dummyVisitableView != nil { return dummyVisitableView }
        dummyVisitableView = CustomVisitableView(self.manager)
        dummyVisitableView.translatesAutoresizingMaskIntoConstraints = false
        return dummyVisitableView
    }
    
    public override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        if !(manager.automaticallyAdjustContentInsets ?? true) {
            if let contentInset = manager.contentInset {
                visitableView.contentInset = contentInset
            } else {
                visitableView.contentInset = UIEdgeInsets.zero
            }
        }
    }
}

extension WebViewController: ApplicationViewController {
    
    func handleTitlePress() {
        manager.handleTitlePress(visitableView.webView!.url, nil)
    }
    
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
