#import "RNTTurbolinksManager.h"
@import Turbolinks;

@implementation RNTTurbolinksManager

RCT_EXPORT_MODULE();

    UINavigationController *navigationController;
    Session *session;

-(id)init {
    if ( self = [super init] ) {
        navigationController = [UINavigationController new];
        session = [[Session alloc] init];
    }
    return self;
}

#pragma mark MKTurbolinkViewDelegate

RCT_EXPORT_METHOD(visit)
{
    NSLog(@"testetestetsteste");
    //NSURL *urlInit = [NSURL URLWithString:@"https:3.basecamp.com/sign_in"];
    //VisitableViewController *visitableViewController = [[VisitableViewController alloc] initWithUrl:urlInit];
    //[navigationController pushViewController:visitableViewController animated:YES];
    //[session visit:visitableViewController];
}

- (UIView *)view
{
    return navigationController.view;
}

@end
