class TurbolinksHelper {
    
    static func getJSCookiesString(_ cookies: [HTTPCookie]) -> String {
        var result = ""
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US")
        dateFormatter.timeZone = TimeZone(abbreviation: "UTC")
        dateFormatter.dateFormat = "EEE, d MMM yyyy HH:mm:ss zzz"
        for cookie in cookies {
            result += "document.cookie='\(cookie.name)=\(cookie.value); domain=\(cookie.domain); path=\(cookie.path); "
            if let date = cookie.expiresDate { result += "expires=\(dateFormatter.string(from: date)); " }
            if (cookie.isSecure) { result += "secure; " }
            result += "'; "
        }
        return result
    }
    
    static func getUIKitLocalizedString(_ key: String) -> String {
        let bundle = Bundle(identifier: "com.apple.UIKit")!
        return bundle.localizedString(forKey: key, value: nil, table: nil)
    }
}
