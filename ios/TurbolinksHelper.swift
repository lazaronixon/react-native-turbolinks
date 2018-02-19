class TurbolinksHelper {
    static func getUIKitLocalizedString(_ key: String) -> String {
        let bundle = Bundle(identifier: "com.apple.UIKit")!
        return bundle.localizedString(forKey: key, value: nil, table: nil)
    }
}
