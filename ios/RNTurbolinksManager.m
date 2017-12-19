#import "RNTurbolinksManager.h"

@import Turbolinks;

@implementation RNTurbolinksManager
{
    UINavigationController *navigationController;
    Session *session;
}

RCT_EXPORT_MODULE();

- (UIView *)view
{
    navigationController = [UINavigationController new];
    session = [[Session alloc] init];
    return navigationController.view;
}

RCT_EXPORT_METHOD(visit)
{
    NSURL *urlInit = [NSURL URLWithString:@"https://3.basecamp.com/sign_in"];
    VisitableViewController *visitableViewController = [[VisitableViewController alloc] initWithUrl:urlInit];
    [navigationController pushViewController:visitableViewController animated:YES];
    [session visit:visitableViewController];
    NSLog(@"Visiting ------------------");
}

@end
