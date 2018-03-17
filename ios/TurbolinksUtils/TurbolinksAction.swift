class TurbolinksAction {
    
    var id: Int
    var title: String?
    var icon: UIImage?
    var button: Bool = false
    
    init(_ action: Dictionary<AnyHashable, Any>) {
        self.id = RCTConvert.nsInteger(action["id"])
        self.title = RCTConvert.nsString(action["title"])
        self.icon = RCTConvert.uiImage(action["icon"])
        self.button = RCTConvert.bool(action["button"])
    }
}
