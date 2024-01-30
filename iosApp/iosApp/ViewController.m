//
//  ViewController.m
//  iosApp
//
//  Created by Kyle on 2024/1/30.
//

#import "ViewController.h"
#import <BugeAIChat/BugeAIChat.h>

@interface ViewController ()
@property (nonatomic, strong) UIViewController *chatAIVC;
@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    UIButton *showButton = [UIButton new];
    [showButton setTitle:@"show" forState:UIControlStateNormal];
    [self.view addSubview:showButton];
    showButton.frame = CGRectMake(100, 200, 100, 100);
    [showButton setBackgroundColor:[UIColor blueColor]];
    [showButton addTarget:self action:@selector(didClickShowButton) forControlEvents:UIControlEventTouchUpInside];
    NSLog(@"did show ViewController");
    [BAICAppModuleKt doInitKoin];
}

- (void)didClickShowButton {
    if (self.chatAIVC == nil) {
        self.chatAIVC = [BAICMain_iosKt MainViewController];
    }
    self.chatAIVC.modalPresentationStyle = UIModalPresentationFullScreen;
    
    [self.navigationController presentViewController:self.chatAIVC animated:true completion:^{
        
    }];
}


@end
