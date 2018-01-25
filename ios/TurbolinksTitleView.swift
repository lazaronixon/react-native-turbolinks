class TurbolinksTitleView : UIStackView {
    
    convenience init(title: String, subtitle: String?, manager: RNTurbolinksManager) {
        let one = UILabel()
        one.text = title
        one.textColor = manager.titleTextColor ?? UIColor.black
        one.font = UIFont.boldSystemFont(ofSize: 17)
        one.sizeToFit()
        
        let two = UILabel()
        two.text = subtitle
        two.textColor = manager.subTitleTextColor ?? UIColor.gray
        two.font = UIFont.systemFont(ofSize: 12)
        two.textAlignment = .center
        two.sizeToFit()
        
        self.init(arrangedSubviews: [one, two])
        distribution = .equalCentering
        axis = .vertical
        
        let width = max(one.frame.size.width, two.frame.size.width)
        frame = CGRect(x: 0, y: 0, width: width, height: 35)
        
        one.sizeToFit()
        two.sizeToFit()
    }
    
}
