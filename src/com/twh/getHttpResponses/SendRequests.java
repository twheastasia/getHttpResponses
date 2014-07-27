package com.twh.getHttpResponses;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import javax.swing.JTextArea;
import javax.swing.JLabel;

public class SendRequests {
	private static JTextField requestUrlTf;
	private static JTextField timesTf;
	private static JTextArea resultTa;
	private static String result = "";
	
	public static void main(String[] args) {
		
		//建立一个JFrame,JFrame的默认LayoutManager为BorderLayout
        JFrame f=new JFrame("GetHttpResponses");
        f.getContentPane().setLayout(null);
        
        requestUrlTf = new JTextField();
        requestUrlTf.setBounds(10, 27, 416, 21);
        f.getContentPane().add(requestUrlTf);
        requestUrlTf.setColumns(10);
        
        JButton sendRequestBtn = new JButton("\u53D1\u9001\u8BF7\u6C42");
        sendRequestBtn.setBounds(333, 58, 93, 23);
        f.getContentPane().add(sendRequestBtn);
        

        resultTa = new JTextArea();
        resultTa.setLineWrap(true);
        JScrollPane sp = new JScrollPane(resultTa);
        resultTa.setEditable(false);
        resultTa.setWrapStyleWord(true);
        sp.setBounds(10, 117, 416, 253);
        f.getContentPane().add(sp);
        
        JLabel lblNewLabel = new JLabel("\u91CD\u590D\u6B21\u6570\uFF1A");
        lblNewLabel.setBounds(10, 58, 76, 15);
        f.getContentPane().add(lblNewLabel);
        
        timesTf = new JTextField();
        timesTf.setText("1");
        timesTf.setBounds(74, 58, 66, 21);
        f.getContentPane().add(timesTf);
        timesTf.setColumns(10);
        
        JLabel lblNewLabel_1 = new JLabel("\u8BF7\u6C42\u5730\u5740\uFF1A");
        lblNewLabel_1.setBounds(10, 10, 78, 15);
        f.getContentPane().add(lblNewLabel_1);
        
        JLabel lblNewLabel_2 = new JLabel("\u8BF7\u6C42\u7ED3\u679C\uFF1A");
        lblNewLabel_2.setBounds(10, 92, 117, 15);
        f.getContentPane().add(lblNewLabel_2);
	
        f.setSize(450, 420);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        
        sendRequestBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				result = "";
				resultTa.setText("");
				if(requestUrlTf.getText().isEmpty()){
					resultTa.setText("没有输入请求url");
				}else if(timesTf.getText().isEmpty()){
					result = sendRequestsUrl(requestUrlTf.getText(), 1);
				}else{
					result = sendRequestsUrl(requestUrlTf.getText(), Integer.parseInt(timesTf.getText().toString()));
				}
				

			}
		});

	}
	
	private static String sendRequestsUrl(String url , int times )
	{
		String responseBodyStr = "";
		//构造HttpClient的实例
		HttpClient httpClient = new HttpClient();
		for(int i=0; i< times; i++){
			responseBodyStr = getResponseFromUrl(httpClient, url);
			resultTa.append("Responses: "+ responseBodyStr + "\n");
			resultTa.setCaretPosition(resultTa.getText().length());
		}
		return responseBodyStr;
	}
	
	
	private static String getResponseFromUrl(HttpClient httpClient, String url)
	{
		String body = "";
		byte[] responseBody = null;
		
		
		//创建GET方法的实例
		GetMethod getMethod = new GetMethod(url);
		//使用系统提供的默认的恢复策略
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,	new DefaultHttpMethodRetryHandler());
		try {
			//执行getMethod
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "+ getMethod.getStatusLine());
			}
			//读取内容
			responseBody = getMethod.getResponseBody();
		} catch (HttpException e) {
			//发生致命的异常，可能是协议不对或者返回的内容有问题
//			System.out.println("Please check your provided http address!");
//			e.printStackTrace();
			return "Please check your provided http address!";
		} catch (IOException e) {
			//发生网络异常
//			e.printStackTrace();
			return "Net error!";
		} finally {
			//释放连接
			getMethod.releaseConnection();
		}
		try {
			body = new String(responseBody,"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return body;
	}
}
