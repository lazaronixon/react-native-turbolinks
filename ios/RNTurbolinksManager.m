#import "RNTurbolinksManager.h"
#import "RNTurbolinks.h"

@import Turbolinks;

@interface RNTurbolinksManager()
@end

@implementation RNTurbolinksManager

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE();

- (UIView *)view
{
    _session = [[Session alloc] init];
    if(!_turbolinks){
        _turbolinks = [[RNTurbolinks alloc] initWithManager:self bridge:self.bridge];
        UIViewController *topController = [UIApplication sharedApplication].keyWindow.rootViewController;
        [topController presentViewController:_turbolinks animated:NO completion:nil];
    }
    return _turbolinks.view;
}

RCT_EXPORT_METHOD(visit:(NSString *)url)
{
    VisitableViewController *visitableViewController = [[VisitableViewController alloc] initWithUrl:[RCTConvert NSURL:url]];
    [_turbolinks pushViewController:visitableViewController animated:YES];
    [_session visit:visitableViewController];
}

@end
