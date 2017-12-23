#import "React/RCTBridge.h"
#import "RNTurbolinks.h"
#import "RNTurbolinksManager.h"


@interface RNTurbolinks ()

@property (nonatomic, weak) RCTBridge *bridge;

@end

@implementation RNTurbolinks

- (id)initWithBridge:(RCTBridge*)bridge {
    if ((self = [super initWithFrame:CGRectZero])) _bridge = bridge;
    return self;
}

@end

