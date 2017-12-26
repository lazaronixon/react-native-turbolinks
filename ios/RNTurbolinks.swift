@objc(RNTurbolinks)
class RNTurbolinks: UIView  {

    var navigationController: UINavigationController!
    var userAgent: String!
    var onMessage: RCTDirectEventBlock!
    var onVisit: RCTDirectEventBlock!
    var onError: RCTDirectEventBlock!

    override init(frame: CGRect) {
        super.init(frame: frame)
        self.navigationController = UINavigationController()
        self.navigationController.view.frame = bounds
        self.navigationController.navigationBar.isTranslucent = true
        addSubview(navigationController.view)
    }

    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
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

    @objc func setOnError(_ onErrorParam: @escaping RCTDirectEventBlock) {
        onError = onErrorParam
    }

}
