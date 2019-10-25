class TurbolinksTitleView : UIStackView {
    
    fileprivate var viewController: ApplicationViewController!
    fileprivate var title: String?
    fileprivate var subtitle: String?
    fileprivate var titleImage: UIImage?
    fileprivate var textColor: UIColor?
    fileprivate var subtitleTextColor: UIColor?
    fileprivate var navBarDropDown: Bool = false
    fileprivate var turbolinksBundle: Bundle { Bundle(path: Bundle.main.path(forResource: "RNTurbolinks", ofType: "bundle")!)! }
    
    convenience init(_ viewController: ApplicationViewController) {
        self.init()
        self.viewController = viewController
        self.title = viewController.route.title ?? viewController.navigationItem.title
        self.subtitle = viewController.route.subtitle
        self.titleImage = viewController.route.titleImage
        self.navBarDropDown = viewController.route.navBarDropDown
        self.textColor = viewController.manager.titleTextColor ?? .black
        self.subtitleTextColor = viewController.manager.subtitleTextColor ?? .gray
        self.configureView()
        
        if titleImage != nil {
            addTitleImage()
        } else {
            addTitle()
            addSubtitle()
        }
    }
    
    fileprivate func configureView() {
        self.axis = .vertical
        self.alignment = .center
        self.addGestureRecognizer(getTitlePressGesture(self))
    }
    
    fileprivate func addTitleImage() {
        let image = UIImageView(image: titleImage)
        addArrangedSubview(image)
    }
    
    fileprivate func addTitle() {
        let stackView: UIStackView = UIStackView()
        stackView.axis = .horizontal
        stackView.alignment = .center
        stackView.spacing = 5
        
        let label = UILabel()
        label.text = title
        label.textColor = textColor
        label.font = .boldSystemFont(ofSize: 17)
        stackView.addArrangedSubview(label)
        
        if (self.navBarDropDown) {
            let dropDown = UIImageView(image: UIImage(named: "ic_caret", in: turbolinksBundle, compatibleWith: nil))
            stackView.addArrangedSubview(dropDown)
        }
        
        addArrangedSubview(stackView)
    }
    
    fileprivate func addSubtitle() {
        let label = UILabel()
        label.text = subtitle
        label.textColor = subtitleTextColor
        label.font = UIFont.systemFont(ofSize: 12)
        addArrangedSubview(label)
    }
    
    fileprivate func getTitlePressGesture(_ target: Any) -> UITapGestureRecognizer {
        return UITapGestureRecognizer(target: target, action: #selector(self.handleTitlePress))
    }
    
    @objc fileprivate func handleTitlePress() {
        viewController.handleTitlePress()
    }
}
