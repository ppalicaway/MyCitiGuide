package com.cellcity.citiguide.ar;

import java.text.DecimalFormat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cellcity.citiguide.screen.ARScreen;
import com.cellcity.citiguide.screen.R;
import com.cellcity.citiguide.util.Constants;

public class SeekBarLayout extends FrameLayout implements SeekBar.OnSeekBarChangeListener {
	private SeekBar seekB;
	private TextView leftTView, rightTView;
	private Context context;
	private SharedPreferences shared;
	private double prog;
	
	public SeekBarLayout(Context context) {
		super(context);
		
		this.context = context;
		
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setGravity(Gravity.CENTER);
		linearLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		
		leftTView = new TextView(context);
		leftTView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		leftTView.setText("0 km");
		leftTView.setPadding(0, 0, 10, 0);
		leftTView.setTextColor(Color.WHITE);
		
		rightTView = new TextView(context);
		rightTView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		rightTView.setText("10 km");
		rightTView.setPadding(10, 0, 0, 0);
		rightTView.setTextColor(Color.WHITE);
		
		seekB = new SeekBar(context);
		seekB.setLayoutParams(new LayoutParams(300, LayoutParams.WRAP_CONTENT));
		seekB.setOnSeekBarChangeListener(this);
		seekB.setMax(10000);
		seekB.setProgress((int) (ARScreen.radius*1000));
		
		linearLayout.addView(leftTView);
		linearLayout.addView(seekB);
		linearLayout.addView(rightTView);
		
		WindowManager w = ARScreen.instance.getWindowManager();
        Display d = w.getDefaultDisplay();
        int width = d.getWidth();
     	int height = d.getHeight();
		
		LayoutParams mainParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		System.out.println(width + " " + height + " " + linearLayout.getHeight() + " " + seekB.getHeight());
		setPadding(0, height - 50, 0, 0);
		setLayoutParams(mainParams);	
		
		this.addView(linearLayout);
		
		shared = context.getSharedPreferences(Constants.DEFAUL_SHARE_DATA, 0);
		double radius = shared.getFloat("radius", (float) 1);
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		prog = (double)progress/1000;
		DecimalFormat df = new DecimalFormat("#.##");
		String distance = df.format(prog);
		leftTView.setText(distance+" km");
	}
	
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		ARScreen.radius = (float) prog;
		try {
			ARScreen.progressDialog = ProgressDialog.show(context, "", context.getResources().getString(R.string.retrieving_data), true, true, new DialogInterface.OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					ARScreen.instance.finish();
				}
			});
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		Thread t = new Thread(null, new GetJSON(), "initR");
		t.start();
	}
}
