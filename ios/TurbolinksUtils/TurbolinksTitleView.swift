open class TurbolinksTitleView : UIStackView {
    fileprivate var controller: ApplicationViewController!
    fileprivate var titleLabel: UILabel!
    fileprivate var subtitleLabel: UILabel!
    fileprivate var dropDownIcon : UIImage { UIImage(named: "caret", in: controller.manager.turbolinksBundle, compatibleWith: nil)! }
        
    @objc dynamic var titleTextColor: UIColor? { willSet { titleLabel.textColor = newValue } }
    @objc dynamic var subtitleTextColor: UIColor? = .gray { willSet { subtitleLabel.textColor = newValue ?? .gray } }
    
    convenience init(_ viewController: ApplicationViewController) {
        self.init()
        self.controller = viewController
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
        titleLabel.text = controller.route.title ?? controller.navigationItem.title
        titleLabel.textColor = titleTextColor
        titleLabel.font = .boldSystemFont(ofSize: 17)
        titleView.addArrangedSubview(titleLabel)
        
        addDropDown(titleView)
        addArrangedSubview(titleView)
    }
    
    fileprivate func addSubtitle() {
        subtitleLabel = UILabel()
        subtitleLabel.text = controller.route.subtitle
        subtitleLabel.textColor = subtitleTextColor
        subtitleLabel.font = UIFont.systemFont(ofSize: 12)
        addArrangedSubview(subtitleLabel)
    }
    
    fileprivate func addDropDown(_ titleView: UIStackView) {
        if controller.route.visibleDropDown {
            titleView.addArrangedSubview(UIImageView(image: dropDownIcon))
        }
    }
    
    fileprivate func getTitlePressGesture(_ target: Any) -> UITapGestureRecognizer {
        UITapGestureRecognizer(target: target, action: #selector(self.handleTitlePress))
    }
    
    @objc fileprivate func handleTitlePress() {
        controller.handleTitlePress()
    }
}
