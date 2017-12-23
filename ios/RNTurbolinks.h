#import <React/RCTComponent.h>

@import Turbolinks;

@class RNTurbolinksManager;

@interface RNTurbolinks : UIView

@property (nonatomic, copy) RCTDirectEventBlock onMessage;

- (id)initWithBridge:(RCTBridge*)bridge;

@end
