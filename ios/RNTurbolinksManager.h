#import <React/RCTViewManager.h>
#import <WebKit/WebKit.h>

@import Turbolinks;

@class RNTurbolinks;

@interface RNTurbolinksManager : RCTViewManager<SessionDelegate, WKScriptMessageHandler>

@property (nonatomic, strong) RNTurbolinks *turbolinks;
@property (nonatomic, strong) UINavigationController *navigationController;
@property (nonatomic, strong) Session *session;
@property (nonatomic, strong) NSURL *url;
@property (nonatomic, strong) NSString *userAgent;

- (void)initialize;

@end
