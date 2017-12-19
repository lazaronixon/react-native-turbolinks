#import "RNTurbolinksManager.h"
#import "RNTurbolinks.h"
#import <React/RCTBridge.h>
#import <React/UIView+React.h>

@interface RNTurbolinksManager()
@end

@implementation RNTurbolinksManager

RCT_EXPORT_MODULE();

- (UIView *)viewWithProps:(__unused NSDictionary *)props
{
    return [self view];
}

- (UIView *)view
{
    if(!self.turbolinks){
        self.turbolinks = [[RNTurbolinks alloc] initWithManager:self bridge:self.bridge];
    }
    return self.turbolinks;
}

RCT_EXPORT_METHOD(visit)
{
    NSLog(@"Visiting ------------------");
}

@end
