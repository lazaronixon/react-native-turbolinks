#import <React/RCTViewManager.h>

@class RNTurbolinks;

@interface RNTurbolinksManager : RCTViewManager;

  @property (nonatomic, strong) RNTurbolinks *turbolinks;

- (void)visit;

@end
