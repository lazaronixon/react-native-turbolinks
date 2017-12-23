#import <React/RCTComponent.h>

@import Turbolinks;

@class RNTurbolinksManager;

@interface RNTurbolinks : UIView

@property (nonatomic, strong) UINavigationController *navigationController;
@property (nonatomic, copy) RCTDirectEventBlock onMessage;

- (id)initWithBridge:(RCTBridge*)bridge;

@end
