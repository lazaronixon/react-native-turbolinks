@objc(RNTurbolinks)
class RNTurbolinks: UIView  {
    
    var navigationController: UINavigationController!
    var url: URL!
    var userAgent: String!
    var onMessage: RCTDirectEventBlock!
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        self.navigationController = self.createNavigationController()
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    fileprivate func createNavigationController() -> UINavigationController {
        let navigationController = UINavigationController()
        navigationController.view.frame = bounds
        navigationController.navigationBar.isTranslucent = true
        addSubview(navigationController.view)
        return navigationController
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
}
