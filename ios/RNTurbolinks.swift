@objc(RNTurbolinks)
class RNTurbolinks: UIView  {
    
    weak var bridge: RCTBridge!
    var navigationController: UINavigationController!
    var url: URL!
    var userAgent: String!
    var onMessage: RCTDirectEventBlock!
    var onVisit: RCTDirectEventBlock!
    
    init(bridge: RCTBridge) {
        super.init(frame: CGRect.zero)
        self.bridge = bridge
        self.navigationController = UINavigationController()
        self.navigationController.view.frame = bounds
        addSubview(navigationController.view)
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    @objc func setInitialRoute(_ initialRouteParam: NSDictionary) {
        var initialRoute = RCTConvert.nsDictionary(initialRouteParam)!
        url = RCTConvert.nsurl(initialRoute["url"])
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
