#import "React/RCTBridge.h"
#import "RNTurbolinks.h"
#import "RNTurbolinksManager.h"


@interface RNTurbolinks ()

@property (nonatomic, weak) RCTBridge *bridge;

@end

@implementation RNTurbolinks

- (id)initWithBridge:(RCTBridge*)bridge {
    if ((self = [super init])) _bridge = bridge;
    return self;
}

@end

