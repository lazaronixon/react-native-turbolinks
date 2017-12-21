#import "React/RCTBridge.h"
#import "RNTurbolinks.h"
#import "RNTurbolinksManager.h"


@interface RNTurbolinks ()

@property (nonatomic, weak) RNTurbolinksManager *manager;
@property (nonatomic, weak) RCTBridge *bridge;

@end

@implementation RNTurbolinks

- (id)initWithManager:(RNTurbolinksManager*)manager bridge:(RCTBridge *)bridge
{
    if ((self = [super init])) {        
        _manager = manager;
        _bridge = bridge;        
    }
    return self;
}

@end
