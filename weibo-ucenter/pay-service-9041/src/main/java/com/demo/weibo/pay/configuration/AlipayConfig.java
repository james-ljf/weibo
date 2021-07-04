package com.demo.weibo.pay.configuration;
import java.io.FileWriter;
import java.io.IOException;

public class AlipayConfig {

	// APPID，收款账号是APPID对应支付宝账号
	public static String app_id = "2016102600764767";

	// 用户私钥，RSA2私钥
	public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCE8KCJYESxwQvkkZMeI0Te9kxr/qfNEfxDHldKnvZB8ZYvxciX6XFH2wMriRRG/zCDhWF0sB/vP8Ihqssppew2vibFHVr8BDTpP4Yv+mEp0t+/jDDHJBEDQSuo8ZiaqlHHNSOq7zZDTQIhlEA4/kInSd+cHOxoD2cLsVXi7/0L1pk8O//j7dHOrjbeFz1OMtksOBJydAVHw0byh1ho1TvAXNNMp5nhZbAyFOqMNxfeiS6vDWPDIzu6+6dZEkzF3EfnrKszGsrHbpRtX984TohAh13ogeqlLEAgJCAtn6NEKA+PjjyrXUfJvmLYgSdJQt4I6lWamEgJc6wiwucqunj9AgMBAAECggEAUIfDd/SNKHCc17UnvZX33mitGq6Ez8EZYz85+cYZvxCtvfq97OhI6xJM9U5wriFgLgvaYWlfrvqcivB/aZdNZEwECFwkBY42zyTgEKhHu6I2UD6IPdonRCVyYUEZBHgwWdBi7uVBpbs8k0LPNLcxM8OYZWVQACl0bqzaLkLasxRIuFXo2Hh4EZzTfSPybNplL0QOJGdtGx4URQt0WjVzGmMYvw4ElOfLbenIEUUgCLi6yejd8TepFs1VhdWkw5MZXtH1IJU1zbBdQ62wfpAsurnoEW8tNJ8vnLXYjYGZ/9yY3VWTaHTNInZ0YfrQGwFcfganzpPcBdQK742jBDibsQKBgQDBOeE0tnC4JBVua1T40Te9+7AdM43QbN9wAx1zhL/o55mR/uM4mJGSGoKeVxCjRYc8+PQJzF7zmBh5zDlPEiOyJ/ECygIbIE8JD957SdLHNLVz8KVn0oCpbuV/i2j6ZdtGo3SQmN+5xEx0MmASqSvpWlvL/NvxSRujPi4K7r/1/wKBgQCwIOcnbO6dH0fWB1vK/vLkCE+kTYoxBW28PqCKmmfUEq49MFGnMRIFCR6MuSCrWGPb+emIBIEZy5zPsXQzAl27/P8veXXPl5t9GehUtjk8wjbZ+wNWxN5FcZO2QLNL+gl1kjhckE8+yd1lKsOrRe6eOeYEYpFTDU+lndOML2tpAwKBgFVYjlttFd6PuZOa/t4j8v4MNF7Bjar427wq9BA5G1bxLQiqT4yI/wtMgFMV27hyFmjdAVVumqX3kSADD3EYbwHa2H5F4aY/oAk9pCRKmDn60wbRzQ1NkhzEacPrPMQUx0StH+haTb9MsARG196GMGoSyicQmck6ZGW3KjkT+V/bAoGBAJLpvyapvJefIgKrfKvk4aHINPZuH1komBIvgyZaCUVil6g+h72YNtmwc6UaPt/uCM3StjNY3n8v0guEj9KEpL9MF2f4Pob8hRxi4eLXZQMDVVGaCXz1FhqC1J1vdp9nX0CMySqczR0osN3UQAa7fIDK9sytk17deZRCA9WpMEUVAoGBAKwcrIbOzW0sExKUzERsI7T32yEk7aN943HrlOATxlnL3xl1LQP5sFdSG7qxvQAaeKyMQfz8ex1W32TiwJ8lyGv5A0MkeUkI1OY3ha5fpeOSaIju1qEbyoov63toDfOpx3F7wkfClHjATFlW62CnfaoDNKciEhHPHawitf3bXwoc";

	// 支付宝公钥
	public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAghd7roYBBlbv5izDiayPf4rORiw0LKDOlUMixdYrynWfYGjtefxRgu2cjx3cduWrlZG253Xz1KPg8FA2+ou6NQdYgl3tb83qgAqRV4IiGcMwnQgSiVXOvRYiFqdCGdMeurA8Nrhk6KmtLlK0TIkhtnC6Vts15ylpqg+UTokPaXGcpepU/ueQhC9dgKNXhs5EIQnmnz7OOG4b6POeKdBC0qsLiUSK20cVetKsJYU8KbAADAqhU06Y+3PFGSEyuHKFo/Y0qpk3IodaETGCMMXPPp0JgfduHpT4FxuW/hJQNDy/j/7S1Qp+hTCIuCAr0zfWfgcsm22zY5LeRAr8Cig4MQIDAQAB";

	// 服务器异步通知页面路径  需外网可访问，可内网穿透
	public static String notify_url = "http://v357h34871.qicp.vip/pay/add-order";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://www.baidu.com";

	// 签名方式
	public static String sign_type = "RSA2";

	// 字符编码格式
	public static String charset = "utf-8";

	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

	// 日志输出路径
	public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	/**
	 * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
	 * @param sWord 要写入日志里的文本内容
	 */
	public static void logResult(String sWord) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
			writer.write(sWord);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

