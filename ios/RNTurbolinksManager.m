#import "RNTurbolinksManager.h"
#import "RNTurbolinks.h"
#import <WebKit/WebKit.h>

@import Turbolinks;

@interface RNTurbolinksManager()
@end

@implementation RNTurbolinksManager

RCT_EXPORT_MODULE();

- (dispatch_queue_t)methodQueue {
    return dispatch_get_main_queue();
}

- (UIView *)view {
    _turbolinks = [[RNTurbolinks alloc] initWithBridge:self.bridge];
    return _turbolinks;
}

- (void)session:(Session *)session didProposeVisitToURL:(NSURL *)URL withAction:(enum Action)action {
    [self presentVisitableForSession:URL withAction:action];
}

- (void)session:(Session *)session didFailRequestForVisitable:(id<Visitable>)visitable withError:(NSError *)error {
}

- (void)sessionDidLoadWebView:(Session *)session {
    session.webView.navigationDelegate = session;
}

- (void)session:(Session *)session openExternalURL:(NSURL *)URL {
    [[UIApplication sharedApplication] openURL:URL];
}

- (void)sessionDidStartRequest:(Session *)session {
    [UIApplication sharedApplication].networkActivityIndicatorVisible=YES;
}

- (void)sessionDidFinishRequest:(Session *)session {
    [UIApplication sharedApplication].networkActivityIndicatorVisible=NO;
}

-(void) presentVisitableForSession:(NSURL *)URL withAction:(enum Action)action {
    VisitableViewController *visitableViewController = [[VisitableViewController alloc] initWithUrl:URL];
    if (action == ActionAdvance) {
        [_turbolinks.navigationController pushViewController:visitableViewController animated:YES];
    } else if (action == ActionReplace) {
        [_turbolinks.navigationController popViewControllerAnimated:NO];
        [_turbolinks.navigationController pushViewController:visitableViewController animated:NO];
    }
    [_session visit:visitableViewController];
}

- (void)userContentController:(WKUserContentController *)userContentController didReceiveScriptMessage:(WKScriptMessage *)message {
    if (_turbolinks.onMessage) _turbolinks.onMessage(@{@"message": message.body});
}

RCT_EXPORT_VIEW_PROPERTY(onMessage, RCTDirectEventBlock)

RCT_EXPORT_METHOD(initialize) {
    WKWebViewConfiguration *configuration = [[WKWebViewConfiguration alloc] init];
    [configuration.userContentController addScriptMessageHandler:self name:_userAgent];
    configuration.applicationNameForUserAgent = _userAgent;
    _session = [[Session alloc] initWithWebViewConfiguration:configuration];
    _session.delegate = self;
    [self presentVisitableForSession:_url withAction:ActionAdvance];
}

RCT_CUSTOM_VIEW_PROPERTY(url, NSString, RNTurbolinks) {
    _url = [RCTConvert NSURL:json];
}

RCT_CUSTOM_VIEW_PROPERTY(userAgent, NSString, RNTurbolinks) {
    _userAgent = [RCTConvert NSString:json];
}

@end
