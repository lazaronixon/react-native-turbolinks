class TurbolinksTabBarItem{
    
    var title: String
    var icon: UIImage?
    var badge: String?
    
    init(_ item: Dictionary<AnyHashable, Any>) {
        self.title = RCTConvert.nsString(item["title"])
        self.icon = RCTConvert.uiImage(item["icon"])
        self.badge = RCTConvert.nsString(item["badge"])
    }
    
}

