#import "RNTurbolinksManager.h"
#import "RNTurbolinks.h"
#import <React/RCTBridge.h>
@import Turbolinks;

@implementation RNTurbolinksManager

RCT_EXPORT_MODULE();

- (UIView *)view
{
    if(!self.turbolinks){
        self.turbolinks = [[RNTurbolinks alloc] initWithManager:self bridge:self.bridge];
    }
    return self.turbolinks.view;
}

RCT_EXPORT_METHOD(visit)
{
    NSLog(@"Visiting ------------------");
}

@end
