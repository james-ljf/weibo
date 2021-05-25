# weibo
# 微博版本1.0.0

# git-ljf

weibo     
├── weibo-api 			  // 公共接口模块(供服务使用feign调用)
├── weibo-gateway         // 网关模块 [8081]
├── weibo-message         // 信息模块 [9001]
├── weibo-file            // 文件上传模块[9100]
├── weibo-common          // 通用模块
├── weibo-ucenter         // 用户前台业务模块
│       └── signup-service                    // 登录注册模块 [9011]
│       └── user-service                      // 用户操作模块 [9021]
│       └── microblog-service                 // 微博模块 [9031]
│       └── comment-service                   // 评论模块 [9041]
│		└── chat-service                      // 网页聊天模块 [9051]
│		└── hot-search-service                // 热搜模块 [9061]
│		└── pay-service                       // 支付模块 [9071]
│		└── vip-service                	      // vip模块 [9081]
├── weibo-system          // 管理员后台业务模块
├── pom.xml               // 公共依赖(版本控制)