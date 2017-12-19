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
    NSLog(@"Visiting ------------------");
}

@end
