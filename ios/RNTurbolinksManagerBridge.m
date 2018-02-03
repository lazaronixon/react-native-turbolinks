#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(RNTurbolinksManager, NSObject)
RCT_EXTERN_METHOD(reloadVisitable)
RCT_EXTERN_METHOD(reloadSession)
RCT_EXTERN_METHOD(dismiss)
RCT_EXTERN_METHOD(back)
RCT_EXTERN_METHOD(visit:(NSDictionary *))
RCT_EXTERN_METHOD(replaceWith:(NSDictionary *))
RCT_EXTERN_METHOD(setUserAgent:NSString)
RCT_EXTERN_METHOD(setMessageHandler:NSString)
RCT_EXTERN_METHOD(setNavigationBarStyle:(NSDictionary *))
RCT_EXTERN_METHOD(setLoadingStyle:(NSDictionary *))
RCT_EXTERN_METHOD(setNavigationBarHidden:(nonnull BOOL))
RCT_EXTERN_METHOD(renderTitle:(NSDictionary *))
RCT_EXTERN_METHOD(renderActions:(NSArray *))
@end
