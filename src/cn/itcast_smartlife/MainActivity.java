package cn.itcast_smartlife;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private Button btnOpenLed;
	private Button btnCloseLed;
	private Button btnDisCon;
	private Button btnMonitorStatus;
	private Button btnConAgain;

	private TextView tvMonitor;

	private String ip;
	private String port;

	// private String ip;
	// private int port;

	private Socket socket;
	private BufferedWriter writer;

	private boolean flag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			//
			Intent intent = getIntent();
			ip = intent.getStringExtra("serverIP");
			port = intent.getStringExtra("severPort");

			// int port = 1;
			// Toast.makeText(this, "ip=" + ip + "port=" + port,
			// Toast.LENGTH_LONG)
			// .show();
			socket = new Socket(ip, Integer.parseInt(port));
			Toast.makeText(this, "网络链接成功！", Toast.LENGTH_LONG).show();

			// setContentView(R.layout.activity_main);

			btnOpenLed = (Button) findViewById(R.id.btn_openLed);
			btnCloseLed = (Button) findViewById(R.id.btn_closeLed);
			btnDisCon = (Button) findViewById(R.id.btn_DisCon);
			btnConAgain = (Button) findViewById(R.id.btn_Con_Again);
			btnMonitorStatus = (Button) findViewById(R.id.btn_monitor);

			tvMonitor = (TextView) findViewById(R.id.tv_monitor);

			btnOpenLed.setOnClickListener(this);
			btnCloseLed.setOnClickListener(this);
			btnDisCon.setOnClickListener(this);
			btnMonitorStatus.setOnClickListener(this);
			btnConAgain.setOnClickListener(this);

		} catch (Exception e) {
			// TODO Auto-generated catch block

			Toast.makeText(this, "网络连接失败", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(MainActivity.this, loginActivity.class);
			startActivity(intent);
		}
		// }

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_openLed:
			// 打开灯

			String msg = "led1";
			try {
				writer = new BufferedWriter(new OutputStreamWriter(
						socket.getOutputStream()));

				writer.write(msg);
				writer.flush();
				tvMonitor.setText("灯已打开!");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;

		case R.id.btn_closeLed:
			// 关闭灯
			String msg2 = "led2";
			try {
				writer = new BufferedWriter(new OutputStreamWriter(
						socket.getOutputStream()));

				writer.write(msg2);
				writer.flush();
				tvMonitor.setText("灯已关闭!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;

		case R.id.btn_monitor:
			// 点击监测按钮
			// Toast.makeText(this, "你点击了抓状态", Toast.LENGTH_LONG).show();

			// 发送反馈指令
			String msg3 = "start";
			try {
				writer = new BufferedWriter(new OutputStreamWriter(
						socket.getOutputStream()));

				writer.write(msg3);
				writer.flush();
				tvMonitor.setText("正在获取状态信息...");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 获取输入流
			InputStream is = null;
			try {

				is = socket.getInputStream();
				// Toast.makeText(this, "请稍后，监测数据正在接受中...", Toast.LENGTH_LONG)
				// .show();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				tvMonitor.setText("对不起，没有接受到监测的数据");
				// Toast.makeText(this, "对不起，没有接受到监测的数据", Toast.LENGTH_LONG)
				// .show();
			}
			byte[] bys = new byte[1024];
			int len = 0;
			try {
				len = is.read(bys);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				tvMonitor.setText("对不起，监测数据读取发生错误");
				// Toast.makeText(this, "对不起，监测数据读取发生错误", Toast.LENGTH_LONG)
				// .show();
			}// 阻塞
			String feedBack = new String(bys, 0, len);
			// System.out.println("client:" + client);
			tvMonitor.setText("监测到的信息为： " + feedBack);
			
			break;

		case R.id.btn_DisCon:
			// 断开服务器连接
			try {
				socket.close();
				flag = true;
				Toast.makeText(this, "断开服务器连接", Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "断开网络操作失败", Toast.LENGTH_LONG).show();
			}

			break;
		case R.id.btn_Con_Again:
			// 重新连接服务器
			if (flag) {

				Toast.makeText(this, "重新连接服务器", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(MainActivity.this,
						loginActivity.class);

				// intent.putExtra("serverIP", ip);
				// intent.putExtra("severPort", port);
				intent.putExtra("againCon", "YES");

				startActivity(intent);
				// Intent intent = getIntent();
				// String ip = intent.getStringExtra("serverIP");
				// int port =
				// Integer.parseInt(intent.getStringExtra("severPort"));
			} else {
				Toast.makeText(this, "请先断接服务器，否则不能执行此操作!", Toast.LENGTH_LONG)
						.show();

			}
			break;

		}

	}
}
