#import "RNTurbolinks.h"
@import Turbolinks;

@implementation RNTurbolinks

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(printLog) {
  NSLog(@"It is working!!");
}

@end
