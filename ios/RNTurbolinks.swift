@objc(RNTurbolinks)
class RNTurbolinks: UIView  {
    
    var navigationController: UINavigationController!
    var url: URL!
    var userAgent: String!
    var onMessage: RCTDirectEventBlock!
    var onVisit: RCTDirectEventBlock!
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        self.navigationController = UINavigationController()
        self.navigationController.view.frame = bounds
        addSubview(navigationController.view)
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    @objc func setUrl(_ urlParam: NSDictionary) {
        url = RCTConvert.nsurl(urlParam)
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
