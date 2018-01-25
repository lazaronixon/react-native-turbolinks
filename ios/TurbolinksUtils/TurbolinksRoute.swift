import Turbolinks

class TurbolinksRoute {
    var title:String?
    var subtitle:String?
    var action:Action?
    var rightButtonIcon:UIImage?
    var rightButtonTitle:String?
    var leftButtonIcon:UIImage?
    var leftButtonTitle:String?
    var url:URL?
    var component:String?
    var modal:Bool?
    var passProps:Dictionary<AnyHashable, Any>?
    
    init(route: Dictionary<AnyHashable, Any>) {
        let action = RCTConvert.nsString(route["action"])
        self.title = RCTConvert.nsString(route["title"])
        self.subtitle = RCTConvert.nsString(route["subtitle"])
        self.action = Action.init(rawValue: action ?? "advance")!
        self.rightButtonTitle = RCTConvert.nsString(route["rightButtonTitle"])
        self.rightButtonIcon = RCTConvert.uiImage(route["rightButtonIcon"])
        self.leftButtonTitle = RCTConvert.nsString(route["leftButtonTitle"])
        self.leftButtonIcon = RCTConvert.uiImage(route["leftButtonIcon"])
        self.url = RCTConvert.nsurl(route["url"])
        self.component = RCTConvert.nsString(route["component"])
        self.modal = RCTConvert.bool(route["modal"]) || false
        self.passProps = RCTConvert.nsDictionary(route["passProps"])
    }
    
}
