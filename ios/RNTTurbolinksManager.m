#import "RNTTurbolinksManager.h"
#import <React/RCTUIManager.h>
@import Turbolinks;

@implementation RNTTurbolinksManager
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
   NSLog(@"testetestetsteste");
}

@end
