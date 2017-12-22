#import <React/RCTViewManager.h>

@import Turbolinks;

@class RNTurbolinks;

@interface RNTurbolinksManager : RCTViewManager<SessionDelegate>

@property (nonatomic, strong) RNTurbolinks *turbolinks;
@property (nonatomic, strong) Session *session;
@property (nonatomic, strong) NSURL *url;
@property (nonatomic, strong) NSString *userAgent;

- (void)initialize;

@end
