package com.techlife.compartilhafoto;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressWarnings("all")
public class MainActivity extends Activity {

	private ImageView imgSelect;
	int column_index;
	Intent intent = null;
	// Declare our Views, so we can access them later
	String logo, imagePath, Logo;
	Cursor cursor;
	// YOU CAN EDIT THIS TO WHATEVER YOU WANT
	private static final int SELECT_PICTURE = 1;
	String selectedImagePath;
	// ADDED
	String filemanagerstring;
	private Vector<ImageView> mySDCardImages;
	private Integer[] mThumbIds;
	TextView txt;
	private Typeface tf;
	private Typeface tftitle;
	private Button btnShareTxt;
	private Button btnShareImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// listeners of our two buttons
		View.OnClickListener handler = new View.OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {

				case R.id.buttonShareTextUrl:
					shareTextUrl();
					break;

				case R.id.buttonShareImage:
					shareImage();
					break;
				}
			}
		};

		// our buttons
		findViewById(R.id.buttonShareTextUrl).setOnClickListener(handler);
		findViewById(R.id.buttonShareImage).setOnClickListener(handler);
		imgSelect = (ImageView) findViewById(R.id.imgSelect);
		txt = (TextView) findViewById(R.id.title);
		TextView title = (TextView) findViewById(R.id.appTitle);
		btnShareTxt = (Button) findViewById(R.id.buttonShareTextUrl);
		btnShareImg = (Button) findViewById(R.id.buttonShareImage);
			
		imgSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// in onCreate or any event where your want the user to
				// select a file
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(
						Intent.createChooser(intent, "Select Picture"),
						SELECT_PICTURE);
			}
		});

		mySDCardImages = new Vector<ImageView>();
		List<Integer> drawablesId = new ArrayList<Integer>();
		int picIndex = 12345;
		File sdDir = new File("/sdcard/pictures/instagram");
		File[] sdDirFiles = sdDir.listFiles();

		/**
		 * for (File singleFile : sdDirFiles) {
		 * 
		 * ImageView myImageView = new ImageView(this);
		 * myImageView.setImageDrawable
		 * (Drawable.createFromPath(singleFile.getAbsolutePath()));
		 * myImageView.setId(picIndex); picIndex++;
		 * drawablesId.add(myImageView.getId());
		 * mySDCardImages.add(myImageView); Log.e("SD PICTURES/INSTAGRAM",
		 * ""+singleFile.getPath()); }
		 */

		Log.e("SD PICTURES/INSTAGRAM",
				"" + sdDirFiles[sdDirFiles.length - 1].getPath());
		selectedImagePath = sdDirFiles[sdDirFiles.length - 1].getPath();
		txt.setText(selectedImagePath);

		File imgFile = new File(sdDirFiles[sdDirFiles.length - 1].getPath());
		if (imgFile.exists()) {

			Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
					.getAbsolutePath());
			imgSelect.setImageBitmap(myBitmap);

		}

		String fontPath = "fonts/Perfograma.otf";
		String fontPathTitle = "fonts/Uni Sans Heavy.otf";
		// Loading Font Face
		tf = Typeface.createFromAsset(getAssets(), fontPath);
		tftitle = Typeface.createFromAsset(getAssets(), fontPathTitle);
		title.setTypeface(tftitle);
		btnShareImg.setTypeface(tf);
		btnShareTxt.setTypeface(tf);
		fontPath = "fonts/Uni Sans Thin.otf";
		tf = Typeface.createFromAsset(getAssets(), fontPath);
		txt.setTypeface(tf);
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void shareImage() {
		Intent share = new Intent(Intent.ACTION_SEND);

		// If you want to share a png image only, you can do:
		// setType("image/png"); OR for jpeg: setType("image/jpeg");
		share.setType("image/*");

		// Make sure you put example png image named myImage.png in your
		// directory
		// String imagePath = Environment.getExternalStorageDirectory()
		// + "/myImage.png";

		String imagePath = selectedImagePath;

		File imageFileToShare = new File(imagePath);

		Uri uri = Uri.fromFile(imageFileToShare);
		share.putExtra(Intent.EXTRA_STREAM, uri);

		startActivity(Intent.createChooser(share, "Share Image!"));
	}

	// Method to share either text or URL.
	private void shareTextUrl() {
		Intent share = new Intent(android.content.Intent.ACTION_SEND);
		share.setType("text/plain");
		share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

		// Add data to the intent, the receiving app will decide
		// what to do with it.
		share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
		share.putExtra(Intent.EXTRA_TEXT, "https://www.codeofaninja.com");

		startActivity(Intent.createChooser(share, "Share link!"));
	}

	// UPDATED
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				Uri selectedImageUri = data.getData();
				// OI FILE Manager
				filemanagerstring = selectedImageUri.getPath();
				// MEDIA GALLERY
				selectedImagePath = getPath(selectedImageUri);
				imgSelect.setImageURI(selectedImageUri);
				imagePath.getBytes();
				txt.setText(imagePath.toString());
				Bitmap bm = BitmapFactory.decodeFile(imagePath);
				// img1.setImageBitmap(bm);
			}
		}
	}

	// UPDATED!
	public String getPath(Uri uri) {
		String[] projection = { MediaColumns.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		imagePath = cursor.getString(column_index);

		return cursor.getString(column_index);
	}

}
