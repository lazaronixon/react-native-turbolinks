#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(RNTurbolinksManager, NSObject)
RCT_EXTERN_METHOD(reloadVisitable)
RCT_EXTERN_METHOD(reloadSession)
RCT_EXTERN_METHOD(dismiss:(BOOL *))
RCT_EXTERN_METHOD(popToRoot:(BOOL *))
RCT_EXTERN_METHOD(back:(BOOL *))
RCT_EXTERN_METHOD(visit:(NSDictionary *))
RCT_EXTERN_METHOD(replaceWith:(NSDictionary *))
RCT_EXTERN_METHOD(renderTitle:(NSString *) subtitle:(NSString *))
RCT_EXTERN_METHOD(renderActions:(NSArray *))
RCT_EXTERN_METHOD(startSingleScreenApp:(NSDictionary *) options:(NSDictionary *))
RCT_EXTERN_METHOD(injectJavaScript:(NSString *))
@end
