import Turbolinks

class TurbolinksRoute {
    var title: String?
    var subtitle: String?
    var titleImage: UIImage?
    var action: Action?
    var url: URL?
    var component: String?
    var modal: Bool = false
    var passProps: Dictionary<AnyHashable, Any>?
    var actions: Array<Dictionary<AnyHashable, Any>>?
    var leftButtonIcon: UIImage?
    var rightButtonIcon: UIImage?
    var navBarHidden: Bool = false
    
    init(_ route: Dictionary<AnyHashable, Any>) {
        let action = RCTConvert.nsString(route["action"])
        self.title = RCTConvert.nsString(route["title"])
        self.subtitle = RCTConvert.nsString(route["subtitle"])
        self.titleImage = RCTConvert.uiImage(route["titleImage"])
        self.action = Action(rawValue: action ?? "advance")!
        self.url = RCTConvert.nsurl(route["url"])
        self.component = RCTConvert.nsString(route["component"])
        self.modal = RCTConvert.bool(route["modal"])
        self.passProps = RCTConvert.nsDictionary(route["passProps"])
        self.actions = RCTConvert.nsDictionaryArray(route["actions"])
        self.leftButtonIcon = RCTConvert.uiImage(route["leftButtonIcon"])
        self.rightButtonIcon = RCTConvert.uiImage(route["rightButtonIcon"])
        self.navBarHidden = RCTConvert.bool(route["navBarHidden"])
    }    
}
