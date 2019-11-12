open class UITitleView : UIStackView {
    fileprivate var viewController: ApplicationViewController!
    fileprivate var title: String?
    fileprivate var subtitle: String?
    fileprivate var titleLabel: UILabel!
    fileprivate var subtitleLabel: UILabel!
    fileprivate var titleImage: UIImage!
    fileprivate var navBarDropDown: Bool = false
    fileprivate var turbolinksBundle: Bundle { Bundle(path: Bundle.main.path(forResource: "RNTurbolinks", ofType: "bundle")!)! }
        
    @objc dynamic var titleTextColor: UIColor? { willSet { titleLabel.textColor = newValue } }
    @objc dynamic var subtitleTextColor: UIColor? { willSet { subtitleLabel.textColor = newValue } }
    
    convenience init(_ viewController: ApplicationViewController) {
        self.init()
        self.viewController = viewController
        self.title = viewController.route.title ?? viewController.navigationItem.title
        self.subtitle = viewController.route.subtitle
        self.titleImage = viewController.route.titleImage
        self.navBarDropDown = viewController.route.navBarDropDown
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
        let imageView = UIImageView()
        let insets = UIEdgeInsets(top: -5, left: 0, bottom: -5, right: 0)
        imageView.image = titleImage.withAlignmentRectInsets(insets)
        imageView.contentMode = .scaleAspectFit
        addArrangedSubview(imageView)
    }
    
    fileprivate func addTitle() {
        let stackView: UIStackView = UIStackView()
        stackView.axis = .horizontal
        stackView.alignment = .center
        stackView.spacing = 5
        
        titleLabel = UILabel()
        titleLabel.text = title
        titleLabel.textColor = titleTextColor
        titleLabel.font = .boldSystemFont(ofSize: 17)
        stackView.addArrangedSubview(titleLabel!)
        
        if (self.navBarDropDown) {
            let dropDown = UIImageView(image: UIImage(named: "ic_caret", in: turbolinksBundle, compatibleWith: nil))
            stackView.addArrangedSubview(dropDown)
        }
        
        addArrangedSubview(stackView)
    }
    
    fileprivate func addSubtitle() {
        subtitleLabel = UILabel()
        subtitleLabel.text = subtitle
        subtitleLabel.textColor = subtitleTextColor
        subtitleLabel.font = UIFont.systemFont(ofSize: 12)
        addArrangedSubview(subtitleLabel)
    }
    
    fileprivate func getTitlePressGesture(_ target: Any) -> UITapGestureRecognizer {
        UITapGestureRecognizer(target: target, action: #selector(self.handleTitlePress))
    }
    
    @objc fileprivate func handleTitlePress() {
        viewController.handleTitlePress()
    }
}
