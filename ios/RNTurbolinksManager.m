#import "RNTurbolinksManager.h"
#import "RNTurbolinks.h"
#import "WebKit/WKWebView.h"

@import Turbolinks;

@interface RNTurbolinksManager()
@end

@implementation RNTurbolinksManager

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE();

- (UIView *)view
{
    _session = [[Session alloc] init];
    _session.delegate = self;
    
    if(!_turbolinks){
        _turbolinks = [[RNTurbolinks alloc] initWithManager:self bridge:self.bridge];
    }
    
    [_turbolinks.navigationBar setTranslucent:YES];
    
    return _turbolinks.view;
}

- (void)sessionDidLoadWebView:(Session * _Nonnull)session {
    session.webView.navigationDelegate = session;
}

- (void)session:(Session * _Nonnull)session openExternalURL:(NSURL * _Nonnull)URL {
    [[UIApplication sharedApplication] openURL:URL];
}

- (void)sessionDidFinishRequest:(Session * _Nonnull)session {
}

- (void)sessionDidStartRequest:(Session * _Nonnull)session {
}

- (void)session:(Session * _Nonnull)session didFailRequestForVisitable:(id<Visitable> _Nonnull)visitable withError:(NSError * _Nonnull)error {
}

- (void)session:(Session * _Nonnull)session didProposeVisitToURL:(NSURL * _Nonnull)URL withAction:(enum Action)action {
    VisitableViewController *visitableViewController = [[VisitableViewController alloc] initWithUrl:URL];
    [_turbolinks pushViewController:visitableViewController animated:YES];
    [_session visit:visitableViewController];
}

RCT_CUSTOM_VIEW_PROPERTY(url, NSstring, RNTurbolinks)
{
    VisitableViewController *visitableViewController = [[VisitableViewController alloc] initWithUrl:[RCTConvert NSURL:json]];
    [_turbolinks pushViewController:visitableViewController animated:YES];
    [_session visit:visitableViewController];
}

@end
