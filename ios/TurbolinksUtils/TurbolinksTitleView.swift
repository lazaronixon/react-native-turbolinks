class TurbolinksTitleView : UIStackView {
    
    fileprivate var viewController: UIViewController?
    fileprivate var title: String?
    fileprivate var subtitle: String?
    fileprivate var textColor: UIColor?
    fileprivate var subtitleTextColor: UIColor?
    
    convenience init(_ viewController: UIViewController) {
        self.init()
        self.viewController = viewController
        self.title = viewController.navigationItem.title
        
        if let turbolinksView = viewController as? WebViewController {
            self.subtitle = turbolinksView.route.subtitle
            self.textColor = turbolinksView.manager.titleTextColor ?? UIColor.black
            self.subtitleTextColor = turbolinksView.manager.subTitleTextColor ?? UIColor.gray
        }
        
        if let turbolinksView = viewController as? NativeViewController {
            self.subtitle = turbolinksView.route.subtitle
            self.textColor = turbolinksView.manager.titleTextColor ?? UIColor.black
            self.subtitleTextColor = turbolinksView.manager.subTitleTextColor ?? UIColor.gray
        }
        
        let gestureOne = UITapGestureRecognizer(target: self, action: #selector(self.handleTitlePress))
        let one = UILabel()
        one.text = title
        one.textColor = textColor
        one.font = UIFont.boldSystemFont(ofSize: 17)
        one.sizeToFit()
        one.isUserInteractionEnabled = true
        one.addGestureRecognizer(gestureOne)
        addArrangedSubview(one)
        
        let gestureTwo = UITapGestureRecognizer(target: self, action: #selector(self.handleTitlePress))
        let two = UILabel()
        two.text = subtitle
        two.textColor = subtitleTextColor
        two.font = UIFont.systemFont(ofSize: 12)
        two.textAlignment = .center
        two.sizeToFit()
        two.isUserInteractionEnabled = true
        two.addGestureRecognizer(gestureTwo)
        addArrangedSubview(two)
        
        
        distribution = .equalCentering
        axis = .vertical
        
        let width = max(one.frame.size.width, two.frame.size.width)
        frame = CGRect(x: 0, y: 0, width: width, height: 35)
        
        one.sizeToFit()
        two.sizeToFit()
    }
    
    @objc fileprivate func handleTitlePress() {
        if let turbolinksView = viewController as? WebViewController {
            turbolinksView.handleTitlePress()
        }
        if let turbolinksView = viewController as? NativeViewController {
            turbolinksView.handleTitlePress()
        }
    }
    
    
}
