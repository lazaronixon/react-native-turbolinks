#import "React/RCTBridge.h"
#import "RNTurbolinks.h"
#import "RNTurbolinksManager.h"

#import <React/UIView+React.h>

@interface RNTurbolinks ()

@property (nonatomic, weak) RNTurbolinksManager *manager;
@property (nonatomic, weak) RCTBridge *bridge;

@end

@implementation RNTurbolinks

- (id)initWithManager:(RNTurbolinksManager*)manager bridge:(RCTBridge *)bridge
{
    if ((self = [super init])) {
        self.manager = manager;
        self.bridge = bridge;
    }
    return self;
}

@end

