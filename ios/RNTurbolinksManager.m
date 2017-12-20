#import "RNTurbolinksManager.h"
#import "RNTurbolinks.h"

@import Turbolinks;

@interface RNTurbolinksManager()

@property (strong, nonatomic) Session *session;

@end

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
    NSURL *urlInit = [NSURL URLWithString:@"https://3.basecamp.com/sign_in"];
    VisitableViewController *visitableViewController = [[VisitableViewController alloc] initWithUrl:urlInit];
    [self.turbolinks pushViewController:visitableViewController animated:YES];
    [self.session visit:visitableViewController];
    NSLog(@"Visiting1 ------------------");
}

@end
