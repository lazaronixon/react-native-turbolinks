#import "RNTurbolinksManager.h"
#import "RNTurbolinks.h"
#import "WebKit/WKWebView.h"
#import "WebKit/WKWebViewConfiguration.h"
#import "WebKit/WKUserContentController.h"

@import Turbolinks;

@interface RNTurbolinksManager()
@end

@implementation RNTurbolinksManager

RCT_EXPORT_MODULE();

- (dispatch_queue_t)methodQueue {
    return dispatch_get_main_queue();
}

- (UIView *)view {
    _turbolinks = [[RNTurbolinks alloc] initWithManager:self bridge:self.bridge];
    _navigationController = [[UINavigationController alloc] init];
    _navigationController.view.frame = _turbolinks.bounds;
    [_navigationController.navigationBar setTranslucent:YES];
    [_turbolinks addSubview:_navigationController.view];
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
        [_navigationController pushViewController:visitableViewController animated:YES];
    } else if (action == ActionReplace) {
        [_navigationController popViewControllerAnimated:NO];
        [_navigationController pushViewController:visitableViewController animated:NO];
    }
    [_session visit:visitableViewController];
}

RCT_EXPORT_METHOD(initialize) {
    WKWebViewConfiguration *configuration = [[WKWebViewConfiguration alloc] init];
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
