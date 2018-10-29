class TurbolinksTitleView : UIStackView {
    
    fileprivate var viewController: GenricViewController!
    fileprivate var title: String?
    fileprivate var subtitle: String?
    fileprivate var titleImage: UIImage?
    fileprivate var textColor: UIColor?
    fileprivate var subtitleTextColor: UIColor?
    
    convenience init(_ viewController: GenricViewController) {
        self.init()
        self.viewController = viewController
        self.title = viewController.route.title ?? viewController.navigationItem.title
        self.subtitle = viewController.route.subtitle
        self.titleImage = viewController.route.titleImage
        self.textColor = viewController.manager.titleTextColor ?? UIColor.black
        self.subtitleTextColor = viewController.manager.subtitleTextColor ?? UIColor.gray
        self.axis = .vertical
        
        if titleImage != nil {
            addTitleImage()
        } else {
            addTitle()
            addSubtitle()
        }
    }
    
    fileprivate func addTitleImage() {
        let image = UIImageView.init(image: titleImage)
        image.isUserInteractionEnabled = true
        image.addGestureRecognizer(getGesture(self))
        addArrangedSubview(image)
    }
    
    fileprivate func addTitle() {
        let label = UILabel()
        label.text = title
        label.textColor = textColor
        label.font = UIFont.boldSystemFont(ofSize: 17)
        label.textAlignment = .center
        label.isUserInteractionEnabled = true
        label.addGestureRecognizer(getGesture(self))
        addArrangedSubview(label)
    }
    
    fileprivate func addSubtitle() {
        let label = UILabel()
        label.text = subtitle
        label.textColor = subtitleTextColor
        label.font = UIFont.systemFont(ofSize: 12)
        label.textAlignment = .center
        label.isUserInteractionEnabled = true
        label.addGestureRecognizer(getGesture(self))
        addArrangedSubview(label)
    }
    
    fileprivate func getGesture(_ target: Any) -> UITapGestureRecognizer {
        return UITapGestureRecognizer(target: target, action: #selector(self.handleTitlePress))
    }
    
    @objc fileprivate func handleTitlePress() {
        viewController.handleTitlePress()
    }
}
