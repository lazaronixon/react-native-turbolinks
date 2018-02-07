class TurbolinksAction {
    
    var id: Int
    var title: String?
    var icon: UIImage?
    
    init(_ action: Dictionary<AnyHashable, Any>) {
        self.id = RCTConvert.nsInteger(action["id"])
        self.title = RCTConvert.nsString(action["title"])
        self.icon = RCTConvert.uiImage(action["icon"])
    }    
}
