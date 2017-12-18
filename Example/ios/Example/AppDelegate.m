/**
 * Copyright (c) 2015-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

#import "AppDelegate.h"

#import <React/RCTBundleURLProvider.h>
#import <React/RCTRootView.h>
@import Turbolinks;

@implementation AppDelegate

  UINavigationController *navigationController;
  Session *session;

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
  NSURL *jsCodeLocation;

  jsCodeLocation = [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"index" fallbackResource:nil];
  
  navigationController = [UINavigationController new];
  session = [[Session alloc] init];
  self.window = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
  self.window.rootViewController = navigationController;
  [self.window makeKeyAndVisible];
  
  NSURL *urlInit = [NSURL URLWithString:@"https://3.basecamp.com/sign_in"];
  VisitableViewController *visitableViewController = [[VisitableViewController alloc] initWithUrl:urlInit];
  [navigationController pushViewController:visitableViewController animated:YES];
  [session visit:visitableViewController];
  
  return YES;
}

@end
