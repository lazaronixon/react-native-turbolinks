class NativeViewController: UIViewController {
    
    var manager : RNTurbolinksManager!
    var route: TurbolinksRoute!
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        renderRightButton()
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
        manager.handleRightButtonPress(URL: nil, component: route.component)
    }
    
}
