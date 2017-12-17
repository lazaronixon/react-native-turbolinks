
#import "RNTurbolinks.h"
#import <React/RCTLog.h>

@implementation RNTurbolinks

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(printLog) {
  RCTLogInfo(@"It is working!!");
}

@end
