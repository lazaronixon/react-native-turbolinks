#import <React/RCTViewManager.h>

@import Turbolinks;

@class RNTurbolinks;

@interface RNTurbolinksManager : RCTViewManager;

  @property (nonatomic, strong) RNTurbolinks *turbolinks;
  @property (nonatomic, strong) Session *session;
  @property (nonatomic, strong) dispatch_queue_t sessionQueue;

- (void)visit;

@end
