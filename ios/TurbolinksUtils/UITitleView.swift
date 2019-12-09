open class UITitleView : UIStackView {
    fileprivate var viewController: ApplicationViewController!
    fileprivate var title: String?
    fileprivate var subtitle: String?
    fileprivate var titleLabel: UILabel!
    fileprivate var subtitleLabel: UILabel!
    fileprivate var navBarDropDown: Bool = false
    fileprivate var turbolinksBundle: Bundle { Bundle(path: Bundle.main.path(forResource: "RNTurbolinks", ofType: "bundle")!)! }
    fileprivate var titleDropDown: UIImageView { UIImageView(image: UIImage(named: "ic_caret", in: turbolinksBundle, compatibleWith: nil)) }
        
    @objc dynamic var titleTextColor: UIColor? { willSet { titleLabel.textColor = newValue } }
    @objc dynamic var subtitleTextColor: UIColor? { willSet { subtitleLabel.textColor = newValue } }
    
    convenience init(_ viewController: ApplicationViewController) {
        self.init()
        self.viewController = viewController
        self.title = viewController.route.title ?? viewController.navigationItem.title
        self.subtitle = viewController.route.subtitle
        self.navBarDropDown = viewController.route.navBarDropDown
        self.configureView()
        
        self.addTitle()
        self.addSubtitle()
    }
    
    fileprivate func configureView() {
        self.axis = .vertical
        self.alignment = .center
        self.addGestureRecognizer(getTitlePressGesture(self))
    }
    
    fileprivate func addTitle() {
        let titleView: UIStackView = UIStackView()
        titleView.axis = .horizontal
        titleView.alignment = .center
        titleView.spacing = 5
        
        titleLabel = UILabel()
        titleLabel.text = title
        titleLabel.textColor = titleTextColor
        titleLabel.font = .boldSystemFont(ofSize: 17)
        titleView.addArrangedSubview(titleLabel)
        if (navBarDropDown) { titleView.addArrangedSubview(titleDropDown) }
        
        addArrangedSubview(titleView)
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
