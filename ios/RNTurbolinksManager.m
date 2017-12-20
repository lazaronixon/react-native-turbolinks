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
    self.session = [[Session alloc] init];
    if(!self.turbolinks){
        self.turbolinks = [[RNTurbolinks alloc] initWithManager:self bridge:self.bridge];
    }
    return self.turbolinks.view;
}

RCT_EXPORT_METHOD(visit)
{
    NSURL *urlInit = [NSURL URLWithString:@"https:/3.basecamp.com/sign_in"];
    VisitableViewController *visitableViewController = [[VisitableViewController alloc] initWithUrl:urlInit];
    [self.turbolinks pushViewController:visitableViewController animated:YES];
    [self.session visit:visitableViewController];
}

@end
