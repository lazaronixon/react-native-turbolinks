#import <React/RCTViewManager.h>

@import Turbolinks;

@class RNTurbolinks;

@interface RNTurbolinksManager : RCTViewManager;

  @property (nonatomic, strong) RNTurbolinks *turbolinks;
  @property (nonatomic, strong) Session *session;

- (void)visit;

@end
