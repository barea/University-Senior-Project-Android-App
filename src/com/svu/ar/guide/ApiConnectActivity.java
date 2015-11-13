/*
 ********** SVU **********
 ********** Barea_27786 **********
 *********** API Connect Activity **********
 *********** Display Result **********
 */

package com.svu.ar.guide;

import com.svu.ar.guide.R;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.Intent;

public class ApiConnectActivity extends Activity {
	private TextView name, Descriptio;
	private ProgressBar progressBar1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apiconnect);

		AsyncTaskRunner runner = new AsyncTaskRunner();
		runner.execute();

	}

	@Override
	public void onBackPressed() {
		// Back to Camera Activity (MainActivity)
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
		ApiConnectActivity.this.finish();
	}

	private class AsyncTaskRunner extends AsyncTask<String, Void, String> {

		Item item;
		
		@Override
		protected void onPreExecute() {
			progressBar1 = (ProgressBar)findViewById(R.id.progressBar1);
			progressBar1.setVisibility(View.VISIBLE);
			name = (TextView) findViewById(R.id.name);
			Descriptio = (TextView) findViewById(R.id.description);
			
		}

		@Override
		protected String doInBackground(String... strings) {
			// get item id from camera preview

			Intent intent = getIntent();
			String itemId = intent.getStringExtra("itemId");
			// do api call request
			item = APIService.getItemById(itemId);
			return "done";
		}

		@Override
		protected void onPostExecute(String result) {
			// update UI
			progressBar1.setVisibility(View.GONE);
			name.setText(item.getName());
			Descriptio.setText(item.getDescription());
			getActionBar().hide();

		}
	}

}
