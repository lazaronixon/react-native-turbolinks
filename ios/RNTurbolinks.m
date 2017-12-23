#import "React/RCTBridge.h"
#import "RNTurbolinks.h"
#import "RNTurbolinksManager.h"


@interface RNTurbolinks ()

@property (nonatomic, weak) RCTBridge *bridge;

@end

@implementation RNTurbolinks

- (id)initWithBridge:(RCTBridge*)bridge {
    if ((self = [super initWithFrame:CGRectZero])) {
       _bridge = bridge;
       _navigationController = [self createNavigationController];
    }
    return self;
}

-(UINavigationController *) createNavigationController {
    UINavigationController *navigationController = [[UINavigationController alloc] init];
    navigationController.view.frame = self.bounds;
    [navigationController.navigationBar setTranslucent:YES];
    [self addSubview:navigationController.view];
    return navigationController;
}

@end

