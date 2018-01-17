#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(RNTurbolinksManager, NSObject)
RCT_EXTERN_METHOD(reloadVisitable)
RCT_EXTERN_METHOD(reloadSession)
RCT_EXTERN_METHOD(dismiss)
RCT_EXTERN_METHOD(visit:(NSDictionary *))
RCT_EXTERN_METHOD(replaceWith:(NSDictionary *))
RCT_EXTERN_METHOD(setUserAgent:NSString)
RCT_EXTERN_METHOD(setMessageHandler:NSString)
@end
