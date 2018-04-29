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
        
        if titleImage != nil {
            let gestureImage = UITapGestureRecognizer(target: self, action: #selector(self.handleTitlePress))
            let image = UIImageView.init(image: titleImage)
            image.isUserInteractionEnabled = true
            image.addGestureRecognizer(gestureImage)
            addArrangedSubview(image)            
        } else {
            let gestureOne = UITapGestureRecognizer(target: self, action: #selector(self.handleTitlePress))
            let one = UILabel()
            one.text = title
            one.textColor = textColor
            one.font = UIFont.boldSystemFont(ofSize: 17)
            one.textAlignment = .center
            one.isUserInteractionEnabled = true
            one.addGestureRecognizer(gestureOne)
            addArrangedSubview(one)
            
            let gestureTwo = UITapGestureRecognizer(target: self, action: #selector(self.handleTitlePress))
            let two = UILabel()
            two.text = subtitle
            two.textColor = subtitleTextColor
            two.font = UIFont.systemFont(ofSize: 12)
            two.textAlignment = .center
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
    }
    
    @objc fileprivate func handleTitlePress() {
        viewController.handleTitlePress()
    }
}
