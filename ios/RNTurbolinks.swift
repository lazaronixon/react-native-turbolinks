@objc(RNTurbolinks)
class RNTurbolinks: UIView  {
    
    var navigationController: UINavigationController!
    var url: URL!
    var userAgent: String!
    var onMessage: RCTDirectEventBlock!
    var onVisit: RCTDirectEventBlock!
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        navigationController = createNavigationController()
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    fileprivate func createNavigationController() -> UINavigationController {
        let navController = UINavigationController()
        navController.view.frame = bounds
        navController.navigationBar.isTranslucent = true
        addSubview(navController.view)
        return navController
    }
    
    @objc func setUrl(_ urlParam: NSString) {
        url = URL(string: RCTConvert.nsString(urlParam))
    }
    
    @objc func setUserAgent(_ userAgentParam: NSString) {
        userAgent = RCTConvert.nsString(userAgentParam)
    }
    
    @objc func setOnMessage(_ onMessageParam: @escaping RCTDirectEventBlock) {
        onMessage = onMessageParam
    }
    
    @objc func setOnVisit(_ onVisitParam: @escaping RCTDirectEventBlock) {
        onVisit = onVisitParam
    }
    
}
