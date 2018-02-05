class TurbolinksTabBar {
    
    var selectedIndex: Int
    var items: Array<Dictionary<AnyHashable, Any>>?
    
    init(_ tabBar: Dictionary<AnyHashable, Any>) {
        self.selectedIndex = RCTConvert.nsInteger(tabBar["selectedIndex"])
        self.items = RCTConvert.nsDictionaryArray(tabBar["items"])
    }
    
}
