#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(RNTurbolinksManager, NSObject)
RCT_EXTERN_METHOD(reloadVisitable)
RCT_EXTERN_METHOD(reloadSession)
RCT_EXTERN_METHOD(dismiss)
RCT_EXTERN_METHOD(popToRoot)
RCT_EXTERN_METHOD(back)
RCT_EXTERN_METHOD(visit:(NSDictionary *))
RCT_EXTERN_METHOD(replaceWith:(NSDictionary *) tabIndex:(NSInteger *))
RCT_EXTERN_METHOD(renderTitle:(NSString *) subtitle:(NSString *) tabIndex:(NSInteger *))
RCT_EXTERN_METHOD(renderActions:(NSArray *) tabIndex:(NSInteger *))
RCT_EXTERN_METHOD(startSingleScreenApp:(NSDictionary *) options:(NSDictionary *))
RCT_EXTERN_METHOD(startTabBasedApp:(NSArray *) options:(NSDictionary *) selectedIndex:(NSInteger *))
RCT_EXTERN_METHOD(evaluateJavaScript:(NSString *) tabIndex:(NSInteger *) resolve:(RCTPromiseResolveBlock *) reject:(RCTPromiseRejectBlock *))
RCT_EXTERN_METHOD(notifyTabItem:(NSString *) tabIndex:(NSInteger *))
@end
